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
        User newUser = userRepository.save(UserMapper.toUser(userDto));
        return UserMapper.toUserDto(newUser);
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
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(userRepository.save(user));
    }

    public UserDto deleteUserByID(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                UserDoesNotExistException::new
        );
        userRepository.deleteById(id);
        return UserMapper.toUserDto(user);
    }
}
