package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @NotEmpty
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "^[^ ]+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

}
