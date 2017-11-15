package application.game;

import application.game.base.Bubble;
import application.game.base.ClientSnap;
import application.game.base.Player;

import java.util.*;


public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Map<Long, Bubble> bubbles;
    private Queue<ClientSnap> clientSnapshots;
    private Date startTime;
    private BubbleFactory bubbleFactory;


    public Game(Player firstPlayer, Player secondPlayer, BubbleFactory bubbleFactory) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.bubbleFactory = bubbleFactory;
        bubbles = new TreeMap<>();
        clientSnapshots = new LinkedList<>();
        startTime = new Date();
    }


    public void gmStep(Long frameTime) {
        Boolean shouldSendSnap = false;

        if (!clientSnapshots.isEmpty()) {
            shouldSendSnap = true;
        }
        while (!clientSnapshots.isEmpty()) {
            final ClientSnap snap = clientSnapshots.remove();
            bubbles.remove(snap.getBurstingBubbleId());
        }

        bubbles.values().forEach(bubble -> {bubble.grow(frameTime);});

        final Bubble bubble = bubbleFactory.produce();
        if (bubble != null) {
            bubbles.put(bubble.getId(), bubble);
            shouldSendSnap = true;
        }

        if (shouldSendSnap) {

        }
    }


    public void addClientSnapshot(ClientSnap snap) {
        clientSnapshots.add(snap);
    }
}
