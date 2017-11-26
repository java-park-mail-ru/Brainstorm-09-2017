package application.game;

import application.game.base.Bubble;
import application.game.messages.*;
import application.game.base.Player;
import application.servicies.UsersService;
import application.websocket.ClientMessage;
import application.websocket.Message;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.*;


@Component
public class GameImpl implements Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Map<Long, Bubble> bubbles;
    private Queue<ClientMessage> clientMessages;
    private Date startTime;
    private Date lastFrameTime;
    private BubbleFactory bubbleFactory;
    private Boolean isFinished = false;

    private static RemotePointService remotePointService;
    private static UsersService usersService;

    private static final Long BLISTARING_PERIOD = 500L;


    public GameImpl(Player firstPlayer, Player secondPlayer) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.bubbleFactory = new BubbleFactory(BLISTARING_PERIOD);
        bubbles = new TreeMap<>();
        clientMessages = new LinkedList<>();
        startTime = new Date();
        lastFrameTime = new Date();

        broadcost();
    }


    @Autowired
    private GameImpl(RemotePointService rps, UsersService us) {
        GameImpl.remotePointService = rps;
        GameImpl.usersService = us;
    }


    @Override
    public void gmStep() {
        final Date now = new Date();
        mechanic(now.getTime() - lastFrameTime.getTime());
        lastFrameTime = now;
    }

    protected void mechanic(Long frameTime) {
        handleMessages();

        for (Bubble bubble : bubbles.values()) {
            bubble.grow(frameTime);
            if (bubble.isBurst()) {
                finish();
            }
        }

        final Bubble bubble = bubbleFactory.produce();
        if (bubble != null) {
            bubbles.put(bubble.getId(), bubble);
            broadcost(new NewBubbles(bubble));
        }
    }

    protected void handleMessages() {
        if (!clientMessages.isEmpty()) {
            final List<ClientSnap> executedSnaps = new ArrayList<>();
            while (!clientMessages.isEmpty()) {
                final ClientMessage msg = clientMessages.remove();
                final Player player = getPlayer(msg.getSenderId());
                if (player != null) {
                    if (ClientSnap.class.isInstance(msg)) {
                        final ClientSnap snap = (ClientSnap) msg;
                        final Bubble bustingBubble = bubbles.remove(snap.getBurstingBubbleId());
                        if (bustingBubble != null) {
                            player.addPoints(1L);
                            executedSnaps.add(snap);
                        }
                    } else if (Surrender.class.isInstance(msg)) {
                        player.surrender();
                        broadcost();
                        if (firstPlayer.isSurrender() && secondPlayer.isSurrender()) {
                            finish();
                        }
                    }
                }
            }
            if (!executedSnaps.isEmpty()) {
                broadcost(
                        new BurstingBubbles(firstPlayer.getScore(), secondPlayer.getScore(), executedSnaps),
                        new BurstingBubbles(secondPlayer.getScore(), firstPlayer.getScore(), executedSnaps)
                );
            }
        }
    }


    @Override
    public void addClientMessage(ClientMessage msg) {
        System.out.println(msg);
        clientMessages.add(msg);
    }


    @Override
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


    @Override
    public void broadcost() {
        broadcost(getSnapshot(firstPlayer.getUserId()), getSnapshot(secondPlayer.getUserId()));
    }

    public void broadcost(Message msg) {
        broadcost(msg, msg);
    }

    public void broadcost(Message forFirstPlayer, Message forSecondPlayer) {
        try {
            remotePointService.sendMessageToUser(firstPlayer.getUserId(), forFirstPlayer);
            remotePointService.sendMessageToUser(secondPlayer.getUserId(), forSecondPlayer);
        } catch (IOException ignored) {
        }
    }


    @Override
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


    @Override
    public void emergencyStop() {
        isFinished = true;
        remotePointService.cutDownConnection(firstPlayer.getUserId(), CloseStatus.SERVER_ERROR);
        remotePointService.cutDownConnection(secondPlayer.getUserId(), CloseStatus.SERVER_ERROR);
    }


    @Override
    public Boolean hasPlayer(Long userId) {
        final Boolean isFirst = firstPlayer.getUserId().equals(userId) && !firstPlayer.isSurrender();
        final Boolean isSecond = secondPlayer.getUserId().equals(userId) && !secondPlayer.isSurrender();
        return isFirst || isSecond;
    }


    public @Nullable Player getPlayer(Long userId) {
        if (firstPlayer.getUserId().equals(userId)) {
            return firstPlayer;
        }
        if (secondPlayer.getUserId().equals(userId)) {
            return secondPlayer;
        }
        return null;
    }
}
