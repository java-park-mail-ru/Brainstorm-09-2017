package application.game;

import application.game.messages.ServerSnap;
import application.websocket.ClientMessage;

public interface Game {
    void gmStep();

    void addClientMessage(ClientMessage msg);

    ServerSnap getSnapshot(Long currentPlayerId);

    Boolean isFinished();

    void emergencyStop();

    Boolean hasPlayer(Long userId);

    void broadcost();
}
