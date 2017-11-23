package application.game.handlers;

import application.game.GameService;
import application.game.messages.Surrender;
import application.websocket.MessageHandler;
import application.websocket.MessageHandlerContainer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SurrenderHandler extends MessageHandler<Surrender> {
    private @NotNull GameService gameService;
    private @NotNull MessageHandlerContainer messageHandlerContainer;

    public SurrenderHandler(@NotNull GameService gameService, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(Surrender.class);
        this.gameService = gameService;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(Surrender.class, this);
    }

    @Override
    public void handle(@NotNull Surrender message, @NotNull Long forUser) {
        message.setSenderId(forUser);
        gameService.addClientMessage(forUser, message);
    }
}
