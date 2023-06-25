package ru.practicum.shareit.user;


import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{
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
    public User getUserByEmail(User user){
        return users.get(user.getEmail());
    }

    @Override
    public User updateUser(User user) {
        User userInDB = new ArrayList<>(users.values())
                .stream()
                .filter(u -> u.getId() == user.getId())
                .findFirst()
                .get();
        if (user.getEmail() != null &&
                !user.getEmail().equals(userInDB.getEmail())){
            users.remove(userInDB.getEmail());
        } else {
            user.setEmail(userInDB.getEmail());
        }

        if (user.getName() == null){
            user.setName(userInDB.getName());
        }

        users.put(user.getEmail(), user);
        return user;
    }

    @Override
    public User getUserByID(Long id) {
        User user = users.values()
                .stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .get();
        return user;
    }

    @Override
    public User deleteUserByID(Long id) {
        User user = getUserByID(id);
        users.remove(user.getEmail());
        return user;
    }
}
