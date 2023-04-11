package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int id;

    @Override
    public Film save(Film film) {
        return films.put(film.getId(), film);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(generateFilmId());
        return films.put(film.getId(), film);
    }

    @Override
    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            log.error("ERROR: ID введен неверно - такого фильма не существует!");
            throw new ObjectNotFoundException(String.format("Film's id %d doesn't found!", id));
        }
        return films.get(id);
    }

    @Override
    public List<Film> getFilmList() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void deleteAllFilms() {
        films.clear();
    }

    public int generateFilmId() {
        return ++id;
    }

    public Set<Integer> getAllId() {
        return films.keySet();
    }
}
