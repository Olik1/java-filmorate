package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    Friendship added(Friendship friendship);

    Friendship update(Friendship friendship);

    boolean deleteById(Friendship friendship);

    List<Friendship> getFriendsIdByUser(int id);

    Friendship getFriendsRelation(int user1Id, int user2Id);
}
