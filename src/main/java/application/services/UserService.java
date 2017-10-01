package application.services;

import application.models.User;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import application.views.ErrorResponseList;
import application.views.RecordResponse;
import application.views.RecordResponse.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserService {
    private static HashMap<Long, User> users = new HashMap<>();

    public ErrorResponseList create(User credentials) {
        final ErrorResponseList errors = new ErrorResponseList();
        errors.add(credentials.emailValidator()).add(credentials.loginValidator()).add(credentials.passwordValidator());

        if (!errors.isEmpty()) {
            return errors;
        }
        for(User u : users.values()) {
            if (u.getLogin().equals(credentials.getLogin())) {
                errors.add(new ErrorResponse(ErrorCode.USER_DUPLICATE));
                return errors;
            }
        }

        credentials.setPassword(BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt()));
        users.put(credentials.getId(), credentials);
        return errors;
    }


    public ErrorResponseList update(User credentials) {
        final ErrorResponseList errors = new ErrorResponseList();
        errors.add(credentials.emailValidator()).add(credentials.passwordValidator());
        if (!errors.isEmpty()) {
            return errors;
        }

        final User userForUpdate = getUserById(credentials.getId());
        if (userForUpdate == null) {
            errors.add(new ErrorResponse(ErrorCode.USER_NOT_FOUND));
            return errors;
        }

        if (credentials.getEmail() != null) {
            userForUpdate.setEmail(credentials.getEmail());
            userForUpdate.setUpdatedDate();
        }
        if (credentials.getPassword() != null) {
            userForUpdate.setPassword(credentials.getPassword());
            userForUpdate.setUpdatedDate();
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


    public @Nullable User auth(User credentials) {
        final User user = getUserByLogin(credentials.getLogin());
        if (user == null || !BCrypt.checkpw(credentials.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }


    public ArrayList<RecordResponse> getRecords() {
        ArrayList<RecordResponse> records = new ArrayList<>();
        for(User user : users.values()) {
            if (user.getRecord() > 0) {
                records.add(new RecordResponse(user));
            }
        }
        records.sort(new RecordResponse.Compare());
        return records;
    }
}
