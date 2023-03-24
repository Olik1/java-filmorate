package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм: {}", film);
        return filmService.createFilm(film);
    }
    @GetMapping
    public List<Film> getFilm() {
        return filmService.getAlFilms();
    }

    @PutMapping
    public Film updateUser(@Valid @RequestBody Film film) {
        log.info("Обновление данных по фильму: {}", film);
        return filmService.updateFilm(film);
    }
}
