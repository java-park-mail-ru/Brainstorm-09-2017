package application.game.base;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class ServerSnap extends Message {
    @JsonProperty("currentPlayer")
    private Player currentPlayer;

    @JsonProperty("enemy")
    private Player enemy;

    @JsonProperty("bubbles")
    private Collection<Bubble> bubbles;


    public ServerSnap(Player currentPlayer, Player enemy, Collection<Bubble> bubbles) {
        this.currentPlayer = currentPlayer;
        this.enemy = enemy;
        this.bubbles = bubbles;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getEnemy() {
        return enemy;
    }

    public Collection<Bubble> getBubbles() {
        return bubbles;
    }
}
