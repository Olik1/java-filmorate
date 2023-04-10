package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //данный класс реализует бизнес-логику хранение, обновление и получение списка Пользоватей
    private static int id;
    UserStorage userStorage;

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
        validateId(userId);
        validateId(friendId);
        User user = userStorage.findUserById(userId);
        user.addFriend(friendId);
        User friend = userStorage.findUserById(friendId);
        friend.addFriend(userId);

        return userStorage.findUserById(userId).getFriends().size();
    }

    @Override
    public void deleteFriendById(int userId, int friendId) {
        validateId(userId);
        validateId(friendId);
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);

        log.debug("Total friends: {}", userStorage.findUserById(userId).getFriends().size());
    }

    @Override
    public List<User> getListOfFriends(int id) {
        validateId(id);
        return userStorage.findUserById(id).getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    private int generateUserId() {
        return ++id;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("ERROR: Поле Email не может быть пустым!");
            throw new ValidationException("Email не может быть пустым!");
        }
        if (!user.getEmail().contains("@")) {
            log.error("ERROR: Поле Email должно содержать символ @");
            throw new ValidationException("Email должно содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty()) {
            log.error("ERROR: Поле Login не может быть пустым!");
            throw new ValidationException("Login не может быть пустым!");
        }
        if (user.getLogin().contains(" ")) {
            log.error("ERROR: Поле Login не может содержать пробелы!");
            throw new ValidationException("Login не может содержать пробелы!");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("ERROR: Поле Birthday не может быть в будущем!");
            throw new ValidationException("Birthday не может быть в будущем!");
        }
        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
    private void validateId(int id) {
        if (userStorage.findUserById(id) == null) {
            throw new ObjectNotFoundException("Person's doesn't found!");
        }
    }
}
