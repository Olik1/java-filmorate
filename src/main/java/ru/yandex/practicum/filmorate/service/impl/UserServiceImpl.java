package ru.yandex.practicum.filmorate.service.impl;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    //данный класс реализует бизнес-логику хранение, обновление и получение списка Пользоватей
    private static int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        validateUser(user);
        user.setId(generateUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            return user;
        } else {
            log.error("ERROR: ID введен неверно - такого пользователя не существует!");
            throw new ValidationException("Такого пользователя не существует!");
        }

    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private int generateUserId() {
        return ++id;
    }

    private static void validateUser(User user) {
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
        if (user.getBirthday() != null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("ERROR: Поле Birthday не может быть в будущем!");
            throw new ValidationException("Birthday не может быть в будущем!");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
