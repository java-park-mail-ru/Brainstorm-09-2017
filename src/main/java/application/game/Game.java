package application.game;

import application.game.base.Player;
import application.game.messages.ServerSnap;
import application.websocket.Letter;

import java.util.List;
import java.util.Queue;

public interface Game {
    void gmStep();

    void addClientMessage(Letter msg);

    ServerSnap getSnapshot(Long currentPlayerId);

    Boolean isFinished();

    Boolean hasPlayer(Long userId);

    Queue<Letter> getMessagesForSend();

    List<Player> getPlayers();

    void broadcost();
}
