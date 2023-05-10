package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDbStorage extends DbStorage implements GenreStorage {

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Genre findGenreById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, name from genre where id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new ObjectNotFoundException("not found mpa");

        }
    }

    @Override
    public List<Genre> findAllGenres() {
        List<Genre> genres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, name from genre order by id");
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
