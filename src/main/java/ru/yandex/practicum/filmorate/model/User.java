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
    @NotBlank(message = "логин не может быть пустым!")
    @Pattern(regexp = "^[^ ]+$")
    private String login;
    private String name;
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
