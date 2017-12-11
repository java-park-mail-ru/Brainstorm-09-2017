package application.game.messages;

import application.game.base.Bubble;
import application.game.base.Coords;
import application.game.base.Player;
import application.models.User;
import application.websocket.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerSnapTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SERVER_SNAP_STRING = '{'
            + "\"class\":\"ServerSnap\","
            + "\"currentPlayer\":{"
            + "\"userProfile\":{"
            + "\"id\":42,"
            + "\"login\":\"testlogin\","
            + "\"email\":\"test@mail.ru\","
            + "\"numberOfGames\":14,"
            + "\"record\":7,"
            + "\"localRecord\":7,"
            + "\"theme\":10,"
            + "\"created\":\"1970-01-01 00:00:00.042Z\","
            + "\"updated\":\"1970-01-01 00:00:00.042Z\"},"
            + "\"score\":0,"
            + "\"isSurrender\":false},"
            + "\"enemy\":{"
            + "\"userProfile\":{\"id\":43,"
            + "\"login\":\"testlogin2\","
            + "\"email\":\"test2@mail.ru\","
            + "\"numberOfGames\":12,"
            + "\"record\":6,"
            + "\"localRecord\":6,"
            + "\"theme\":5,"
            + "\"created\":\"1970-01-01 00:00:00.042Z\","
            + "\"updated\":\"1970-01-01 00:00:00.042Z\"},"
            + "\"score\":0,"
            + "\"isSurrender\":false},"
            + "\"bubbles\":[{"
            + "\"id\":0,"
            + "\"coords\":{\"x\":1.0,\"y\":2.0,\"z\":3.0},"
            + "\"growthRate\":4.0,"
            + "\"radius\":5.0,"
            + "\"maxRadius\":6.0,"
            + "\"isBurst\":false}],"
            + "\"isFinished\":false}";


    @Test
    public void testDeserialization() throws IOException {
        final Message msg = objectMapper.readValue(SERVER_SNAP_STRING, Message.class);
        assertTrue(msg instanceof ServerSnap);
        final ServerSnap snap = (ServerSnap) msg;
    }

    @Test
    public void testSerialization() throws IOException {
        final ServerSnap snap = new ServerSnap(
                new Player(new User(
                        42L,
                        "testlogin",
                        "testpassword",
                        "test@mail.ru",
                        14L,
                        7L,
                        7L,
                        10,
                        new Timestamp(42L),
                        new Timestamp(42L))),
                new Player(new User(
                        43L,
                        "testlogin2",
                        "testpassword2",
                        "test2@mail.ru",
                        12L,
                        6L,
                        6L,
                        5,
                        new Timestamp(42L),
                        new Timestamp(42L))),
                Collections.singletonList(new Bubble(
                        0L,
                        new Coords(1f, 2f, 3f),
                        4f,
                        5f,
                        6f)),
                false);
        assertEquals(SERVER_SNAP_STRING, objectMapper.writeValueAsString(snap));
    }
}
