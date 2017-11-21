package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientSnap extends Message {
    private Long userId;

    @JsonProperty("burstingBubbleId")
    private Long burstingBubbleId;

    public ClientSnap(@JsonProperty("burstingBubbleId") Long bubbleId) {
        this.burstingBubbleId = bubbleId;
    }

    public Long getBurstingBubbleId() {
        return burstingBubbleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
