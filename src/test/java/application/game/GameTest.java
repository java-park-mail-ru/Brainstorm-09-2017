package application.game;

import application.game.base.Player;
import application.game.messages.ServerSnap;
import application.models.User;
import application.servicies.UsersService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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


    @Before
    public void setup() {
        firstPlayer = new Player(new User(1L, "FirstUser"));
        secondPlayer = new Player(new User(2L, "SecondUser"));
    }


    @Test
    public void newGameTest() {
        final Game game = new GameImpl(firstPlayer, secondPlayer);
        assertTrue(game.hasPlayer(firstPlayer.getUserId()));
        assertTrue(game.hasPlayer(secondPlayer.getUserId()));

        final ServerSnap snapForFirst = game.getSnapshot(firstPlayer.getUserId());
        assertEquals(snapForFirst.getCurrentPlayer(), firstPlayer);
        assertEquals(snapForFirst.getEnemy(), secondPlayer);
        assertFalse(snapForFirst.getFinished());
        final ServerSnap snapForSecond = game.getSnapshot(secondPlayer.getUserId());
        assertEquals(snapForSecond.getCurrentPlayer(), secondPlayer);
        assertEquals(snapForSecond.getEnemy(), firstPlayer);
        assertFalse(snapForSecond.getFinished());
    }
}
