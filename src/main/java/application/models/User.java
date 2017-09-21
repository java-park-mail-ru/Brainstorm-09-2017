package application.models;

import application.services.UserService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    public String create() {
        final String error = loginValidator(login) + emailValidator(email) + passwordValidator(password);
        if (!error.isEmpty()) {
            return error;
        }
        if (UserService.addUser(this) == UserService.Status.ERROR_DUPLICATE) {
            return "There is a user with the same login";
        }
        return "";
    }


    public String update(User user) {
        final StringBuilder errorBuilder = new StringBuilder();
        if (user.email != null) {
            errorBuilder.append(user.email);
        }
        if (user.password != null) {
            errorBuilder.append(user.password);
        }

        final String error = errorBuilder.toString();
        if (error.isEmpty()) {
            email = user.email;
            password = user.password;
        }
        return error;
    }


    public Integer getId()      { return id; }
    public String getLogin()    { return login;  }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }

    public void setId(Integer id)            { this.id = id; } // TODO: удалить
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }


    public static String emailValidator(String email) {
        final String ePattern = "^[a-z0-9_-]+\\.@[a-z0-9_-]+\\.[a-z]{2,6}$";
        return !Pattern.compile(ePattern).matcher(email).matches() ? "Not valid email. " : "";
    }

    public static String loginValidator(String login) {
        return !Pattern.compile("^[\\w\\d]{3,10}$").matcher(login).matches() ?  "Not valid login. " : "";
    }

    public static String passwordValidator(String password) {
        return !Pattern.compile("^\\S{3,16}$").matcher(password).matches() ? "Not valid password. " : "";
    }
}
