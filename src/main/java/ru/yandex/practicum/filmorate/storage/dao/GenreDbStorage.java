package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private SqlRowSet sqlRowSet;

    @Override
    public Optional<Genre> findGenreById(int id) {
        sqlRowSet = jdbcTemplate.queryForRowSet("select * from genre where id = ?", id);
        if (sqlRowSet.next()) {
            return Optional.of(mapToRow(sqlRowSet));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        List<Genre> genres = new ArrayList<>();
        sqlRowSet = jdbcTemplate.queryForRowSet("select * from genre order by id");
        while (sqlRowSet.next()) {
            Genre genre = mapToRow(sqlRowSet);
            genres.add(genre);
        }
        return genres;
    }

    @Override
    public List<Genre> findGenreByFilm(int filmId) {
        List<Genre> genres = new ArrayList<>();
        //SQL-запрос для получения жанров с выборкой по filmId из двух таблиц Film_Genre и Genre
        String sql = "SELECT id, name from genre, filmgenres WHERE filmgenres.genreId = genres.id AND filmgenres.filmId = ?";
        //получаем все строки результата выборки
        sqlRowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{filmId});
        while (sqlRowSet.next()) {
            Genre genre = mapToRow(sqlRowSet);
            genres.add(genre);
        }
        return genres;
    }

    private Genre mapToRow(SqlRowSet sqlRowSet) {
        //преобразуем данные из запроса к бд к типу жанр
        int id = sqlRowSet.getInt("id");
        String name = sqlRowSet.getString("name");
        return Genre.builder()
                .id(id)
                .name(name)
                .build();
    }
}
