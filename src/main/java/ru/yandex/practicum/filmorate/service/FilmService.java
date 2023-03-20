package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film createUser(Film film);
    Film updateUser(Film film);
    List<Film> getAlFilms();
}
