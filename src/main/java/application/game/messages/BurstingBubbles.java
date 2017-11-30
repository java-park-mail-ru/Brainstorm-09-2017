package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

public class BurstingBubbles extends Message {
    @JsonProperty("currentPlayerScore")
    private Long currentPlayerScore;

    @JsonProperty("enemyScore")
    private Long enemyScore;

    @JsonProperty("burstingBubbleIds")
    private List<BurstingBubble> burstingBubbles;


    public static class BurstingBubble {
        @JsonProperty("userId")
        private Long userId;
        @JsonProperty("burstingBubbleId")
        private Long burstingBubbleId;

        BurstingBubble(Long userId, Long burstingBubbleId) {
            this.userId = userId;
            this.burstingBubbleId = burstingBubbleId;
        }

        BurstingBubble(ClientSnap clientSnap) {
            this.burstingBubbleId = clientSnap.getBurstingBubbleId();
        }

        public Long getUserId() {
            return userId;
        }

        public Long getBurstingBubbleId() {
            return burstingBubbleId;
        }
    }


    public BurstingBubbles(Long currentPlayerScore, Long enemyScore, List<ClientSnap> clientSnaps) {
        this.currentPlayerScore = currentPlayerScore;
        this.enemyScore = enemyScore;
        this.burstingBubbles = clientSnaps.stream().map(BurstingBubble::new).collect(Collectors.toList());
    }


    public List<BurstingBubble> getBurstingBubbles() {
        return burstingBubbles;
    }

    public Long getCurrentPlayerScore() {
        return currentPlayerScore;
    }

    public Long getEnemyScore() {
        return enemyScore;
    }
}
