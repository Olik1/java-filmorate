package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    Film findFilmById(int id);
    List<Film> getFilmList();
    void deleteAllFilms();
}
