package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
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
        throwErrorIfDuplicateEmail(user);

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException();
        }

        return UserMapper.toUserDto(userDAO.createUser(user));
    }

    public List<UserDto> getUsers() {
        return UserMapper.toUsersDto(userDAO.getUsers());
    }

    public UserDto getUserByID(Long id) {
        throwErrorIfUserDoesNotExist(id);
        User user = userDAO.getUserByID(id);
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(userId);
        throwErrorIfUserDoesNotExist(userId);
        throwErrorIfDuplicateEmail(user);
        user.setId(userId);
        User userUpdated = userDAO.updateUser(user);
        return UserMapper.toUserDto(userUpdated);
    }

    public UserDto deleteUserByID(Long id) {
        throwErrorIfUserDoesNotExist(id);
        return UserMapper.toUserDto(userDAO.deleteUserByID(id));
    }

    private void throwErrorIfUserDoesNotExist(Long id) {
        User user = userDAO.getUserByID(id);
        if (user == null) {
            throw new UserDoesNotExistException();
        }
    }

    private void throwErrorIfDuplicateEmail(User user) {
        if (userDAO.isDuplicateEmail(user)) {
            throw new DuplicateEmailException();
        }
    }
}
