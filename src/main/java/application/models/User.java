package application.models;

import application.services.UserService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private Integer id;
    private String login;
    private String password;
    private String email;

    @JsonCreator
    public User(@JsonProperty("login") String login, @JsonProperty("password") String password,
                @JsonProperty("email") String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public @Nullable String create() {
        final String error = validate();
        if (error != null) {
            return error;
        }
        if (UserService.addUser(this) == UserService.Status.ERROR_DUPLICATE) {
            return "There is a user with the same login";
        }
        return null;
    }

    public Integer getId()      { return id; }
    public String getLogin()    { return login;  }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }

    public void setId(Integer id) { this.id = id; } // TODO: удалить


    public static Boolean emailValidator(String email) {
        final String ePattern = "^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$";
        return Pattern.compile(ePattern).matcher(email).matches();
    }

    public static Boolean loginValidator(String login) {
        return Pattern.compile("^[\\w\\d]{3,10}$").matcher(login).matches();
    }

    public static Boolean passwordValidator(String password) {
        return Pattern.compile("^\\S{3,16}$").matcher(password).matches();
    }

    public @Nullable String validate() {
        final StringBuilder errorBuilder = new StringBuilder();
        if (!loginValidator(login)) {
            errorBuilder.append("Not valid login. ");
        }
        if (!emailValidator(email)) {
            errorBuilder.append("Not valid email. ");
        }
        if (!passwordValidator(password)) {
            errorBuilder.append("Not valid password. ");
        }
        final String error = errorBuilder.toString();
        return error.isEmpty() ? null : error;
    }
}
