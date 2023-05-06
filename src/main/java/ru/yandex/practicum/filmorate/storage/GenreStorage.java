package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> findGenreById(int id);
    List<Genre> findAllGenres();
    List<Genre> findGenreByFilm(int filmId);

}
