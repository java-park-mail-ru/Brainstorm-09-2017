package application.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    private void notValidEmailTest(String email) {
        final User validUser = new User(null, null, null, email);
        assertTrue("Не выдало ошибки на " + email, validUser.emailValidator().isPresent());
    }

    @Test
    public void testEmailValidator() {
        final User validUser = new User(null, null, null, "mr.super-user@park.mail.ru");
        assertFalse("Не прошла валидная почта mr.super-user@park.mail.ru", validUser.emailValidator().isPresent());

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
        assertTrue("Не выдало ошибки на " + login, validUser.loginValidator().isPresent());
    }

    @Test
    public void testLoginValidator() {
        final User validUser = new User(null, "Login123", null, null);
        assertFalse("Не прошёл валидный логин Login123", validUser.loginValidator().isPresent());

        notValidLoginTest("Log%^&.in123");
        notValidLoginTest("Lo");
        notValidLoginTest("VeryLoooooooongString");
        notValidLoginTest("");
    }


    private void notValidPwdTest(String pwd) {
        final User validUser = new User(null, null, pwd, null);
        assertTrue("Не выдало ошибки на " + pwd, validUser.passwordValidator().isPresent());
    }

    @Test
    public void testPasswordValidator() {
        final User validUser = new User(null, null, "Password123_@#$", null);
        assertFalse("Не прошёл валидный пароль Password123_@#$", validUser.passwordValidator().isPresent());

        notValidPwdTest("Pass word 123");
        notValidPwdTest("Pa");
        notValidPwdTest("VeryLoooooooooooooooooongString");
        notValidPwdTest("");
    }
}
