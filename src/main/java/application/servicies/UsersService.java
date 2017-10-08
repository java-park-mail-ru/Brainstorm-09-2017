package application.servicies;

import application.models.User;
import application.views.ErrorResponse;
import application.views.ErrorResponse.ErrorCode;
import application.views.RecordResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    private HashMap<Long, User> users = new HashMap<>();


    public List<ErrorResponse> create(User credentials) {
        final List<ErrorCode> errors = new ArrayList<>();
        credentials.emailValidator().ifPresent(errors::add);
        credentials.loginValidator().ifPresent(errors::add);
        credentials.passwordValidator().ifPresent(errors::add);

        if (!errors.isEmpty()) {
            return new ArrayList<>(errors.stream().map(ErrorResponse::new).collect(Collectors.toList()));
        }
        for(User u : users.values()) {
            if (u.getLogin().equals(credentials.getLogin())) {
                return new ErrorResponse(ErrorCode.USER_DUPLICATE).toList();
            }
        }

        credentials.setPassword(hashpw(credentials.getPassword()));
        users.put(credentials.getId(), credentials);
        return new ArrayList<>();          // Возвращаяю пустой список ошибок
    }


    public List<ErrorResponse> update(Long id, User credentials) {
        final List<ErrorCode> errors = new ArrayList<>();
        if (credentials.getEmail() != null) {
            credentials.emailValidator().ifPresent(errors::add);
        }
        if (credentials.getPassword() != null) {
            credentials.passwordValidator().ifPresent(errors::add);
        }
        if (!errors.isEmpty()) {
            return new ArrayList<>(errors.stream().map(ErrorResponse::new).collect(Collectors.toList()));
        }

        final User userForUpdate = getUserById(id);
        if (userForUpdate == null) {
            return new ErrorResponse(ErrorCode.USER_NOT_FOUND).toList();
        }

        if (credentials.getEmail() != null) {
            userForUpdate.setEmail(credentials.getEmail());
            userForUpdate.setUpdatedDate();
        }
        if (credentials.getPassword() != null) {
            userForUpdate.setPassword(hashpw(credentials.getPassword()));
            userForUpdate.setUpdatedDate();
        }
        return new ArrayList<>();          // Возвращаяю пустой список ошибок
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


    public List<RecordResponse> getRecords() {
        final List<User> sortedUsers = new ArrayList<>(users.values());
        sortedUsers.sort((user1, user2) -> user2.getRecord().compareTo(user1.getRecord()));
        final List<RecordResponse> records = new ArrayList<>();
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
