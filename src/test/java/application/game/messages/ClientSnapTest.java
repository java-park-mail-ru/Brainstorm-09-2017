package application.game.messages;

import application.websocket.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClientSnapTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SNAP_STRING = '{'
            + "\"class\":\"ClientSnap\","
            + "\"burstingBubbleId\":42}";


    @Test
    public void testDeserialization() throws IOException {
        final Message msg = objectMapper.readValue(SNAP_STRING, Message.class);
        assertTrue(msg instanceof ClientSnap);
        assertEquals(((ClientSnap) msg).getBurstingBubbleId(), 42);
    }


    @Test
    public void testClientSnapSerialization() throws IOException {
        final ClientSnap snap = new ClientSnap(42L);
        assertEquals(SNAP_STRING, objectMapper.writeValueAsString(snap));
    }
}
