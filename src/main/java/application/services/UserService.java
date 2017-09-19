package application.services;

import application.models.User;

import java.util.ArrayList;

public class UserService {
    private static ArrayList<User> users = new ArrayList<>();

    public enum Status { GOOD, ERROR_DUPLICATE }

    public static Status addUser(User user) {
        for(User u : users) {
            if (u.getLogin().equals(user.getLogin())) {
                return Status.ERROR_DUPLICATE;
            }
        }
        users.add(user);
        return Status.GOOD;
    }

    public static User getUserById(Integer id) {
        return users.get(id);
    }
}
