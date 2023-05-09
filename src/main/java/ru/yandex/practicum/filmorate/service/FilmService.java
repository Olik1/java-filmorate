package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface FilmService {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getTopFilms(int count);

    Film findFilmById(int id);

    RatingMpa getMpa(int id);

    List<RatingMpa> getMpaList();


    List<Genre> getGenreList();

    Genre getGenre(int id);

}
