package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User save(User user);

    User findUserById(int id);

    List<User> getUserList();

    void deleteAllUsers();

    User addUser(User user);
}
