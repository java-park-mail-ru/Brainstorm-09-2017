package application.services;

import application.models.User;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import java.util.HashMap;

@Service
public class UserService {
    private static HashMap<Long, User> users = new HashMap<>();

    public enum Status { GOOD, ERROR_DUPLICATE }


    public static Status addUser(User user) {
        for(User u : users.values()) {
            if (u.getLogin().equals(user.getLogin())) {
                return Status.ERROR_DUPLICATE;
            }
        }
        users.put(user.getId(), user);
        return Status.GOOD;
    }


    public static @Nullable User getUserById(Integer id) {
        try {
            return users.get(id);
        } catch (RuntimeException e) {
            return null;
        }
    }


    public static @Nullable User getUserByLogin(String login) {
        for(User user : users.values()) {
            if (user.getLogin().equals(login)) {
                return user;
            }
        }
        return null;
    }
}
