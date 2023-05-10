package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.impl.ValidatationService.validateFilm;


@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingMpaStorage ratingMpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final FilmGenreStorage filmGenreStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage, RatingMpaStorage ratingMpaStorage,
                           GenreStorage genreStorage, LikesStorage likesStorage,
                           FilmGenreStorage filmGenreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.ratingMpaStorage = ratingMpaStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
        this.filmGenreStorage = filmGenreStorage;
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        filmStorage.addFilm(film);
        addGenreForFilm(film);
        return film;
    }

    private void addGenreForFilm(Film film) {
        if (film.getGenres() != null && film.getGenres().size() > 0) {

            List<Genre> ratingList = film.getGenres();

            Set<Integer> set = new HashSet<>(); // создаем множество для проверки уникальности id

            ratingList.removeIf(rating -> !set.add(rating.getId()));
            film.setGenres(ratingList);

            for (var genre : film.getGenres()) {
                var filmGenre = new FilmGenre(film.getId(), genre.getId());
                filmGenreStorage.add(filmGenre);
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        validateFilm(film);

        try {
            filmStorage.save(film);
        } catch (Exception e) {
            throw new ObjectNotFoundException("Такого фильма не существует!");
        }

        filmGenreStorage.deleteByFilmId(film.getId());
        addGenreForFilm(film);
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        var film = filmStorage.findFilmById(id);

        var mpaList = ratingMpaStorage.findAllRating();
        var genres = genreStorage.findAllGenres();
        var filmGenres = filmGenreStorage.findAllFilmGenre();
        var likes = likesStorage.findAllLikes();

        setMpaGenreLikesForFilm(film, mpaList, genres, filmGenres, likes);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        var films = filmStorage.getFilmList();


        var mpaList = ratingMpaStorage.findAllRating();
        var genres = genreStorage.findAllGenres();
        var filmGenres = filmGenreStorage.findAllFilmGenre();
        var likes = likesStorage.findAllLikes();
        for (var film : films) {
            setMpaGenreLikesForFilm(film, mpaList, genres, filmGenres, likes);
        }
        return films;
    }

    @Override
    public void addLike(int userId, int filmId) {
        Likes like = likesStorage.getLikesUserFilmId(userId, filmId);
        if (like == null) {
            likesStorage.add(new Likes(filmId, userId));
        }
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        Likes like = likesStorage.getLikesUserFilmId(userId, filmId);
        if (like == null) {
            throw new ObjectNotFoundException("doesn't found!");
        }
        likesStorage.delete(new Likes(filmId, userId));
    }

    @Override
    public List<Film> getTopFilms(int count) {
        var films = getAllFilms();

        return films.stream().sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Genre getGenre(int id) {
        return genreStorage.findGenreById(id);
    }

    @Override
    public List<Genre> getGenreList() {
        return genreStorage.findAllGenres();
    }

    @Override
    public RatingMpa getMpa(int id) {
        return ratingMpaStorage.findRatingById(id);
    }

    @Override
    public List<RatingMpa> getMpaList() {
        var mpaList = ratingMpaStorage.findAllRating();
        List<RatingMpa> list = new ArrayList<>(mpaList); // преобразуем Set в List
        Collections.sort(list, Comparator.comparing(RatingMpa::getId)); // сортируем объекты по полю id
        return list;
    }

    //установка жанра, лайков и название рейтинга
    private void setMpaGenreLikesForFilm(Film film, Set<RatingMpa> mpaList, List<Genre> genres,
                                         List<FilmGenre> filmGenres, List<Likes> likes) {
        List<Genre> genreByFilm = new ArrayList<>();
        filmGenres.stream().filter(f -> f.getFilmId() == film.getId())
                .forEach(f -> genreByFilm.add(
                        new Genre(f.getGenreId(),
                                genres.stream().filter(g -> g.getId() == f.getGenreId()).findAny().get().getName())));

        film.setGenres(genreByFilm);

        film.getMpa().setName(mpaList.stream().filter(m -> m.getId() == film.getMpa().getId()).findAny().get().getName());

        Set<Integer> likesByFilm = new HashSet<>();
        likes.stream().filter(l -> l.getFilmId() == film.getId()).forEach(l -> likesByFilm.add(l.getUserId()));
        film.setLikes(likesByFilm);

    }
}