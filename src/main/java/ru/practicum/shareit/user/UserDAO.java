package ru.practicum.shareit.user;


import java.util.List;

public interface UserDAO {
    User createUser(User user);

    List<User> getUsers();

    User getUserByEmail(User user);

    User updateUser(User user);

    User getUserByID(Long id);

    User deleteUserByID(Long id);
}
