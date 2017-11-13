package application.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(value = Parameterized.class)
public class EmailValidatorTest {
    @Parameter(value = 0)
    public String email;

    @Parameter(value = 1)
    public Boolean isValidData;


    @Test
    public void testEmailValidator() {
        final User user = new User( null, null, email);
        if (isValidData) {
            assertFalse("Не прошла валидная почта " + email, user.emailValidator().isPresent());
        } else {
            assertTrue("Не выдало ошибки на " + email, user.emailValidator().isPresent());
        }
    }


    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"user@park.mail.ru", true},
                {"mr.super-user@park.mail.ru", true},
                {"UpperCase@Mail.Ru", false},
                {"email", false},
                {"@mail.ru", false},
                {"mail.ru", false},
                {"%^&*&^@&mail.ru", false},
                {"%^&*&^@&$%^.ru", false},
                {"user@ru", false},
                {"", false}
        });
    }
}
