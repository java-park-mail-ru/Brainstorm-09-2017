package application.game.handlers;

import application.game.GameService;
import application.game.messages.ClientSnap;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class ClientSnapHandler extends MessageHandler<ClientSnap> {
    private @NotNull GameService gameService;
    private @NotNull MessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(@NotNull GameService gameService, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(ClientSnap.class);
        this.gameService = gameService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(ClientSnap.class, this);
    }

    @Override
    public void handle(@NotNull ClientSnap message, @NotNull Long forUser) {
        message.setUserId(forUser);
        gameService.addClientSnapshot(forUser, message);
    }
}
