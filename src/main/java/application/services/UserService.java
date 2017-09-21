package application.services;

import application.models.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static HashMap<Long, User> users = new HashMap<>();

    public String create(User user) {
        final String error = user.loginValidator() + user.emailValidator() + user.passwordValidator();
        if (!error.isEmpty()) {
            return error;
        }
        for(User u : users.values()) {
            if (u.getLogin().equals(user.getLogin())) {
                return "There is a user with the same login";
            }
        }
        users.put(user.getId(), user);
        return "";
    }


    public String update(User user) {
        final StringBuilder errorBuilder = new StringBuilder();
        if (user.getEmail() != null) {
            errorBuilder.append(user.emailValidator());
        }
        if (user.getPassword() != null) {
            errorBuilder.append(user.passwordValidator());
        }
        final String error = errorBuilder.toString();
        if (!error.isEmpty()) {
            return error;
        }

        final User userForUpdate = getUserById(user.getId());
        if (userForUpdate == null) {
            return "User not found.";
        }
        if (user.getEmail() != null) {
            userForUpdate.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userForUpdate.setPassword(user.getPassword());
        }
        return "";
    }


    public @Nullable User getUserById(Long id) {
        return users.getOrDefault(id, null);
    }


    public @Nullable User getUserByLogin(String login) {
        for(User user : users.values()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }

    public static String emailValidator(String email) {
        final String ePattern = "^[.a-z0-9_-]+@[a-z0-9_-]+\\.[a-z]{2,6}$";
        return !Pattern.compile(ePattern).matcher(email).matches() ? "Not valid email. " : "";
    }

    public static String loginValidator(String login) {
        return !Pattern.compile("^[\\w\\d]{3,10}$").matcher(login).matches() ?  "Not valid login. " : "";
    }

    public static String passwordValidator(String password) {
        return !Pattern.compile("^\\S{3,16}$").matcher(password).matches() ? "Not valid password. " : "";
    }
}
