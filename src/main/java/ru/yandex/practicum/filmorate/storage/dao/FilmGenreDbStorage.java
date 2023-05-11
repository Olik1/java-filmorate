package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.FilmGenreStorage;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FilmGenreDbStorage extends DbStorage implements FilmGenreStorage {

    public FilmGenreDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public List<FilmGenre> findAllFilmGenre() {
        List<FilmGenre> filmGenres = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select filmid, genreid from FILMGENRE");
        while (sqlRowSet.next()) {
            FilmGenre filmGenre = mapToRow(sqlRowSet);
            filmGenres.add(filmGenre);
        }
        return filmGenres;
    }

    @Override
    public List<FilmGenre> getLikesFilmId(int filmId) {
        List<FilmGenre> filmGenres = new ArrayList<>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select filmid, genreid from FILMGENRE where FILMID = ?", filmId);

        while (sqlRowSet.next()) {
            var filmGenre = mapToRow(sqlRowSet);
            filmGenres.add(filmGenre);
        }

        return filmGenres;
    }

    @Override
    public FilmGenre add(FilmGenre filmGenre) {

        String sql = "insert into FilmGenre (FILMID, GENREID) values(?, ?)";
        jdbcTemplate.update(sql,
                filmGenre.getFilmId(),
                filmGenre.getGenreId());
        return filmGenre;
    }

    @Override
    public void deleteByFilmId(int filmId) {
        var sqlQuery = "DELETE FROM FILMGENRE WHERE FILMID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    private FilmGenre mapToRow(SqlRowSet sqlRowSet) {
        //преобразуем данные из запроса к бд к типу жанр
        int genreId = sqlRowSet.getInt("genreId");
        int filmId = sqlRowSet.getInt("filmId");
        return FilmGenre.builder()
                .genreId(genreId)
                .filmId(filmId)
                .build();
    }
}
