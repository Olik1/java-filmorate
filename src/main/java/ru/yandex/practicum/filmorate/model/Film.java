package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private int id;
    @NotBlank()
    @Size(max = 200)
    private String name;
    @NotNull
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;

}
