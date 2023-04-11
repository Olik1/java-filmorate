package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.service.ValidatationService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FilmServiceImpl implements FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;
    ValidatationService validator;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage, ValidatationService validator) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.validator = validator;
    }

    @Override
    public Film createFilm(Film film) {
        validator.validateFilm(film);
        filmStorage.addFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.getAllId().contains(film.getId())) {
            validator.validateFilm(film);
            filmStorage.save(film);
        } else {
            log.error("ERROR: ID введен неверно - такого фильма не существует!");
            throw new ObjectNotFoundException("Такого фильма не существует!");
        }
        return film;

    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getFilmList());
    }

    @Override
    public void addLike(int userId, int filmId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.addLike(user);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.deleteLike(user);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        return filmStorage.getFilmList().stream().sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

    }


}
