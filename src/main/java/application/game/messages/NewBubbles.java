package application.game.messages;

import application.game.base.Bubble;
import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public class NewBubbles extends Message {
    @JsonProperty("bubbles")
    Collection<Bubble> bubbles;

    public NewBubbles(Collection<Bubble> bubbles) {
        this.bubbles = bubbles;
    }

    public Collection<Bubble> getBubbles() {
        return bubbles;
    }
}
