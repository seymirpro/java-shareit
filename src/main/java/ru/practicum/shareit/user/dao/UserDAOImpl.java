package ru.practicum.shareit.user.dao;


import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {
    private static long id = 0;
    private static HashMap<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isDuplicateEmail(User user) {
        User userInDb = users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .findFirst()
                .orElseGet(() -> null);

        return userInDb != null && user.getId().longValue() != userInDb.getId().longValue();
    }

    @Override
    public User updateUser(User user) {
        User userInDB = getUserByID(user.getId());
        if (user.getEmail() != null &&
                !user.getEmail().equals(userInDB.getEmail())) {

            userInDB.setEmail(user.getEmail());
        }

        if (user.getName() != null && !user.getName().equals(userInDB.getName())) {
            userInDB.setName(user.getName());
        }

        users.put(userInDB.getId(), userInDB);
        return userInDB;
    }

    @Override
    public User getUserByID(Long id) {
        return users.get(id);
    }

    @Override
    public User deleteUserByID(Long id) {
        User user = getUserByID(id);
        users.remove(id);
        return user;
    }
}
