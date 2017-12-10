package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BurstingBubbles extends Message {
    @JsonProperty("currentPlayerScore")
    private Long currentPlayerScore;

    @JsonProperty("enemyScore")
    private Long enemyScore;

    @JsonProperty("burstingBubbleIds")
    private List<BurstingBubbleId> burstingBubbles;


    public static class BurstingBubbleId {
        @JsonProperty("userId")
        private Long userId;
        @JsonProperty("burstingBubbleId")
        private Long burstingBubbleId;


        @JsonCreator
        BurstingBubbleId(@JsonProperty("userId") Long userId,
                         @JsonProperty("burstingBubbleId") Long burstingBubbleId) {
            this.userId = userId;
            this.burstingBubbleId = burstingBubbleId;
        }


        BurstingBubbleId(ClientSnap clientSnap) {
            this.burstingBubbleId = clientSnap.getBurstingBubbleId();
        }

        public Long getUserId() {
            return userId;
        }

        public Long getBurstingBubbleId() {
            return burstingBubbleId;
        }


        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            final BurstingBubbleId that = (BurstingBubbleId) obj;

            if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
                return false;
            }
            return burstingBubbleId != null ? burstingBubbleId.equals(that.burstingBubbleId) : that.burstingBubbleId == null;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            final int hashConst = 31;
            result = hashConst * result + (burstingBubbleId != null ? burstingBubbleId.hashCode() : 0);
            return result;
        }
    }


    @JsonCreator
    public BurstingBubbles(@JsonProperty("currentPlayerScore") Long currentPlayerScore,
                           @JsonProperty("enemyScore") Long enemyScore,
                           @JsonProperty("burstingBubbleIds") Collection<BurstingBubbleId> burstingBubbles) {
        this.currentPlayerScore = currentPlayerScore;
        this.enemyScore = enemyScore;
        this.burstingBubbles = new ArrayList<>(burstingBubbles);
    }

    public BurstingBubbles(Long currentPlayerScore,
                           Long enemyScore,
                           List<ClientSnap> clientSnaps) {
        this.currentPlayerScore = currentPlayerScore;
        this.enemyScore = enemyScore;
        this.burstingBubbles = clientSnaps.stream().map(BurstingBubbleId::new).collect(Collectors.toList());
    }


    public List<BurstingBubbleId> getBurstingBubbles() {
        return burstingBubbles;
    }

    public Long getCurrentPlayerScore() {
        return currentPlayerScore;
    }

    public Long getEnemyScore() {
        return enemyScore;
    }
}
