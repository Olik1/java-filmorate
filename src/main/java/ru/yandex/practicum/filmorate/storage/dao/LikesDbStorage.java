package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikesDbStorage extends DbStorage implements LikesStorage {

    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public void delete(Likes likes) {
        var sqlQuery = "DELETE FROM Likes WHERE FILMID = ? AND UserId = ?";
        jdbcTemplate.update(sqlQuery, likes.getFilmId(), likes.getUserId());
    }

    @Override
    public Set<Likes> getLikesFilmId(int filmId) {
        Set<Likes> likes = new HashSet<>();

        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select FILMID, USERID from LIKES where FILMID = ?", filmId);

        while (sqlRowSet.next()) {
            var like = mapToRow(sqlRowSet);
            likes.add(like);
        }

        return likes;
    }

    @Override
    public Likes getLikesUserFilmId(int userId, int filmId) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select FILMID, USERID from Likes where userId = ? AND filmId = ?", userId, filmId);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            return null;
        }
    }

    @Override
    public Set<Likes> findAllLikes() {
        Set<Likes> likes = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select FILMID, USERID from LIKES");
        while (sqlRowSet.next()) {
            Likes like = mapToRow(sqlRowSet);
            likes.add(like);
        }
        return likes;
    }

    @Override
    public Likes add(Likes likes) {

        String sql = "insert into Likes (FILMID, USERID) values(?, ?)";
        jdbcTemplate.update(sql,
                likes.getFilmId(),
                likes.getUserId());
        return likes;
    }

    private Likes mapToRow(SqlRowSet sqlRowSet) {
        //преобразуем данные из запроса к бд к типу жанр
        int filmId = sqlRowSet.getInt("filmId");
        int userId = sqlRowSet.getInt("userId");
        return Likes.builder()
                .filmId(filmId)
                .userId(userId)
                .build();
    }
}
