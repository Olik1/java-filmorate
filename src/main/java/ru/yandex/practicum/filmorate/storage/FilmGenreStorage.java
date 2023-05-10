package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.List;

public interface FilmGenreStorage {
    void deleteByFilmId(int filmId);

    FilmGenre add(FilmGenre filmGenre);

    List<FilmGenre> getLikesFilmId(int filmId);
    List<FilmGenre> findAllFilmGenre();
}
