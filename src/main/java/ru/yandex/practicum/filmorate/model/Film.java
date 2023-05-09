package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
   // private int ratingMpaId;

    @NotNull
    private RatingMpa mpa;
    private List<Genre> genres;
    private Set<Integer> likes;




}
