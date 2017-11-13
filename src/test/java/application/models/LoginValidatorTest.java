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
public class LoginValidatorTest {
    @Parameter(value = 0)
    public String login;

    @Parameter(value = 1)
    public Boolean isValidData;


    @Test
    public void testLoginValidator() {
        final User user = new User( login, null, null);
        if (isValidData) {
            assertFalse("Не прошла валидный логин " + login, user.loginValidator().isPresent());
        } else {
            assertTrue("Не выдало ошибки на логин " + login, user.loginValidator().isPresent());
        }
    }


    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"login", true},
                {"Login123", true},
                {"Log%^&.in123", false},
                {"Lo", false},
                {"VeryLoooooooongString", false},
                {"", false}
        });
    }
}
