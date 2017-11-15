package application.game;

import application.game.base.Bubble;
import application.game.base.ClientSnap;
import application.game.base.Player;
import application.game.base.ServerSnap;
import application.servicies.UsersService;
import application.websocket.RemotePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.*;


public class Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Map<Long, Bubble> bubbles;
    private Queue<ClientSnap> clientSnapshots;
    private Date startTime;
    private Date lastFrameTime;
    private BubbleFactory bubbleFactory;
    private Boolean isFinished = false;
    @Autowired
    private static RemotePointService remotePointService;
    @Autowired
    private static UsersService usersService;

    private static final Long BLISTARING_PERIOD = 500L;



    public Game(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.bubbleFactory = new BubbleFactory(BLISTARING_PERIOD);
        bubbles = new TreeMap<>();
        clientSnapshots = new LinkedList<>();
        startTime = new Date();
        lastFrameTime = new Date();
    }

    public void gmStep() {
        final Date now = new Date();
        mechanic(now.getTime() - lastFrameTime.getTime());
        lastFrameTime = now;
    }

    private void mechanic(Long frameTime) {
        Boolean shouldSendSnap = false;

        if (!clientSnapshots.isEmpty()) {
            while (!clientSnapshots.isEmpty()) {
                final ClientSnap snap = clientSnapshots.remove();
                bubbles.remove(snap.getBurstingBubbleId());
            }
            shouldSendSnap = true;
        }

        for (Bubble bubble : bubbles.values()) {
            bubble.grow(frameTime);
            if (bubble.isBurst()) {
                finish();
            }
        }

        final Bubble bubble = bubbleFactory.produce();
        if (bubble != null) {
            bubbles.put(bubble.getId(), bubble);
            shouldSendSnap = true;
        }

        if (shouldSendSnap) {
            broadcost();
        }
    }


    public void addClientSnapshot(ClientSnap snap) {
        clientSnapshots.add(snap);
    }


    public ServerSnap getSnapshot(Long currentPlayerId) {
        final Player currentPlayer;
        final Player enymy;
        if (firstPlayer.getUserId().equals(currentPlayerId)) {
            currentPlayer = firstPlayer;
            enymy = secondPlayer;
        } else {
            currentPlayer = secondPlayer;
            enymy = firstPlayer;
        }
        return new ServerSnap(currentPlayer, enymy, bubbles.values(), isFinished);
    }


    public void broadcost() {
        try {
            remotePointService.sendMessageToUser(firstPlayer.getUserId(), getSnapshot(firstPlayer.getUserId()));
            remotePointService.sendMessageToUser(secondPlayer.getUserId(), getSnapshot(secondPlayer.getUserId()));
        } catch (IOException ignored) {
        }
    }


    public Boolean isFinished() {
        return isFinished;
    }


    private void finish() {
        isFinished = true;
        usersService.record(firstPlayer.getUserId(), firstPlayer.getScore());
        usersService.record(secondPlayer.getUserId(), secondPlayer.getScore());
        broadcost();
        remotePointService.cutDownConnection(firstPlayer.getUserId(), CloseStatus.NORMAL);
        remotePointService.cutDownConnection(secondPlayer.getUserId(), CloseStatus.NORMAL);
    }


    public void emergencyStop() {
        isFinished = true;
        remotePointService.cutDownConnection(firstPlayer.getUserId(), CloseStatus.SERVER_ERROR);
        remotePointService.cutDownConnection(secondPlayer.getUserId(), CloseStatus.SERVER_ERROR);
    }
}
