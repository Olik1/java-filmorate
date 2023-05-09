package ru.yandex.practicum.filmorate.storage.dao;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.*;

@Repository("UserDbStorage")
@Primary
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (ResultSet resultSet, int rowNum) -> User.builder()
            .id(resultSet.getInt("id"))
            .login(resultSet.getString("login"))
            .email(resultSet.getString("email"))
            .name(resultSet.getString("name"))
            .birthday(resultSet.getDate("birthday").toLocalDate())
            .build();

    @Override
    public User save(User user) {
        //запрос обновляет данные по user_id
        String updateSql = "update users set email = ?, login = ?, name = ?, birthday = ? where id = ?";
        if (jdbcTemplate.update(updateSql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        ) <= 0) {
            log.error("Пользователь не найден {}", user.getId());
            throw new ObjectNotFoundException("Пользователь не найден");
        } else {
            return user;
        }
    }

    @Override
    public User findUserById(int id) {
        String sqlQuery = "SELECT * FROM USERS WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sqlQuery, userRowMapper, id);
            log.info("Пользователь с id {} найден:", id);
            return user;
        } catch (Exception e) {
            throw new ObjectNotFoundException("Пользователь не найден!");
        }
    }

    @Override
    public List<User> getUserList() {
        List<User> users = new ArrayList<>();
        String sql = "select * from users";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()) {
            User user = mapToRow(sqlRowSet);
            users.add(user);
        }
        log.info("Количество пользователей: {}", users.size());
        return users;
    }

    @Override
    public void deleteAllUsers() {
        String sql = "delete from users";
        jdbcTemplate.update(sql);
    }

    @Override
    public Set<Integer> getAllId() {
        return null;
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", user.getEmail());
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("birthday", user.getBirthday());

        Number userKey = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));

        user.setId(userKey.intValue());


        return user;

    }

    private User mapToRow(SqlRowSet sqlRowSet) {
        int id = sqlRowSet.getInt("id");
        String email = sqlRowSet.getString("email");
        String login = sqlRowSet.getString("login");
        String name = sqlRowSet.getString("name");
        LocalDate birthday = sqlRowSet.getDate("birthday").toLocalDate();
        return User.builder()
                .id(id)
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
