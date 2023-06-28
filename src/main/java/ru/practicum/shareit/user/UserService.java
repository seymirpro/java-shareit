package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User createUser(User user) {
        if (userDAO.getUserByEmail(user) != null) {
            throw new UserAlreadyExistsException();
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException();
        }

        return userDAO.createUser(user);
    }

    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    public User getUserByID(Long id) {
        User user = userDAO.getUserByID(id.longValue());
        if (user == null) {
            throw new UserDoesNotExistException();
        }

        return user;
    }

    public User updateUser(Long id, User user) {
        User userActualInfo = userDAO.getUserByID(id.longValue());
        if (userActualInfo == null) {
            throw new UserDoesNotExistException();
        } else {
            user.setId(userActualInfo.getId());
            User userInDb = userDAO.getUserByEmail(user);
            if (userInDb != null && user.getId().longValue() != userInDb.getId().longValue()) {
                throw new RuntimeException();
            }
            userDAO.updateUser(user);
        }
        return user;
    }

    public User deleteUserByID(Long id) {
        User user = getUserByID(id.longValue());
        if (user == null) {
            throw new UserDoesNotExistException();
        }
        return userDAO.deleteUserByID(id.longValue());
    }
}
