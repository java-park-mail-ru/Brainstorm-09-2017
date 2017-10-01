package application.services;

import application.UserService;
import application.models.User;
import application.views.ErrorResponseList;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

public class UserServiceTest {
    private UserService userService;
    private User credentials;

    private static final AtomicLong ID_GENERATOR = new AtomicLong();


    @Before
    public void setup(){
        credentials = new User(null, "login" + ID_GENERATOR.getAndIncrement(), "password", "user@mail.ru");
        userService = new UserService();
    }


    @Test
    public void testCreate() {
        ErrorResponseList errors = userService.create(credentials);
        assertTrue(errors.toString(), errors.isEmpty());

        final User createdUser = userService.getUserByLogin(credentials.getLogin());
        assertNotNull(createdUser);
        assertEquals(createdUser.getLogin(), credentials.getLogin());
        assertEquals(createdUser.getPassword(), credentials.getPassword());
        assertEquals(createdUser.getEmail(), credentials.getEmail());

        errors = userService.create(credentials);
        assertFalse("Не выдало ошибки об существования такого же пользователя", errors.isEmpty());
    }
}
