package application.models;

import application.views.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    private void notValidEmailTest(String email) {
        final User validUser = new User(null, null, null, email);
        final ErrorResponse err = validUser.emailValidator();
        assertNotNull("Не выдало ошибки на " + email, err);
    }

    @Test
    public void testEmailValidator() {
        final User validUser = new User(null, null, null, "mr.super-user@park.mail.ru");
        final ErrorResponse err = validUser.emailValidator();
        assertNull("Не прошла валидная почта mr.super-user@park.mail.ru", err);

        notValidEmailTest("UpperCase@Mail.Ru");
        notValidEmailTest("@mail.ru");
        notValidEmailTest("mail.ru");
        notValidEmailTest("%^&*&^@&mail.ru");
        notValidEmailTest("%^&*&^@&$%^.ru");
        notValidEmailTest("user@ru");
        notValidEmailTest("");
    }


    private void notValidLoginTest(String login) {
        final User validUser = new User(null, login, null, null);
        final ErrorResponse err = validUser.loginValidator();
        assertNotNull("Не выдало ошибки на " + login, err);
    }

    @Test
    public void testLoginValidator() {
        final User validUser = new User(null, "Login123", null, null);
        final ErrorResponse err = validUser.loginValidator();
        assertNull("Не прошёл валидный логин Login123", err);

        notValidLoginTest("Log%^&.in123");
        notValidLoginTest("Lo");
        notValidLoginTest("VeryLoooooooongString");
        notValidLoginTest("");
    }


    private void notValidPwdTest(String pwd) {
        final User validUser = new User(null, null, pwd, null);
        final ErrorResponse err = validUser.passwordValidator();
        assertNotNull("Не выдало ошибки на " + pwd, err);
    }

    @Test
    public void testPasswordValidator() {
        final User validUser = new User(null, null, "Password123_@#$", null);
        final ErrorResponse err = validUser.passwordValidator();
        assertNull("Не прошёл валидный пароль Password123_@#$", err);

        notValidPwdTest("Pass word 123");
        notValidPwdTest("Pa");
        notValidPwdTest("VeryLoooooooooooooooooongString");
        notValidPwdTest("");
    }
}
