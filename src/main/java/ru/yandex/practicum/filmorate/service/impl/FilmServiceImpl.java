package ru.yandex.practicum.filmorate.service.impl;

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

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private static int id;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    FilmStorage filmStorage;
    UserStorage userStorage;


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
            throw new ValidationException("Такого фильма не существует!");
        }
        return film;

    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getFilmList());
    }

    @Override
    public void addLike(int userId, int filmId) {
        if (filmStorage.findFilmById(userId) == null || filmStorage.findFilmById(filmId) == null) {
            throw new ObjectNotFoundException("Film doesn't found");
        }
        User user = userStorage.findUserById(userId);
        Film film = filmStorage.findFilmById(filmId);
        film.addLike(user);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (filmStorage.findFilmById(userId) == null || filmStorage.findFilmById(filmId) == null) {
            throw new ObjectNotFoundException("Film has been already deleted");
        }
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

    private int generateFilmId() {
        return ++id;
    }

    private void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("ERROR: Поле Name не может быть пустым!");
            throw new ValidationException("Name не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            log.error("ERROR: Поле Description должно содержать не более 200 символов!");
            throw new ValidationException("MAX длина описания — 200 символов!");
        }
        if (film.getReleaseDate().isBefore(DATE)) {
            log.error("ERROR: Поле Release должно содержать корректную дату!");
            throw new ValidationException("Дата релиза не может быть раньше " + DATE);
        }
        if (film.getDuration() < 0) {
            log.error("ERROR: Поле Duration должно быть положительным!");
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }

}
