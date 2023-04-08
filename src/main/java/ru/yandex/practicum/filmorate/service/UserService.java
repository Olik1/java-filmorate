package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    List<User> getCommonFriends(int userId, int friendId);

    Integer addFriend(int userId, int friendId);

    void deleteFriendById(int userId, int friendId);

}
