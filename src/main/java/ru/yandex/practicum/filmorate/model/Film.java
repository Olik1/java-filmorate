package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank()
    @Size(max=200)
    private String name;
    @NotEmpty
    @NotNull
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;

}
