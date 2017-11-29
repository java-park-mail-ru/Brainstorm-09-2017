package application.game.messages;

import application.websocket.ClientMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientSnap extends ClientMessage {
    @JsonProperty("burstingBubbleId")
    private Long burstingBubbleId;

    public ClientSnap(@JsonProperty("burstingBubbleId") Long bubbleId) {
        this.burstingBubbleId = bubbleId;
    }

    public long getBurstingBubbleId() {
        return burstingBubbleId;
    }
}
