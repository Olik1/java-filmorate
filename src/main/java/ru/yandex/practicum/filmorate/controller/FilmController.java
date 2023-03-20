package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film.toString());
        return filmService.createUser(film);
    }

//    @GetMapping
//    public List<User> getUser() {
//        return userService.getAllUsers();
//    }
//    @PutMapping
//    public User updateUser(@Valid @RequestBody User user) {
//        log.info("Обновление данных пользователя: {}", user.toString());
//        return userService.updateUser(user);
//    }
}
