package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserServiceImpl;
import ru.yandex.practicum.filmorate.service.impl.ValidatationService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests extends FilmorateApplicationTests {

    private static UserController userController;
    private static UserStorage userStorage;
    private static User user1;
    private static User user2;

    @BeforeAll
    public static void init() {
        userStorage = new InMemoryUserStorage();
        userController = new UserController(new UserServiceImpl(userStorage));
    }

    @BeforeEach
    void beforeEach() {
        user1 = User.builder()
                .email("Olga13@yandex.ru")
                .login("Olik13")
                .name("Olga")
                .birthday(LocalDate.of(1989, 1, 24))
                .build();
        user2 = User.builder()
                .email("Konstantin@yandex.ru")
                .login("Arni")
                .name("Kostya")
                .birthday(LocalDate.of(1986, 7, 12))
                .build();
    }

    @Test
    void shouldCreateUserTest() {
        assertEquals(user1, userController.addUser(user1));
        assertNotNull(userController.getUsers());
        assertTrue(userController.getUsers().contains(user1));
    }

    @Test
    void shouldUpdateUserTest() {
        User actual1 = userController.addUser(user1);
        actual1.setId(user1.getId());
        actual1.setEmail("Maria11@yandex.ru");
        actual1.setName("Maria");
        User update = userController.updateUser(actual1);
        assertNotNull(update, "Test error: user doesn't update!");
        assertEquals(user1, update, "Test error: users aren't equals!");
    }

    @Test
    void shouldThrowWrongIdTest() {
        User actual1 = userController.addUser(user1);
        actual1.setId(0);
        Exception exception = assertThrows(ObjectNotFoundException.class, () -> userController.updateUser(user1));
        assertEquals("Такого пользователя не существует!", exception.getMessage());
    }

    @Test
    void shouldThrowEmailIsEmptyTest() {
        user1.setEmail("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(user1));
        String expectedMessage = "Email не может быть пустым!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowEmailWithNoSimbolsTest() {
        user1.setEmail("Olga");
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(user1));
        String expectedMessage = "Email должно содержать символ @";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowLoginEmptyTest() {
        user1.setLogin("");
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(user1));
        String expectedMessage = "Login не может быть пустым!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowLoginWithSpaceTest() {
        user1.setLogin("Olga the best!");
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(user1));
        String expectedMessage = "Login не может содержать пробелы!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowBirthdayInFutureTest() {
        user1.setBirthday(LocalDate.of(2025, 1, 22));
        Exception exception = assertThrows(ValidationException.class, () -> userController.addUser(user1));
        String expectedMessage = "Birthday не может быть в будущем!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }


}