package application.game;

import application.game.base.ClientSnap;
import application.game.base.Player;
import application.models.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GameService {
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameService.class);
    private List<Game> games = new ArrayList<>();
    private Queue<Player> playersQueue = new LinkedList<>();

    public static final Long FRAME_TIME = 20L;

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


    public GameService() {
        new Thread(new MechanicsExucuter()).start();
    }


    private void mainCycle() {
        while (true) {
            try {
                final Long before = new Date().getTime();

                startNewGames();

                Iterator<Game> gameIter = games.iterator();
                while (gameIter.hasNext()) {
                    Game game = gameIter.next();
                    try {
                        if (!game.isFinished()) {
                            game.gmStep();
                        } else {
                            gameIter.remove();
                        }
                    } catch (RuntimeException e) {
                        LOGGER.error("The game emergincy stoped", e);
                        game.emergencyStop();
                        games.remove(game);
                    }
                }

                final Long after = new Date().getTime();

                try {
                    final Long sleepingTime = Math.max(0, FRAME_TIME - (after - before));
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    LOGGER.error("Mechanics thread was interrupted", e);
                }
            }
            catch (RuntimeException e) {
                LOGGER.error("Mechanics executor was reseted due to exception", e);
                games.clear();
                playersQueue.clear();
            }
        }
    }


    private void startNewGames() {
        while (playersQueue.size() >= 2) {
            final Player firstPlayer = playersQueue.remove();
            final Player secondPlayer = playersQueue.remove();
            final Game game = new Game(firstPlayer, secondPlayer);
            games.add(game);
        }
    }


    public void addUser(User user) {
        playersQueue.add(new Player(user));
    }


    public void addClientSnapshot(Long userId, ClientSnap snap) {
        final Optional<Game> game = games.stream().filter(gm -> gm.hasPlayer(userId)).findFirst();
        game.ifPresent(gm -> gm.addClientSnapshot(snap));
    }
}
