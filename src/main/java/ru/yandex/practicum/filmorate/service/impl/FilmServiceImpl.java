package ru.yandex.practicum.filmorate.service.impl;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private static int id = 1;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createUser(Film film) {
        validateFilm(film);
        film.setId(generateFilmId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateUser(Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            log.error("ERROR: ID введен неверно - такого фильма не существует!");
            throw new ValidationException("Такого фильма не существует!");
        }

    }

    @Override
    public List<Film> getAlFilms() {
        return new ArrayList<>(films.values());
    }

    private static void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("ERROR: Поле Name не может быть пустым!");
            throw new ValidationException("Name не может быть пустым!");
        }
        if (film.getDescription() != null || film.getDescription().length() > 200) {
            log.error("ERROR: Поле Description должно содержать не более 200 символов!");
            throw new ValidationException("MAX длина описания — 200 символов!");
        }
        if (film.getReleaseDate() != null || film.getReleaseDate().isBefore(DATE)) {
            log.error("ERROR: Поле Release должно содержать корректную дату!");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 г.!");
        }
        if (film.getDuration() < 0) {
            log.error("ERROR: Поле Duration должно быть положительным!");
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }

    private int generateFilmId() {
        return ++id;
    }
}
