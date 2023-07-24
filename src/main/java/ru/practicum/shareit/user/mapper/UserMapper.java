package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserDtoGetId;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {
    public User toUser(UserDto userDto) {
        User user = User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
        return userDto;
    }

    public List<UserDto> toUsersDto(List<User> users) {
        List<UserDto> usersDto = users.stream()
                .map(u -> toUserDto(u))
                .collect(Collectors.toList());
        return usersDto;
    }

    public static UserDtoGetId toUserDtoGetId(User user) {
        return UserDtoGetId.builder().id(user.getId()).build();
    }
}
