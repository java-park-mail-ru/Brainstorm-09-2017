package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BurstingBubblesTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String MSG_STRING = '{'
            + "\"class\":\"BurstingBubbles\","
            + "\"currentPlayerScore\":0,"
            + "\"enemyScore\":0,"
            + "\"burstingBubbleIds\":[{"
            + "\"userId\":0,"
            + "\"burstingBubbleId\":0}]}";


    @Test
    public void testDeserialization() throws IOException {
        final Message msg = objectMapper.readValue(MSG_STRING, Message.class);
        assertTrue(msg instanceof BurstingBubbles);
        assertTrue(((BurstingBubbles) msg).getBurstingBubbles().contains(
                new BurstingBubbles.BurstingBubbleId(0L,0L)
        ));
    }


    @Test
    public void testSerialization() throws IOException {
        final BurstingBubbles msg = new BurstingBubbles(
                0L,
                0L,
                Collections.singletonList(new BurstingBubbles.BurstingBubbleId(0L, 0L))
        );
        assertEquals(MSG_STRING, objectMapper.writeValueAsString(msg));
    }
}
