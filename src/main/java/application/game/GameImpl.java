package application.game;

import application.game.base.Bubble;
import application.game.messages.*;
import application.game.base.Player;
import application.websocket.Letter;
import application.websocket.Message;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;


public class GameImpl implements Game {
    private Player firstPlayer;
    private Player secondPlayer;
    private Map<Long, Bubble> bubbles = new TreeMap<>();
    private Queue<Letter> clientMessages = new LinkedList<>();
    private Queue<Letter> messagesForSend = new LinkedList<>();
    private Date startTime = new Date();
    private Date lastFrameTime = new Date();
    private BubbleFactory bubbleFactory;
    private Boolean isFinished = false;

    private static final Long BLISTARING_PERIOD = 500L;


    public GameImpl(Player firstPlayer, Player secondPlayer) throws IOException {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.bubbleFactory = new BubbleFactory(BLISTARING_PERIOD);
        broadcost();
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
                final Letter letter = clientMessages.remove();
                final Player player = getPlayer(letter.getAddresserId());
                if (player != null && !player.isSurrender()) {
                    final Message msg = letter.getMessage();

                    if (ClientSnap.class.isInstance(msg)) {
                        final ClientSnap snap = (ClientSnap) letter.getMessage();
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
    public void addClientMessage(Letter letter) {
        clientMessages.add(letter);
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

    protected void broadcost(Message msg) {
        broadcost(msg, msg);
    }

    protected void broadcost(Message forFirstPlayer, Message forSecondPlayer) {
        if (!firstPlayer.isSurrender()) {
            messagesForSend.add(new Letter(firstPlayer.getUserId(), forFirstPlayer));
        }
        if (!secondPlayer.isSurrender()) {
            messagesForSend.add(new Letter(secondPlayer.getUserId(), forSecondPlayer));
        }
    }


    @Override
    public Boolean isFinished() {
        return isFinished;
    }


    protected void finish() {
        isFinished = true;
        broadcost();
    }


    @Override
    public Boolean hasPlayer(Long userId) {
        final Boolean isFirst = firstPlayer.getUserId().equals(userId) && !firstPlayer.isSurrender();
        final Boolean isSecond = secondPlayer.getUserId().equals(userId) && !secondPlayer.isSurrender();
        return isFirst || isSecond;
    }


    @Override
    public Queue<Letter> getMessagesForSend() {
        final Queue<Letter> letters = messagesForSend;
        messagesForSend = new LinkedList<>();
        return letters;
    }


    @Override
    public List<Player> getPlayers() {
        final List<Player> players = new ArrayList<>();
        players.add(firstPlayer);
        players.add(secondPlayer);
        return players;
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
