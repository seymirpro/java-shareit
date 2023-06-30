package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
