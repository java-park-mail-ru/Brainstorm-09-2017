package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientSnap extends Message {
    @JsonProperty("burstingBubbleId")
    private Long burstingBubbleId;

    public ClientSnap(@JsonProperty("burstingBubbleId") Long bubbleId) {
        this.burstingBubbleId = bubbleId;
    }

    public long getBurstingBubbleId() {
        return burstingBubbleId;
    }
}
