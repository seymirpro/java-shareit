package ru.practicum.shareit.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String name;
}
