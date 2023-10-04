package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserDbStorage userDbStorage;
    private final FilmDbStorage filmDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final MpaDbStorage mpaDbStorage;

    Mpa mpaMain = Mpa.builder()
            .id(1)
            .name("PG")
            .build();

    User userMain = User.builder()
            .email("Main@yandex.ru")
            .login("Main")
            .name("Main")
            .birthday(LocalDate.now())
            .build();

    User userUpdate = User.builder()
            .id(1)
            .email("Update@yandex.ru")
            .login("Update")
            .name("Update")
            .birthday(LocalDate.now().plusDays(1))
            .build();

    User userFriend = User.builder()
            .email("Friend@yandex.ru")
            .login("Friend")
            .name("Friend")
            .birthday(LocalDate.now().plusDays(2))
            .build();

    User userDelete = User.builder()
            .email("Delete@yandex.ru")
            .login("Delete")
            .name("Delete")
            .birthday(LocalDate.now().plusDays(3))
            .build();

    Film filmMain = Film.builder()
            .name("Main")
            .description("Main")
            .releaseDate(LocalDate.now())
            .duration(10)
            .mpa(mpaMain)
            .build();

    Film filmUpdate = Film.builder()
            .id(1)
            .name("Update")
            .description("Update")
            .releaseDate(LocalDate.now().plusDays(2))
            .duration(20)
            .mpa(mpaMain)
            .build();

    Film filmDelete = Film.builder()
            .name("Delete")
            .description("Delete")
            .releaseDate(LocalDate.now().plusDays(3))
            .duration(30)
            .mpa(mpaMain)
            .build();


    @Test
    public void fullDbStorageTest() {
        User userMainDb = userDbStorage.addUser(userMain);
        User userDeleteDb = userDbStorage.addUser(userDelete);
        User userFriendDb = userDbStorage.addUser(userFriend);
        User userUpdateDb = userDbStorage.editUser(userUpdate);
        User userUpdateCopyDb = userDbStorage.getUser(1);
        Collection<User> users3 = userDbStorage.getAllUsers();
        userDbStorage.addFriend(userUpdateDb.getId(), userFriendDb.getId());
        userDbStorage.addFriend(userUpdateDb.getId(), userDeleteDb.getId());
        List<User> friends2 = userDbStorage.getAllFriends(userUpdateDb.getId());
        userDbStorage.deleteFriend(userUpdateDb.getId(), userFriendDb.getId());
        List<User> friends1 = userDbStorage.getAllFriends(userUpdateDb.getId());
        userDbStorage.deleteUser(userDeleteDb.getId());
        Collection<User> users2 = userDbStorage.getAllUsers();

        assertAll("UserDbStorage работает не правильно: ",
                () -> assertNotNull(userMainDb, "Пользователи создаются не правильно"),
                () -> assertEquals(userMainDb.getId(), 1, "Id присваевается не правильно"),
                () -> assertEquals(userMainDb.getId(), userUpdateDb.getId(), "Пользователи обновляются не правильно"),
                () -> assertEquals(userUpdateDb.toString(), userUpdateCopyDb.toString(), "Получение пользователя по id происходит не правильно"),
                () -> assertEquals(users3.size(), 3, "Получение всех пользователей работает не правильно"),
                () -> assertEquals(friends2.size(), 2, "Добавление в друзья работает не правильно"),
                () -> assertEquals(friends1.size(), 1, "Удаление друга работает не правильно"),
                () -> assertEquals(users2.size(), 2, "Удаление пользователя работает не правильно")
        );

        Film filmMainDb = filmDbStorage.addFilm(filmMain);
        Film filmDeleteDb = filmDbStorage.addFilm(filmDelete);
        Film filmUpdateDb = filmDbStorage.editFilm(filmUpdate);
        Film filmUpdateCopyDb = filmDbStorage.getFilm(1);
        Collection<Film> films2 = filmDbStorage.getAllFilms();
        filmDbStorage.addLike(userUpdateDb.getId(), filmUpdateDb.getId());
        filmDbStorage.addLike(userDeleteDb.getId(), filmUpdateDb.getId());
        List<Integer> likes2 = filmDbStorage.getLikes(filmUpdateDb.getId());
        filmDbStorage.deleteLike(userUpdateDb.getId(), filmUpdateDb.getId());
        List<Integer> likes1 = filmDbStorage.getLikes(filmUpdateDb.getId());
        filmDbStorage.deleteAllLike(filmUpdateDb.getId());
        List<Integer> likes0 = filmDbStorage.getLikes(filmUpdateDb.getId());
        filmDbStorage.deleteFilm(filmDeleteDb.getId());
        Collection<Film> films1 = filmDbStorage.getAllFilms();

        assertAll("FilmDbStorage работает не правильно: ",
                () -> assertNotNull(filmMainDb, "Фильмы создаются не правильно"),
                () -> assertEquals(filmMainDb.getId(), 1, "Id присваевается не правильно"),
                () -> assertEquals(filmMainDb.getId(), filmUpdateDb.getId(), "Фильмы обновляются не правильно"),
                () -> assertEquals(filmUpdateDb.getName(), filmUpdateCopyDb.getName(), "Получение фильма по id происходит не правильно"),
                () -> assertEquals(films2.size(), 2, "Получение всех фильмов работает не правильно"),
                () -> assertEquals(likes2.size(), 2, "Добавление лайка работает не правильно"),
                () -> assertEquals(likes1.size(), 1, "Удаление лайка работает не правильно"),
                () -> assertEquals(likes0.size(), 0, "Удаление всех лайков работает не правильно"),
                () -> assertEquals(films1.size(), 1, "Удаление фильма работает не правильно")
        );

        Genre genre1 = genreDbStorage.getGenre(1);
        List<Genre> genres6 = genreDbStorage.getAllGenre();
        genreDbStorage.addGenreInFilm(filmUpdateDb.getId(), 1);
        List<Genre> genresFromFilm1 = genreDbStorage.getGenresFromFilm(filmUpdateDb.getId());
        genreDbStorage.deleteAllGenresFromFilm(filmUpdateDb.getId());
        List<Genre> genresFromFilm0 = genreDbStorage.getGenresFromFilm(filmUpdateDb.getId());

        assertAll("GenreDbStorage работает не правильно: ",
                () -> assertNotNull(genre1, "Получение жарна по id работает не правильно"),
                () -> assertEquals(genres6.size(), 6, "Получение всех жанров работает не правильно"),
                () -> assertEquals(genresFromFilm1.size(), 1, "Добавление фильму жанра работает не правильно"),
                () -> assertEquals(genresFromFilm0.size(), 0, "Удаление всех жанров работает не правильно")
        );

        Mpa mpa1 = mpaDbStorage.getMpa(1);
        List<Mpa> allMpa5 = mpaDbStorage.getAllMpa();

        assertAll("MpaDbStorage работает не правильно: ",
                () -> assertNotNull(mpa1, "Получение жарна по id работает не правильно"),
                () -> assertEquals(allMpa5.size(), 5, "Получение всех жанров работает не правильно")
        );
    }
}
