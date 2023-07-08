package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utils.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserDto {
    private Long id;
    @NotNull(groups = {Create.class})
    private String name;
    @Email(groups = {Create.class}, message = "Указана невалидная почта для пользователя")
    @NotBlank(groups = {Create.class})
    @NotNull(groups = {Create.class}, message = "Поле почта должно быть заполнено")
    private String email;
}
