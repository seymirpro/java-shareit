package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    @Email(message = "Указана невалидная почта для пользователя")
    @NotBlank
    @NotNull(message = "Поле почта должно быть заполнено")
    private String email;
}
