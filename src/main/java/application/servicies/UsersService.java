package application.servicies;

import application.models.User;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import application.views.ErrorResponseList;
import application.views.RecordResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UsersService {
    private HashMap<Long, User> users = new HashMap<>();

    public ErrorResponseList create(User credentials) {
        final ErrorResponseList errors = new ErrorResponseList();
        errors
                .add(credentials.emailValidator())
                .add(credentials.loginValidator())
                .add(credentials.passwordValidator());

        if (!errors.isEmpty()) {
            return errors;
        }
        for(User u : users.values()) {
            if (u.getLogin().equals(credentials.getLogin())) {
                errors.add(new ErrorResponse(ErrorCode.USER_DUPLICATE));
                return errors;
            }
        }

        credentials.setPassword(hashpw(credentials.getPassword()));
        users.put(credentials.getId(), credentials);
        return errors;
    }


    public ErrorResponseList update(Long id, User credentials) {
        final ErrorResponseList errors = new ErrorResponseList();
        if (credentials.getEmail() != null) {
            errors.add(credentials.emailValidator());
        }
        if (credentials.getPassword() != null) {
            errors.add(credentials.passwordValidator());
        }
        if (!errors.isEmpty()) {
            return errors;
        }

        final User userForUpdate = getUserById(id);
        if (userForUpdate == null) {
            errors.add(new ErrorResponse(ErrorCode.USER_NOT_FOUND));
            return errors;
        }

        if (credentials.getEmail() != null) {
            userForUpdate.setEmail(credentials.getEmail());
            userForUpdate.setUpdatedDate();
        }
        if (credentials.getPassword() != null) {
            userForUpdate.setPassword(hashpw(credentials.getPassword()));
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
        if (user == null || !checkpw(credentials.getPassword(), user.getPassword())) {
            return null;
        }
        return user;
    }


    public ArrayList<RecordResponse> getRecords() {
        final ArrayList<User> sortedUsers = new ArrayList<>(users.values());
        sortedUsers.sort((user1, user2) -> user2.getRecord().compareTo(user1.getRecord()));
        final ArrayList<RecordResponse> records = new ArrayList<>();
        for(User user : sortedUsers) {
            if (user.getRecord() > 0) {
                records.add(new RecordResponse(user));
            }
        }
        return records;
    }


    private String hashpw(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt());
    }


    public Boolean checkpw(String pwd, String storedHash) {
        return BCrypt.checkpw(pwd, storedHash);
    }
}
