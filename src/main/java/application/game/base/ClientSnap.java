package application.game.base;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientSnap {
    @JsonProperty("burstingBubbleId")
    private Long burstingBubbleId;

    public ClientSnap(@JsonProperty("burstingBubbleId") Long bubbleId) {
        this.burstingBubbleId = bubbleId;
    }

    public Long getBurstingBubbleId() {
        return burstingBubbleId;
    }
}
