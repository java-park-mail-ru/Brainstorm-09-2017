package application.game.messages;

import application.game.base.Bubble;
import application.game.base.Player;
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

    @JsonProperty("isFinished")
    private Boolean isFinished;


    public ServerSnap(Player currentPlayer, Player enemy, Collection<Bubble> bubbles, Boolean isFinished) {
        this.currentPlayer = currentPlayer;
        this.enemy = enemy;
        this.bubbles = bubbles;
        this.isFinished = isFinished;
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

    public Boolean getFinished() {
        return isFinished;
    }
}
