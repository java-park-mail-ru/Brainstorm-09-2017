package application.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class UserTest {
    private User credentials;


    @Before
    public void setup(){
        credentials = new User("login", "password", "user@mail.ru");
    }


    @Test
    public void testSerialization() throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final String jsonUser = objectMapper.writeValueAsString(credentials);
        final User user = objectMapper.readValue(jsonUser, User.class);
        assertNotNull(user);
        assertEquals(credentials.getId(), user.getId());
        assertEquals(credentials.getEmail(), user.getEmail());
        assertEquals(credentials.getLogin(), user.getLogin());
        assertNull("Пароль не должен выдаваться в json", user.getPassword());
    }
}
