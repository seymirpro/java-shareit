package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.DuplicateEmailException;
import ru.practicum.shareit.exception.UserDoesNotExistException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        /*Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser.isPresent()){
            throw new UserAlreadyExistsException(String.format("Пользователь с " +
                    "%s уже зарегистрирован", userDto.getEmail()));
        }

        User newUser = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(newUser);*/
        try {
            User newUser = userRepository.save(UserMapper.toUser(userDto));
            return UserMapper.toUserDto(newUser);
        } catch (RuntimeException ex) {
            throw new DuplicateEmailException(String.format("Почта %s  уже используется.",
                    userDto.getEmail()));
        }
    }

    public List<UserDto> getUsers() {
        return UserMapper.toUsersDto(userRepository.findAll());
    }

    public UserDto getUserByID(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserDoesNotExistException::new);
        return UserMapper.toUserDto(user);
    }

    public UserDto updateUser(long userId, UserDto userDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);
        if (userDto.getName() != null && !userDto.getName().isBlank()) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().isBlank()) {
            Optional<User> existingUser = userRepository.findByEmail(userDto.getEmail());
            if (existingUser.isPresent() && existingUser.get().getId() != user.getId()) {
                throw new DuplicateEmailException("Почта уже используется");
            }
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(userRepository.save(user));
    }

    public UserDto deleteUserByID(long id) {
        User user = userRepository.findById(id).orElseThrow(
                UserDoesNotExistException::new
        );
        userRepository.deleteById(id);
        return UserMapper.toUserDto(user);
    }
}
