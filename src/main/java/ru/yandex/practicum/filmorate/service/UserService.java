package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    List<User> getCommonFriends(int user1Id, int user2Id);

    Friendship addFriend(int userId, int friendId);

    void deleteFriendById(int userId, int friendId);

    List<User> getListOfFriends(int id);

    User getUserById(int id);

}
