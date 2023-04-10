package ru.yandex.practicum.filmorate.service.impl;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

public class Validator {
    private static UserStorage userStorage;
    private static FilmStorage filmStorage;
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);

    public Validator() {
    }

    public static void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new ValidationException("Email не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Email должно содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            throw new ValidationException("Login не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login не может содержать пробелы!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday не может быть в будущем!");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static void validateUserId(int id) {
        if (userStorage.findUserById(id) == null) {
            throw new ObjectNotFoundException("Person's doesn't found!");
        }
    }

    public static void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Name не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("MAX длина описания — 200 символов!");
        }
        if (film.getReleaseDate().isBefore(DATE)) {
            throw new ValidationException("Дата релиза не может быть раньше " + DATE);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }

    public static void validateFilmId(int id) {
        if (filmStorage.findFilmById(id) == null) {
            throw new ObjectNotFoundException("Film doesn't found!");
        }
    }

}
