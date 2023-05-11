package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class RatingMpa implements Comparable<RatingMpa> {
    @NotBlank
    private int id;
    private String name;

    @Override
    public int compareTo(RatingMpa o) {
        return Integer.compare(this.id, o.id);
    }
}
