package application.game;

import application.game.base.*;
import application.game.messages.ServerSnap;
import application.models.User;
import application.websocket.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class ServerSnapTest {
    @Test
    public void testSerialization() throws IOException {
        final User user = new User(1L, "login", "password", "email@mail.ru", null, null, null, null, null, null);
        final Player player = new Player(user);

        final Collection<Bubble> bubbles = new ArrayList<>();
        bubbles.add(new Bubble(new Coords(1f, 2f, 3f), 4f, 5f, 6f));
        bubbles.add(new Bubble(new Coords(1f, 2f, 3f), 4f, 5f, 6f));
        bubbles.add(new Bubble(new Coords(1f, 2f, 3f), 4f, 5f, 6f));

        final Message serverSnap = new ServerSnap(player, player, bubbles, false);

        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonServerSnap = objectMapper.writeValueAsString(serverSnap);
        // TODO
        System.out.println(jsonServerSnap);
    }
}
