package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.Set;

public interface FilmGenreStorage {
    Set<FilmGenre> getGenresByFilmId(int filmId);
    void deleteByFilmId(int filmId);

    FilmGenre add(FilmGenre filmGenre);
}
