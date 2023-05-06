package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@AllArgsConstructor
public class RatingMpaDbStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private SqlRowSet sqlRowSet;

    @Override
    public Optional<RatingMpa> findRatingById(int id) {
        sqlRowSet = jdbcTemplate.queryForRowSet("select * from ratingmpa where id = ?", id);
        if (sqlRowSet.next()) {
            return Optional.of(mapToRow(sqlRowSet));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<RatingMpa> findAllRating() {
        List<RatingMpa> ratingMpaList = new ArrayList<>();
        sqlRowSet = jdbcTemplate.queryForRowSet("select * from ratingmpa order by id");
        while (sqlRowSet.next()) {
            RatingMpa ratingMpa = mapToRow(sqlRowSet);
            ratingMpaList.add(ratingMpa);
        }
        return ratingMpaList;
    }

    private RatingMpa mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("id");
        String name = sqlRowSet.getString("name");
        return RatingMpa.builder()
                .id(id)
                .name(name)
                .build();
    }
}
