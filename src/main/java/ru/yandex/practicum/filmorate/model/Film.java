package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
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
    private final Set<Integer> likes = new HashSet<>();
    public void addLike(User user) {
        likes.add(user.getId());
    }
    public void deleteLike(User user) {
        likes.remove(user.getId());
    }

}
