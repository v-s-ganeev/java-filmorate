package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    private UserStorage userStorage = new InMemoryUserStorage();

    @Test
    void createUserTest() {
        User user = User.builder()
                .login("ТестЛогин")
                .email("@")
                .birthday(LocalDate.now())
                .build();

        User validatedUser = userStorage.addUser(user);

        assertNotNull(validatedUser.getId(), "Id не был присвоен");
    }
}
