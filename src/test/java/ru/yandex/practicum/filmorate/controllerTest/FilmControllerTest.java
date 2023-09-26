package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    @Autowired
    private FilmController filmController;

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

        Film validatedFilm = filmController.addFilm(film);

        assertAll("Создание пользователя выполняется не правильно: ",
                () -> assertNotNull(validatedFilm.getId(), "Id не был присвоен")
        );
    }
}
