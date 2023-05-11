package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.Set;

public interface RatingMpaStorage {
    RatingMpa findRatingById(int id);

    Set<RatingMpa> findAllRating();
}
