package ru.yandex.practicum.filmorate.controllerTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void createUserTest() {
        User user = User.builder()
                .login("ТестЛогин")
                .email("@")
                .birthday(LocalDate.now())
                .build();

        User validatedUser = userController.addUser(user);

        assertAll("Создание пользователя выполняется не правильно: ",
                () -> assertNotNull(validatedUser.getId(), "Id не был присвоен"),
                () -> assertEquals(validatedUser.getLogin(), validatedUser.getName(), "Имя было присвоено не правильно")
        );
    }
}
