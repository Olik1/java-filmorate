package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^[^ ]+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
