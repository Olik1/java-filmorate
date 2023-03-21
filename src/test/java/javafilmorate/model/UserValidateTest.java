package javafilmorate.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserValidateTest {

    @Autowired
    private UserService userService;

    @Test
    public void createUserTest() {
        User actualUser = new User();
        actualUser.setEmail("Olik@yandex.ru");
        actualUser.setLogin("Olik13");
        actualUser.setBirthday(LocalDate.of(1989, 1, 24));

        userService.createUser(actualUser);
        assertEquals(1, userService.getAllUsers().size());
    }


}
