package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;
import java.util.Optional;

public interface RatingMpaStorage {
    Optional<RatingMpa> findRatingById(int id);
    List<RatingMpa> findAllRating();
}
