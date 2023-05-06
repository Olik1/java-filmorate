package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    boolean isAdded(int userId, int friendId);
    boolean isUpdate(int userId, int friendId);
    boolean isDeleted(int userId, int friendId);
    List<Friendship> getFriendsIdByUser(int id);
}
