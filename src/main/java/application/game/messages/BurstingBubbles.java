package application.game.messages;

import application.game.base.Player;
import application.websocket.Message;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BurstingBubbles extends Message {
    @JsonProperty("currentPlayer")
    private Player currentPlayer;

    @JsonProperty("enemy")
    private Player enemy;

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


    public BurstingBubbles(Player currentPlayer, Player enemy, List<ClientSnap> clientSnaps) {
        this.currentPlayer = currentPlayer;
        this.enemy = enemy;
        this.burstingBubbles = clientSnaps.stream().map(BurstingBubble::new).collect(Collectors.toList());
    }


    public List<BurstingBubble> getBurstingBubbles() {
        return burstingBubbles;
    }
}
