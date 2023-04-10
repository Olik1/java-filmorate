package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmTests extends FilmorateApplicationTests {

    private static FilmController filmController;
    private static FilmStorage filmStorage;
    private static UserStorage userStorage;
    private static Film film1;
    private static Film film2;

    @BeforeAll
    public static void init() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmController = new FilmController(new FilmServiceImpl(filmStorage, userStorage));
    }

    @BeforeEach
    void BeforeEach() {
        film1 = Film.builder()
                .name("Архив Ланъя")
                .description("Историческая дорама")
                .duration(2430)
                .releaseDate(LocalDate.of(2015, 9, 19))
                .build();
        film2 = Film.builder()
                .name("Терминатор")
                .description("боевик")
                .duration(120)
                .releaseDate(LocalDate.of(1984, 1, 1))
                .build();
    }


    @Test
    void shouldCreateFilmTest() {
        assertEquals(film1, filmController.addFilm(film1));
        assertNotNull(filmController.getFilms());
        assertTrue(filmController.getFilms().contains(film1));
    }

    @Test
    void shouldUpdateFilmTest() {
        Film expected1 = film1;
        Film actual1 = filmController.addFilm(expected1);
        actual1.setId(film1.getId());
        actual1.setReleaseDate(LocalDate.of(2000, 1, 1));
        actual1.setName("Мария, просто Мария");
        Film update = filmController.updateFilm(actual1);
        assertNotNull(update, "Test error: film doesn't update!");
        assertEquals(actual1, update, "Test error: films aren't equals!");
    }
    @Test
    void shouldThrowNameIsEmptyTest() {
        film1.setName("");
        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        String expectedMessage = "Name не может быть пустым!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void shouldThrowDescriptionMoreThan200Test() {
        film1.setDescription("История разворачивается вокруг заговора против старшего сына императора и семьи Линь, " +
                "глава которой командует армией Великой Лян. Они обвинены в измене и приговорены к смерти. " +
                "Линь Шу, девятнадцатилетний командующий одного из отрядов, выживает и скрывается в Цзянху, мире бойцов. " +
                "Приняв личность ученого Мэй Чансу, он основывает союз Цзянцзо и устраивает свои дела вдали от двора, " +
                "готовясь отомстить. Сильно подорванное здоровье заставляет его отказаться от физических методов " +
                "и заняться деятельностью стратега. Двенадцать лет спустя он возвращается в столицу " +
                "под вымышленным именем, чтобы вмешаться в борьбу сыновей императора за престол. " +
                "Он принимает решение поддержать принца Цзина, когда-то своего лучшего друга, " +
                "который не участвует в политических делах. Принц Цзин не верит в обвинения против семьи Линь " +
                "и хочет добиться оправдания, но из-за резких речей и поступков он считается " +
                "опальным принцем и практически не имеет власти. " +
                "Между ним и троном стоят старшие братья — наследный принц, сын фаворитки императора, " +
                "и принц Юй, выращенный императрицей. Давние враги семьи Линь, подготовившие заговор, " +
                "занимают высокие посты и вмешиваются в придворные игры, а внешнеполитическая обстановка " +
                "медленно, но верно накаляется. Цель Мэй Чансу — добиться справедливости, отомстить и " +
                "остаться неузнанным, но до её исполнения нужно преодолеть много препятствий.");
        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertEquals("MAX длина описания — 200 символов!", exception.getMessage());
    }
    @Test
    void shouldThrowReleaseDateTest() {
        film1.setReleaseDate(LocalDate.of(1800, 1, 1));
        Exception exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
        assertEquals("Дата релиза не может быть раньше 1895-12-28", exception.getMessage());
    }
    @Test
    void shouldThrowDurationNegativeTest() {
        film1.setDuration(-99);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film1));
    }

}