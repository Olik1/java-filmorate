package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotBlank()
    @Size(max=200)
    private String name;
    @NotEmpty
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;

}
