package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    private FilmStorage filmStorage = new InMemoryFilmStorage();

    @Test
    void createFilmTest() {
        Film film = Film.builder()
                .name("Тест Имя")
                .description("Тест Описания на 200 символов.\n" +
                        "Уж сколько раз твердили миру,\n" +
                        "Что лесть гнусна, вредна; но только все не впрок,\n" +
                        "И в сердце льстец всегда отыщет уголок.\n" +
                        "Вороне где-то бог послал кусочек сыру;\n" +
                        "На ель вор")
                .releaseDate(LocalDate.of(1895, 12, 28))
                .duration(1)
                .build();

        Film validatedFilm = filmStorage.addFilm(film);

        assertAll("Создание пользователя выполняется не правильно: ",
                () -> assertNotNull(validatedFilm.getId(), "Id не был присвоен")
        );
    }
}
