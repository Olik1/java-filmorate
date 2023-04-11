package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film save(Film film);

    Film findFilmById(int id);

    List<Film> getFilmList();

    void deleteAllFilms();

    Set<Integer> getAllId();

    Film addFilm(Film film);
}
