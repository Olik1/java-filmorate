package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User addUser(User user);

    User findUserById(int id);

    List<User> getUserList();

    void deleteAllUsers();

}
