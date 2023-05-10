package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("FilmDbStorage")
@Primary
public class FilmDbStorage extends DbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final RatingMpaStorage ratingMpaStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RatingMpaStorage ratingMpaStorage,
                         GenreStorage genreStorage, LikesStorage likesStorage) {
        super(jdbcTemplate);
        this.ratingMpaStorage = ratingMpaStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
    }

    @Override
    public Film save(Film film) {
        String updateSql = "update Films set name = ?, description = ?, releaseDate = ?, duration = ?, RATINGMPAID = ? where id = ?";
        if (jdbcTemplate.update(updateSql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId())
                <= 0) {
            log.error("Фильм не найден {}", film.getId());
            throw new ObjectNotFoundException("Фильм не найден");
        } else {
            return film;
        }
    }

    @Override
    public Film findFilmById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, name, description, RELEASEDATE, duration, RATINGMPAID from FILMS where id = ?", id);
        if (sqlRowSet.next()) {
            var film = mapToRow(sqlRowSet);
            return film;
        } else {
            throw new ObjectNotFoundException(String.format("Film's id %d doesn't found!", id));
        }
    }

    //получаем весь список фильмов, установкой жанра фильму занимается FilmService.
    @Override
    public List<Film> getFilmList() {
        List<Film> films = new ArrayList<>();
        String sql = "select id, name, description, RELEASEDATE, duration, RATINGMPAID from films";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            Film film = mapToRow(sqlRowSet);
            films.add(film);
        }
        log.info("Количество фильмов: {}", films.size());
        return films;
    }

    @Override
    public void deleteAllFilms() {
        String sql = "delete from films";
        jdbcTemplate.update(sql);
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("releaseDate", film.getReleaseDate());
        parameters.put("duration", film.getDuration());
        parameters.put("ratingMpaId", film.getMpa().getId());

        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        film.setId(userKey.intValue());
        return film;
    }

    private Film mapToRow(SqlRowSet sqlRowSet) {
        int mpaId = sqlRowSet.getInt("ratingMpaId");
        int id = sqlRowSet.getInt("id");
        String name = sqlRowSet.getString("name");
        String description = sqlRowSet.getString("description");
        LocalDate date = sqlRowSet.getDate("releaseDate").toLocalDate();
        int duration = sqlRowSet.getInt("duration");
        RatingMpa mpa = RatingMpa.builder()
                .id(mpaId)
                .build();

        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(date)
                .duration(duration)
                .mpa(mpa)
                .build();
    }
}
