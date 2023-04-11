package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private static int id;

    @Autowired

    public InMemoryUserStorage() {
        User user = User.builder()
                .email("Olga13@yandex.ru")
                .login("Olik13")
                .name("Olga")
                .birthday(LocalDate.of(1989, 1, 24))
                .build();
        addUser(user);
        User user1 = User.builder()
                .email("Konstantin@yandex.ru")
                .login("Arni")
                .name("Kostya")
                .birthday(LocalDate.of(1986, 7, 12))
                .build();
        addUser(user1);
        user.addFriend(user1.getId());
        user1.addFriend(user.getId());
    }


    @Override
    public User save(User user) {
        return users.put(user.getId(), user);
    }

    @Override
    public User addUser(User user) {
        user.setId(generateUserId());
        return users.put(user.getId(), user);
    }

    @Override
    public User findUserById(int id) {
        if (!users.containsKey(id)) {
            log.error("ERROR: ID введен неверно - такого пользователя не существует!");
            throw new ObjectNotFoundException(String.format("User's id %d doesn't found!", id));
        }
        return users.get(id);
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
    }

    @Override
    public Set<Integer> getAllId() {
        return users.keySet();
    }

    private int generateUserId() {
        return ++id;
    }
}
