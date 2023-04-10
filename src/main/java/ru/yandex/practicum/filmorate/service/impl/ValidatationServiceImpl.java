package ru.yandex.practicum.filmorate.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ValidatationService;

import java.time.LocalDate;

@Component
public class ValidatationServiceImpl implements ValidatationService {

    private final LocalDate date = LocalDate.of(1895, 12, 28);

    @Autowired
    public ValidatationServiceImpl() {

    }

    @Override
    public void validateUser(User user) {
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


    @Override
    public void validateFilm(Film film) {

        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Name не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("MAX длина описания — 200 символов!");
        }
        if (film.getReleaseDate().isBefore(date)) {
            throw new ValidationException("Дата релиза не может быть раньше " + date);
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной!");
        }

    }


}
