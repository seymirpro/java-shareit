package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserAlreadyExistsException;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
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

    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (userDAO.getUserByEmail(user) != null) {
            throw new UserAlreadyExistsException();
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException();
        }

        return UserMapper.toUserDto(userDAO.createUser(user));
    }

    public List<UserDto> getUsers() {
        return UserMapper.toUsersDto(userDAO.getUsers());
    }

    public UserDto getUserByID(Long id) {
        checkUser(id);
        User user = userDAO.getUserByID(id);
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        checkUser(id);
        User userActualInfo = userDAO.getUserByID(id);
        user.setId(userActualInfo.getId());
        User userInDb = userDAO.getUserByEmail(user);
        if (userInDb != null && user.getId().longValue() != userInDb.getId().longValue()) {
            throw new RuntimeException();
        }
        userDAO.updateUser(user);
        return UserMapper.toUserDto(user);
    }

    public UserDto deleteUserByID(Long id) {
        checkUser(id);
        return UserMapper.toUserDto(userDAO.deleteUserByID(id));
    }

    private void checkUser(Long id) {
        User user = userDAO.getUserByID(id);
        if (user == null) {
            throw new UserDoesNotExistException();
        }
    }
}
