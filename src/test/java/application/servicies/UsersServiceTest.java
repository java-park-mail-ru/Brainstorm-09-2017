package application.servicies;

import application.models.User;
import application.views.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UsersServiceTest {
    @Autowired
    private UsersService usersService;
    private User credentials;

    private static final AtomicLong ID_GENERATOR = new AtomicLong();


    @Before
    public void setup(){
        credentials = new User( "login" + ID_GENERATOR.getAndIncrement(), "password", "user@mail.ru");
    }


    private User create(User user) {
        final List<ErrorResponse> errors = usersService.create(user);
        assertTrue(errors.toString(), errors.isEmpty());
        return userExistingCheck(usersService, user);
    }


    @Test
    public void testCreate() {
        create(credentials);

        List<ErrorResponse> errors = usersService.create(credentials);
        assertFalse("Не выдало ошибки об существовании такого же пользователя", errors.isEmpty());

        // Тесты на валидацию
        User notValidUser = new User( "log *-in" + ID_GENERATOR.getAndIncrement(), "password", "user@mail.ru");
        errors = usersService.create(notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность логина", errors.isEmpty());

        notValidUser = new User( "login" + ID_GENERATOR.getAndIncrement(), "pass word", "user@mail.ru");
        errors = usersService.create(notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность пароля", errors.isEmpty());

        notValidUser = new User( "login" + ID_GENERATOR.getAndIncrement(), "password", "usermail.ru");
        errors = usersService.create(notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность почты", errors.isEmpty());
    }


    private User update(Long id, User newCredentials) {
        final List<ErrorResponse> errors = usersService.update(id, newCredentials);
        assertTrue(errors.toString(), errors.isEmpty());
        final User updatedUser = usersService.findUserById(id);
        assertNotNull("Пользователь был удалён", updatedUser);
        return updatedUser;
    }


    @Test
    public void testUpdatePwdAndEmail() {
        final User createdUser = create(credentials);
        final User newCredentials = new User( null, "newPassword", "newuser@mail.ru");
        final User updatedUser = update(createdUser.getId(), newCredentials);
        assertTrue("Пароль не изменился при обновлении пароля и почты", usersService.checkpw(newCredentials.getPassword(), updatedUser.getPassword()));
        assertEquals("Почта не изменилась при обновлении пароля и почты", updatedUser.getEmail(), newCredentials.getEmail());

        // Тесты на валидацию
        final User notValidUser = new User( null, "pass word", "Usermail.ru");
        final List<ErrorResponse> errors = usersService.update(credentials.getId(), notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность", errors.isEmpty());
    }


    @Test
    public void testUpdatePwd() {
        final User createdUser = create(credentials);
        final User newCredentials = new User( null, "newPassword", null);
        final User updatedUser = update(createdUser.getId(), newCredentials);
        assertTrue("Пароль не изменился при обновлении пароля", usersService.checkpw(newCredentials.getPassword(), updatedUser.getPassword()));

        // Тесты на валидацию
        final User notValidUser = new User( null, "pass word", null);
        final List<ErrorResponse> errors = usersService.update(credentials.getId(), notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность", errors.isEmpty());
    }


    @Test
    public void testUpdateEmail() {
        final User createdUser = create(credentials);
        final User newCredentials = new User( null, null, "newuser@mail.ru");
        final User updatedUser = update(createdUser.getId(), newCredentials);
        assertEquals("Почта не изменилась при обновлении почты", updatedUser.getEmail(), newCredentials.getEmail());

        // Тесты на валидацию
        final User notValidUser = new User( null, "password", "newuser@mailru");
        final List<ErrorResponse> errors = usersService.update(credentials.getId(), notValidUser);
        assertFalse("Не выдало ошибки при проверки на валидность", errors.isEmpty());
    }


    public static User userExistingCheck(UsersService usersService, User user) {
        final User createdUser = usersService.findUserByLogin(user.getLogin());
        assertNotNull("Пользователь не был создан", createdUser);
        assertEquals("Логин не совпал", createdUser.getLogin(), user.getLogin());
        assertEquals("Пароль не совпал", createdUser.getPassword(), user.getPassword());
        assertEquals("Почта не совпала", createdUser.getEmail(), user.getEmail());
        return createdUser;
    }
}