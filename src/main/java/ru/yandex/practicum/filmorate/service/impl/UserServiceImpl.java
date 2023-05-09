package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.dao.FriendshipDbStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.service.impl.ValidatationService.validateUser;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //данный класс реализует бизнес-логику хранение, обновление и получение списка Пользоватей

    private final UserStorage userStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage, FriendshipDbStorage friendshipDbStorage) {
        this.userStorage = userStorage;
        this.friendshipDbStorage = friendshipDbStorage;

    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        //  if (userStorage.getAllId().contains(user.getId())) {
        validateUser(user);
        userStorage.save(user);
        return user;
//        } else {
//            log.error("ERROR: ID введен неверно - такого пользователя не существует!");
//            throw new ObjectNotFoundException("Такого пользователя не существует!");
//        }

    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getUserList();
    }

    @Override
    public List<User> getCommonFriends(int user1Id, int user2Id) {

        var friendshipsByUser1 = friendshipDbStorage.getFriendsIdByUser(user1Id);
        var friendshipsByUser2 = friendshipDbStorage.getFriendsIdByUser(user2Id);
        List<Integer> friendIdByUser1 = new ArrayList<>();
        List<Integer> friendIdByUser2 = new ArrayList<>();
        for (var friendshipsByUser : friendshipsByUser1) {
            int userId;
            if (friendshipsByUser.getUserId() == user1Id)
                userId = friendshipsByUser.getFriendId();
            else
                userId = friendshipsByUser.getUserId();
            friendIdByUser1.add(userId);
        }

        for (var friendshipsByUser : friendshipsByUser2) {
            int userId;
            if (friendshipsByUser.getUserId() == user2Id)
                userId = friendshipsByUser.getFriendId();
            else
                userId = friendshipsByUser.getUserId();
            friendIdByUser2.add(userId);
        }

        friendIdByUser1.retainAll(friendIdByUser2);
        List<User> users = new ArrayList<>();

        for (var commonFriendId : friendIdByUser1) {
            users.add(userStorage.findUserById(commonFriendId));
        }

        return users;
    }

    @Override
    public Friendship addFriend(int userId, int friendId) {

        var friendShip = friendshipDbStorage.getFriendsRelation(userId, friendId);

        if (friendShip == null) {
            try {
                return friendshipDbStorage.added(new Friendship(userId, friendId, false));
            } catch (Exception e) {
                throw new ObjectNotFoundException("Запись не найдена");
            }
        } else if (friendShip.isStatus()) {
            return friendShip;
        } else if (friendShip.getFriendId() == userId) {
            friendShip.setStatus(true);
            return friendshipDbStorage.update(friendShip);
        }

        return friendShip;

//        var friendship = new Friendship(userId, friendId, false);
//        User user = userStorage.findUserById(userId);
//        User friend = userStorage.findUserById(friendId);
////        user.addFriend(friendId);
////        friend.addFriend(userId);
//        friendshipDbStorage.added(friendship);
//        return userStorage.findUserById(userId).getFriends().size();
    }

    @Override
    public void deleteFriendById(int userId, int friendId) {


        var friendShip = friendshipDbStorage.getFriendsRelation(userId, friendId);

        if (friendShip == null) {
            throw new ObjectNotFoundException("Запись не найдена");

        } else {
            if (friendShip.isStatus()) {
                if (friendShip.getUserId() == userId) {
                    friendShip.setUserId(friendId);
                    friendShip.setFriendId(userId);
                    friendShip.setStatus(false);
                    friendshipDbStorage.update(friendShip);
                } else {
                    friendShip.setStatus(false);
                    friendshipDbStorage.update(friendShip);
                }
            } else {
                if (friendShip.getUserId() == userId) {
                    friendshipDbStorage.deleteById(friendShip);

                }
            }
        }

        // return  null;

//        User user = userStorage.findUserById(userId);
//        User friend = userStorage.findUserById(friendId);
//        user.deleteFriend(friendId);
//        friend.deleteFriend(userId);
//
//        log.debug("Total friends: {}", userStorage.findUserById(userId).getFriends().size());
    }

    @Override
    public List<User> getListOfFriends(int id) {
        var friendships = friendshipDbStorage.getFriendsIdByUser(id);
        List<User> users = new ArrayList<>();

        for (var friendship : friendships) {
            int userId;
            if (friendship.getUserId() == id)
                userId = friendship.getFriendId();
            else
                userId = friendship.getUserId();
            var user = userStorage.findUserById(userId);
            users.add(user);
        }

        return users;


//        return userStorage.findUserById(id).getFriends().stream()
//                .map(userStorage::findUserById)
//                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        return userStorage.findUserById(id);
    }


}
