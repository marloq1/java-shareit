package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * TODO Sprint add-controllers.
 */
@Data
@EqualsAndHashCode(of = {"email"})
public class User {

    Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;

}
