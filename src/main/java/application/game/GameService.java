package application.game;

import application.game.base.Player;
import application.models.User;
import application.servicies.UsersService;
import application.websocket.Letter;
import application.websocket.Message;
import application.websocket.RemotePointService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.*;


@Service
@PropertySource("classpath:game.properties")
public class GameService {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private List<Game> games = new ArrayList<>();
    private Queue<Player> playersQueue = new LinkedList<>();

    @Autowired
    private RemotePointService remotePointService;

    private final UsersService usersService;

    private static final Long FRAME_TIME = 50L;

    class MechanicsExucuter implements Runnable {
        @Override
        public void run() {
            try {
                mainCycle();
            } finally {
                LOGGER.warn("Mechanic executor terminated");
            }
        }
    }


    @Autowired
    public GameService(UsersService usersService) {
        this.usersService = usersService;
        new Thread(new MechanicsExucuter()).start();
    }


    private void mainCycle() {
        while (true) {
            try {
                final Long before = new Date().getTime();

                startNewGames();

                final Iterator<Game> gameIter = games.iterator();
                while (gameIter.hasNext()) {
                    final Game game = gameIter.next();
                    try {
                        game.gmStep();

                        final Queue<Letter> messagesForSend = game.getMessagesForSend();
                        while (!messagesForSend.isEmpty()) {
                            final Letter letter = messagesForSend.remove();
                            try {
                                remotePointService.send(letter);
                            } catch (IOException ignored) {
                            }
                        }

                        if (game.isFinished()) {
                            finishGame(game);
                            gameIter.remove();
                        }
                    } catch (RuntimeException e) {
                        LOGGER.error("The game emergincy stoped", e);
                        try {
                            cutDownPlayersConnections(game);
                        } catch (RuntimeException ignored) {
                        }
                        games.remove(game);
                    }
                }

                final Long after = new Date().getTime();

                try {
                    final Long sleepingTime = FRAME_TIME - (after - before);
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    LOGGER.error("Mechanics thread was interrupted", e);
                }
            } catch (Exception e) {
                LOGGER.error("Mechanics executor was reseted due to exception", e);
                remotePointService.reset();
                games.clear();
                playersQueue.clear();
            }
        }
    }


    private void startNewGames() throws IOException {
        while (playersQueue.size() >= 2) {
            final Player firstPlayer = playersQueue.remove();
            final Player secondPlayer = playersQueue.remove();
            final Game game = new GameImpl(firstPlayer, secondPlayer);
            games.add(game);
        }
    }


    public void addUser(User user) {
        final Player newPlayer = new Player(user);
        if (!playersQueue.contains(newPlayer)) {
            final Optional<Game> gameWithPlayer = games.stream()
                    .filter(game -> game.hasPlayer(newPlayer.getUserId())).findFirst();
            gameWithPlayer.ifPresent(Game::broadcost);
            if (!gameWithPlayer.isPresent()) {
                playersQueue.add(newPlayer);
            }
        }
    }


    public void addClientMessage(Long userId, Message msg) {
        final Optional<Game> game = games.stream().filter(gm -> gm.hasPlayer(userId)).findFirst();
        game.ifPresent(gm -> gm.addClientMessage(new Letter(userId, msg)));
    }


    private void finishGame(Game game) {
        final List<Player> players = game.getPlayers();
        for (Player player : players) {
            usersService.record(player.getUserId(), player.getScore());
            remotePointService.cutDownConnection(player.getUserId(), CloseStatus.NORMAL);
        }
    }


    private void cutDownPlayersConnections(Game game) {
        final List<Player> players = game.getPlayers();
        for (Player player : players) {
            remotePointService.cutDownConnection(player.getUserId(), CloseStatus.SERVER_ERROR);
        }
    }
}
