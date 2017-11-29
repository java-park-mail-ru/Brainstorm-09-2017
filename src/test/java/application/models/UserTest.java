package application.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class UserTest {
    private User credentials;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_STRING = '{'
            + "\"id\":42,"
            + "\"login\":\"login\","
            + "\"email\":\"user@mail.ru\","
            + "\"numberOfGames\":null,"
            + "\"record\":null,"
            + "\"localRecord\":null,"
            + "\"theme\":null,"
            + "\"created\":null,"
            + "\"updated\":null}";

    @Before
    public void setup(){
        credentials = new User(42L, "login", "password", "user@mail.ru");
    }


    @Test
    public void testDeserialization() throws IOException {
        final User user = objectMapper.readValue(USER_STRING, User.class);
        assertNotNull(user);
        assertEquals(credentials.getId(), user.getId());
        assertEquals(credentials.getEmail(), user.getEmail());
        assertEquals(credentials.getLogin(), user.getLogin());
    }


    @Test
    public void testSerialization() throws IOException {
        assertEquals(USER_STRING, objectMapper.writeValueAsString(credentials));
    }
}
