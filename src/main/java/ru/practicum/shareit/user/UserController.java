package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto){
        User user = UserMapper.toUser(userDto);
        userService.createUser(user);
        UserDto userDtoUPD = UserMapper.toUserDto(user);
        return userDtoUPD;
    }

    @GetMapping
    public List<UserDto> getUsers(){
        List<User> users = userService.getUsers();
        return UserMapper.toUsersDto(users);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        User userUpdated = userService.updateUser(id, user);
        return UserMapper.toUserDto(userUpdated);
    }

    @GetMapping("/{id}")
    public UserDto getUserByID(@PathVariable Long id){
        User user = userService.getUserByID(id);
        UserDto userDto = UserMapper.toUserDto(user);
        return userDto;
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUserByID(@PathVariable Long id){
        User user = userService.deleteUserByID(id);
        UserDto userDto = UserMapper.toUserDto(user);
        return userDto;
    }
}
