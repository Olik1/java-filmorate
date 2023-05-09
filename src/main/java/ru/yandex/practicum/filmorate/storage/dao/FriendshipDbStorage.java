package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
@AllArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Friendship> friendshipRowMapper = (ResultSet resultSet, int rowNum) -> Friendship.builder()
            .userId(resultSet.getInt("userId"))
            .friendId(resultSet.getInt("friendId"))
            .status(resultSet.getBoolean("status"))
            .build();

    @Override
    public Friendship added(Friendship friendship) {
        String sql = "insert into Friendship (userid, friendid, status) values(?, ?, ?)";
        jdbcTemplate.update(sql,
                friendship.getUserId(),
                friendship.getFriendId(),
                friendship.isStatus());
        return friendship;
    }

    @Override
    public Friendship update(Friendship friendship) {
        String updateSql = "update FRIENDSHIP set STATUS = ? where USERID = ? AND FRIENDID = ?";
        if (jdbcTemplate.update(updateSql,
                friendship.isStatus(),
                friendship.getUserId(),
                friendship.getFriendId())
                <= 0) {
            throw new ObjectNotFoundException("запись не найдена");
        } else {
            return friendship;
        }
    }

    @Override
    public boolean deleteById(Friendship friendship) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USERID=? AND FRIENDID=?";
        return (jdbcTemplate.update(sql, friendship.getUserId(), friendship.getFriendId()) > 0);
    }

    @Override
    public List<Friendship> getFriendsIdByUser(int id) {
        List<Friendship> friendships = new ArrayList<>();

        String sql = "SELECT * FROM FRIENDSHIP WHERE userId = ? OR (friendId = ? AND status = ?)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, new Object[]{id, id, true});
        while (sqlRowSet.next()) {
            Friendship friendship = mapToRow(sqlRowSet);
            friendships.add(friendship);
        }
        return friendships;
    }


    @Override
    public Friendship getFriendsRelation(int user1Id, int user2Id) {

        String sqlQuery = "SELECT * FROM friendship WHERE userId = ? AND FRIENDID = ? OR userId = ? AND FRIENDID = ?";
        try {
            Friendship friendship = jdbcTemplate.queryForObject(sqlQuery, friendshipRowMapper, user1Id, user2Id, user2Id, user1Id);
            return friendship;
        } catch (Exception e) {
            return null;
        }
    }

    private Friendship mapToRow(SqlRowSet sqlRowSet) {
        int userId = sqlRowSet.getInt("userId");
        int friendId = sqlRowSet.getInt("friendId");
        boolean status = sqlRowSet.getBoolean("status");

        return Friendship.builder()
                .userId(userId)
                .friendId(friendId)
                .status(status)
                .build();
    }
}
