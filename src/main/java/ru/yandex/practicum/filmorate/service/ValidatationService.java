package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface ValidatationService {
    void validateUser(User user);

    void validateFilm(Film film);

}
