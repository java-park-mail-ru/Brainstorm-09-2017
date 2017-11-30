package application.websocket;

import application.models.User;
import application.servicies.UsersService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;


public class GameSocketHandler extends TextWebSocketHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);
    private static final CloseStatus ACCESS_DENIED = new CloseStatus(4500, "Not logged in. Access denied");

    private @NotNull UsersService usersService;
    private final @NotNull MessageHandlerContainer messageHandlerContainer;
    private final @NotNull RemotePointService remotePointService;

    private final ObjectMapper objectMapper;


    public GameSocketHandler(@NotNull MessageHandlerContainer messageHandlerContainer,
                             @NotNull UsersService usersService,
                             @NotNull RemotePointService remotePointService,
                             ObjectMapper objectMapper) {
        this.messageHandlerContainer = messageHandlerContainer;
        this.usersService = usersService;
        this.remotePointService = remotePointService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        final User user = usersService.auth(webSocketSession);
        if (user == null) {
            LOGGER.warn("User requested websocket is not registred or not logged in. Openning websocket session is denied.");
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        remotePointService.registerUser(user, webSocketSession);
    }

    @Override
    protected void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        if (!webSocketSession.isOpen()) {
            return;
        }
        final User user = usersService.auth(webSocketSession);
        if (user == null) {
            closeSessionSilently(webSocketSession, ACCESS_DENIED);
            return;
        }
        handleMessage(user, message);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    private void handleMessage(User userProfile, TextMessage text) {
        final Message message;
        try {
            message = objectMapper.readValue(text.getPayload(), Message.class);
            message.setAddresserId(userProfile.getId());
        } catch (IOException ex) {
            LOGGER.error("Wrong json format at game response", ex);
            return;
        }
        try {
            messageHandlerContainer.handle(message, userProfile.getId());
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + message.getClass().getName() + " with content: " + text, e);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        final User user = usersService.auth(webSocketSession);
        if (user == null) {
            LOGGER.warn("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        remotePointService.removeUser(user.getId());
    }

    private void closeSessionSilently(@NotNull WebSocketSession session, @Nullable CloseStatus closeStatus) {
        final CloseStatus status = closeStatus == null ? SERVER_ERROR : closeStatus;
        //noinspection OverlyBroadCatchBlock
        try {
            session.close(status);
        } catch (Exception ignore) {
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
