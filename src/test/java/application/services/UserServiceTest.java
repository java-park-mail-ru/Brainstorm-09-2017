package application.services;

import application.servicies.UserService;
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


    private User create(User user) {
        final ErrorResponseList errors = userService.create(user);
        assertTrue(errors.toString(), errors.isEmpty());

        final User createdUser = userService.getUserByLogin(user.getLogin());
        assertNotNull("Пользователь не был создан", createdUser);
        assertEquals("Логин не совпал", createdUser.getLogin(), user.getLogin());
        assertEquals("Пароль не совпал", createdUser.getPassword(), user.getPassword());
        assertEquals("Почта не совпала", createdUser.getEmail(), user.getEmail());
        return createdUser;
    }


    @Test
    public void testCreate() {
        create(credentials);

        ErrorResponseList errors = userService.create(credentials);
        assertFalse("Не выдало ошибки об существовании такого же пользователя", errors.isEmpty());

        // Тесты на валидацию
        User notValidUser = new User(null, "log *-in" + ID_GENERATOR.getAndIncrement(), "password", "user@mail.ru");
        errors = userService.create(notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность логина", errors.isEmpty());

        notValidUser = new User(null, "login" + ID_GENERATOR.getAndIncrement(), "pass word", "user@mail.ru");
        errors = userService.create(notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность пароля", errors.isEmpty());

        notValidUser = new User(null, "login" + ID_GENERATOR.getAndIncrement(), "password", "usermail.ru");
        errors = userService.create(notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность почты", errors.isEmpty());
    }


    private User update(Long id, User newCredentials) {
        final ErrorResponseList errors = userService.update(id, newCredentials);
        assertTrue(errors.toString(), errors.isEmpty());
        final User updatedUser = userService.getUserById(id);
        assertNotNull("Пользователь был удалён", updatedUser);
        return updatedUser;
    }


    @Test
    public void testUpdatePwdAndEmail() {
        create(credentials);
        final User newCredentials = new User(null, null, "newPassword", "newuser@mail.ru");
        final User updatedUser = update(credentials.getId(), newCredentials);
        assertTrue("Пароль не изменился при обновлении пароля и почты", userService.checkpw(newCredentials.getPassword(), updatedUser.getPassword()));
        assertEquals("Почта не изменилась при обновлении пароля и почты", updatedUser.getEmail(), newCredentials.getEmail());

        // Тесты на валидацию
        User notValidUser = new User(null, null, "pass word", "Usermail.ru");
        ErrorResponseList errors = userService.update(credentials.getId(), notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность", errors.isEmpty());
    }


    @Test
    public void testUpdatePwd() {
        create(credentials);
        final User newCredentials = new User(null, null, "newPassword", null);
        final User updatedUser = update(credentials.getId(), newCredentials);
        assertTrue("Пароль не изменился при обновлении пароля", userService.checkpw(newCredentials.getPassword(), updatedUser.getPassword()));

        // Тесты на валидацию
        User notValidUser = new User(null, null, "pass word", null);
        ErrorResponseList errors = userService.update(credentials.getId(), notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность", errors.isEmpty());
    }


    @Test
    public void testUpdateEmail() {
        create(credentials);
        final User newCredentials = new User(null, null, null, "newuser@mail.ru");
        final User updatedUser = update(credentials.getId(), newCredentials);
        assertEquals("Почта не изменилась при обновлении почты", updatedUser.getEmail(), newCredentials.getEmail());

        // Тесты на валидацию
        User notValidUser = new User(null, null, "password", "newuser@mailru");
        ErrorResponseList errors = userService.update(credentials.getId(), notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность", errors.isEmpty());
    }
}
