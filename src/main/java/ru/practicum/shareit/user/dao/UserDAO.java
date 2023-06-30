package ru.practicum.shareit.user.dao;


import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDAO {
    User createUser(User user);

    List<User> getUsers();

    boolean isDuplicateEmail(User user);

    User updateUser(User user);

    User getUserByID(Long id);

    User deleteUserByID(Long id);
}
