package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository("FilmDbStorage")
@Primary
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

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
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from FILMS where id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new ObjectNotFoundException(String.format("Film's id %d doesn't found!", id));
//            return Optional.empty();
        }

//        String sql = "select f.*, m.name as mpa_name from films as f join RATINGMPA as m on f.RATINGMPAID = m.ID where f.ID = ?";
//        try {
//            return mapRowToFilm(rs), id);
//        } catch (Exception e) {
//            log.warn("Фильм с id {} не найден", id);
//            throw new ObjectNotFoundException(String.format("Film's id %d doesn't found!", id));
//        }


//
//        String sql = "SELECT * FROM users WHERE id = ?";
//        RowMapper<Film> mapper = new BeanPropertyRowMapper<>(Film.class);
//        return jdbcTemplate.queryForObject(sql, mapper, id);


//        String sqlRequest = "select * from films where id = ?";
//        try {
//            Film film = jdbcTemplate.queryForObject(sqlRequest, (rs, rowNum) -> mapToRow((SqlRowSet) rs), id);
//            log.error("Фильм c id найден: {}", film.getId());
//            return film;
//        } catch (ObjectNotFoundException e) {
//            log.error("Фильм не найден {}", id);
//            throw new ObjectNotFoundException("Фильм не найден");
//        }
    }

    @Override
    public List<Film> getFilmList() {
        List<Film> films = new ArrayList<>();
        String sql = "select * from films";
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
    public Set<Integer> getAllId() {
        return null;
    }

    @Override
    public Film addFilm(Film film) {

//
//        String sql = "insert into films (name, description, releasedate, duration, ratingMpaId) values (?, ?, ?, ?, ?)";
//        jdbcTemplate.update(sql,
//                film.getName(),
//                film.getDescription(),
//                film.getReleaseDate(),
//                film.getDuration(),
//                film.getRatingMpaId()
//                );
//        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select * from films where name = ?", film.getName());
//        if (sqlRowSet.next()) {
//            return mapToRow(sqlRowSet);
//        } else {
//            log.info("Ошибка при добавлении нового фильма");
//            return film;
//        }






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
      //  String mpaName = sqlRowSet.getString("ratingMpa_name");
        RatingMpa mpa = RatingMpa.builder()
                .id(mpaId)
             //   .name(mpaName)
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



    private Film mapRowToFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("duration");
        int mpaId = rs.getInt("ratingMpaId");
        String mpaName = rs.getString("ratingMpa_name");

        RatingMpa mpa = RatingMpa.builder()
                .id(mpaId)
                .name(mpaName)
                .build();
        return Film.builder()
                .id(id)
                .name(name)
                .description(description)
                .releaseDate(releaseDate)
                .duration(duration)
                .mpa(mpa)
                .build();
    }


}
