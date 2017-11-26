package application.game;

import application.game.base.Bubble;
import application.game.base.Player;
import application.game.messages.ClientSnap;
import application.game.messages.ServerSnap;
import application.models.User;
import application.servicies.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class GameTest {
    private Player firstPlayer;
    private Player secondPlayer;

    @MockBean
    private UsersService usersService;

    private Game game;


    @Before
    public void setup() {
        firstPlayer = new Player(new User(1L, "FirstUser"));
        secondPlayer = new Player(new User(2L, "SecondUser"));
    }


    @Test
    public void testNewGame() {
        game = new GameImpl(firstPlayer, secondPlayer);
        assertTrue(game.hasPlayer(firstPlayer.getUserId()));
        assertTrue(game.hasPlayer(secondPlayer.getUserId()));
        assertFalse(game.isFinished());

        final ServerSnap snapForFirst = game.getSnapshot(firstPlayer.getUserId());
        assertEquals(snapForFirst.getCurrentPlayer(), firstPlayer);
        assertEquals(snapForFirst.getEnemy(), secondPlayer);
        assertFalse(snapForFirst.getFinished());
        final ServerSnap snapForSecond = game.getSnapshot(secondPlayer.getUserId());
        assertEquals(snapForSecond.getCurrentPlayer(), secondPlayer);
        assertEquals(snapForSecond.getEnemy(), firstPlayer);
        assertFalse(snapForSecond.getFinished());
    }


    @Test
    public void testNewBubbles() {
        testNewGame();
        game.gmStep();
        final ServerSnap snap = game.getSnapshot(firstPlayer.getUserId());
        final Collection<Bubble> bubbles = snap.getBubbles();
        assertFalse(bubbles.isEmpty());
    }
}
