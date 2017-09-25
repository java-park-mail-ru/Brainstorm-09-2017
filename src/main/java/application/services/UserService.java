package application.services;

import application.models.User;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static HashMap<Long, User> users = new HashMap<>();

    public ArrayList<ErrorResponse> create(User user) {
        final ArrayList<ErrorResponse> errors = new ArrayList<>();
        ErrorResponse error = user.emailValidator();
        if (error != null) errors.add(error);
        error = user.loginValidator();
        if (error != null) errors.add(error);
        error = user.passwordValidator();
        if (error != null) errors.add(error);

        if (!errors.isEmpty()) {
            return errors;
        }
        for(User u : users.values()) {
            if (u.getLogin().equals(user.getLogin())) {
                errors.add(new ErrorResponse(ErrorCode.USER_DUPLICATE));
                return errors;
            }
        }
        users.put(user.getId(), user);
        return errors;
    }


    public ArrayList<ErrorResponse> update(User user) {
        final ArrayList<ErrorResponse> errors = new ArrayList<>();
        ErrorResponse error = user.emailValidator();
        if (error != null) errors.add(error);
        error = user.passwordValidator();
        if (error != null) errors.add(error);
        if (!errors.isEmpty()) {
            return errors;
        }

        final User userForUpdate = getUserById(user.getId());
        if (userForUpdate == null) {
            errors.add(new ErrorResponse(ErrorCode.USER_NOT_FOUND));
            return errors;
        }

        if (user.getEmail() != null) {
            userForUpdate.setEmail(user.getEmail());
        }
        if (user.getPassword() != null) {
            userForUpdate.setPassword(user.getPassword());
        }
        return errors;
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
}
