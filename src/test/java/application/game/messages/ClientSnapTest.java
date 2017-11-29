package application.game.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ClientSnapTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String SNAP_STRING = '{'
            + "\"class\":\"ClientSnap\","
            + "\"burstingBubbleId\":42}";


    @Test
    public void testDeserialization() throws IOException {
        final ClientSnap snap = objectMapper.readValue(SNAP_STRING, ClientSnap.class);
        assertEquals(snap.getBurstingBubbleId(), 42);
    }
}
