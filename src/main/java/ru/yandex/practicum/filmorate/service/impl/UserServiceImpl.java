package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.impl.Validator.validateUserId;
import static ru.yandex.practicum.filmorate.service.impl.Validator.validateUser;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //данный класс реализует бизнес-логику хранение, обновление и получение списка Пользоватей
    private static int id;
    UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(generateUserId());
        userStorage.save(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.getAllId().contains(user.getId())) {
            validateUser(user);
            userStorage.save(user);
            return user;
        } else {
            log.error("ERROR: ID введен неверно - такого пользователя не существует!");
            throw new ObjectNotFoundException("Такого пользователя не существует!");
        }

    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getUserList();
    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        Set<Integer> users = userStorage.findUserById(userId).getFriends();

        return userStorage.findUserById(friendId).getFriends().stream()
                .filter(users::contains)
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public Integer addFriend(int userId, int friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        User user = userStorage.findUserById(userId);
        user.addFriend(friendId);
        User friend = userStorage.findUserById(friendId);
        friend.addFriend(userId);

        return userStorage.findUserById(userId).getFriends().size();
    }

    @Override
    public void deleteFriendById(int userId, int friendId) {
        validateUserId(userId);
        validateUserId(friendId);
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);

        log.debug("Total friends: {}", userStorage.findUserById(userId).getFriends().size());
    }

    @Override
    public List<User> getListOfFriends(int id) {
        validateUserId(id);
        return userStorage.findUserById(id).getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(int id) {
        validateUserId(id);
        return userStorage.findUserById(id);
    }

    private int generateUserId() {
        return ++id;
    }


}
