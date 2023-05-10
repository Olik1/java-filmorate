package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.HashSet;
import java.util.Set;

@Repository
@Primary
public class RatingMpaDbStorage extends DbStorage implements RatingMpaStorage {

    public RatingMpaDbStorage(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public RatingMpa findRatingById(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, name from RatingMpa where id = ?", id);
        if (sqlRowSet.next()) {
            return mapToRow(sqlRowSet);
        } else {
            throw new ObjectNotFoundException("not found mpa");
        }
    }

    @Override
    public Set<RatingMpa> findAllRating() {
        Set<RatingMpa> ratingMpaList = new HashSet<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("select id, name from RatingMpa order by id");
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
