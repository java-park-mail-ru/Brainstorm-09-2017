package application.game.base;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientSnap extends Message {
    @JsonProperty("burstingBubbleId")
    private Long burstingBubbleId;

    public ClientSnap(@JsonProperty("burstingBubbleId") Long bubbleId) {
        this.burstingBubbleId = bubbleId;
    }

    public Long getBurstingBubbleId() {
        return burstingBubbleId;
    }
}
