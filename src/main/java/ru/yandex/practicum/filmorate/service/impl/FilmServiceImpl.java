package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.impl.Validator.validateFilm;
import static ru.yandex.practicum.filmorate.service.impl.Validator.validateFilmId;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private static int id;

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(generateFilmId());
        filmStorage.save(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.getAllId().contains(film.getId())) {
            validateFilm(film);
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
        validateFilmId(userId);
        validateFilmId(filmId);
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.addLike(user);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        validateFilmId(userId);
        validateFilmId(filmId);
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

    public int generateFilmId() {
        return ++id;
    }


}
