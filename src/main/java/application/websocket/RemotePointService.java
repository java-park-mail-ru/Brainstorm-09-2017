package application.websocket;

import application.game.GameService;
import application.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class RemotePointService {
    private Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    @Autowired
    private GameService gameService;


    public RemotePointService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerUser(@NotNull User user, @NotNull WebSocketSession webSocketSession) {
        sessions.put(user.getId(), webSocketSession);
        gameService.addUser(user);
    }

    public boolean isConnected(@NotNull Long userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    public void removeUser(@NotNull Long userId) {
        sessions.remove(userId);
    }

    public void cutDownConnection(@NotNull Long userId, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
                removeUser(userId);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessage(@NotNull Message msg) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(msg.getAddresserId());
        if (webSocketSession == null) {
            throw new IOException("No game websocket for user " + msg.getAddresserId());
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("Session is closed or not exsists");
        }
        //noinspection OverlyBroadCatchBlock
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
        } catch (IOException e) {
            throw new IOException("Unnable to send message", e);
        }
    }

    public void reset() {
        final Collection<WebSocketSession> webSocketSessions = sessions.values();
        for (WebSocketSession session : webSocketSessions) {
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException ignore) {
            }
        }
        sessions.clear();
    }
}
