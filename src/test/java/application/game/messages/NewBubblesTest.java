package application.game.messages;

import application.game.base.Bubble;
import application.game.base.Coords;
import application.websocket.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class NewBubblesTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MSG_STRING = '{'
            + "\"class\":\"NewBubbles\","
            + "\"bubbles\":[{"
            + "\"id\":0,"
            + "\"coords\":{\"x\":1.0,\"y\":2.0,\"z\":3.0},"
            + "\"growthRate\":4.0,"
            + "\"radius\":5.0,"
            + "\"maxRadius\":6.0,"
            + "\"isBurst\":false}]}";


    @Test
    public void testDeserialization() throws IOException {
        final Message msg = objectMapper.readValue(MSG_STRING, Message.class);
        assertTrue(msg instanceof NewBubbles);
        assertTrue(((NewBubbles) msg).getBubbles().contains(new Bubble(
                0L,
                new Coords(1f, 2f, 3f),
                4f,
                5f,
                6f)));
    }


    @Test
    public void testSerialization() throws IOException {
        final NewBubbles snap = new NewBubbles(new Bubble(
                0L,
                new Coords(1f, 2f, 3f),
                4f,
                5f,
                6f));
        assertEquals(MSG_STRING, objectMapper.writeValueAsString(snap));
    }
}
