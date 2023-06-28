package ru.practicum.shareit.user.dao;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {
    private static long id = 0;
    private static HashMap<String, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserByEmail(User user) {
        return users.get(user.getEmail());
    }

    @Override
    public User updateUser(User user) {
        User userInDB = new ArrayList<>(users.values())
                .stream()
                .filter(u -> u.getId().longValue() == user.getId().longValue())
                .findFirst()
                .get();
        if (user.getEmail() != null &&
                !user.getEmail().equals(userInDB.getEmail())) {
            users.remove(userInDB.getEmail());
        } else {
            user.setEmail(userInDB.getEmail());
        }

        if (user.getName() == null) {
            user.setName(userInDB.getName());
        }

        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User getUserByID(Long id) {
        Optional<User> userOptional = users.values()
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        return userOptional.orElseGet(() -> null);
    }

    @Override
    public User deleteUserByID(Long id) {
        User user = getUserByID(id);
        users.remove(user.getEmail());
        return user;
    }
}
