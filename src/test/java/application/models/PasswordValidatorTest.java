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
public class PasswordValidatorTest {
    @Parameter(value = 0)
    public String pwd;

    @Parameter(value = 1)
    public Boolean isValidData;


    @Test
    public void testPasswordValidator() {
        final User user = new User( null, pwd, null);
        if (isValidData) {
            assertFalse("Не прошла валидный пароль " + pwd, user.passwordValidator().isPresent());
        } else {
            assertTrue("Не выдало ошибки на пароль " + pwd, user.passwordValidator().isPresent());
        }
    }


    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"password", true},
                {"Password123_@#$", true},
                {"Pass word 123", false},
                {"Pa", false},
                {"VeryLoooooooooooooooooongString", false},
                {"", false}
        });
    }
}
