package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class BurstingBubbles extends Message {
    @JsonProperty("burstingBubbleIds")
    List<BurstingBubble> burstingBubbles;


    static class BurstingBubble {
        @JsonProperty("userId")
        private Long userId;
        @JsonProperty("burstingBubbleId")
        private Long burstingBubbleId;

        BurstingBubble(Long userId, Long burstingBubbleId) {
            this.userId = userId;
            this.burstingBubbleId = burstingBubbleId;
        }

        BurstingBubble(ClientSnap clientSnap) {
            this.userId = clientSnap.getUserId();
            this.burstingBubbleId = clientSnap.getBurstingBubbleId();
        }

        public Long getUserId() {
            return userId;
        }

        public Long getBurstingBubbleId() {
            return burstingBubbleId;
        }
    }


    public BurstingBubbles(List<ClientSnap> clientSnaps) {
        burstingBubbles = clientSnaps.stream().map(BurstingBubble::new).collect(Collectors.toList());
    }


    public List<BurstingBubble> getBurstingBubbles() {
        return burstingBubbles;
    }
}
