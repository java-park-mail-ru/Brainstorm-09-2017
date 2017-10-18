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


    private void notValidLoginTest(String login) {
        final User validUser = new User( login, null, null);
        assertTrue("Не выдало ошибки на " + login, validUser.loginValidator().isPresent());
    }

    @Test
    public void testLoginValidator() {
        final User validUser = new User( "Login123", null, null);
        assertFalse("Не прошёл валидный логин Login123", validUser.loginValidator().isPresent());

        notValidLoginTest("Log%^&.in123");
        notValidLoginTest("Lo");
        notValidLoginTest("VeryLoooooooongString");
        notValidLoginTest("");
    }


    private void notValidPwdTest(String pwd) {
        final User validUser = new User( null, pwd, null);
        assertTrue("Не выдало ошибки на " + pwd, validUser.passwordValidator().isPresent());
    }

    @Test
    public void testPasswordValidator() {
        final User validUser = new User( null, "Password123_@#$", null);
        assertFalse("Не прошёл валидный пароль Password123_@#$", validUser.passwordValidator().isPresent());

        notValidPwdTest("Pass word 123");
        notValidPwdTest("Pa");
        notValidPwdTest("VeryLoooooooooooooooooongString");
        notValidPwdTest("");
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
