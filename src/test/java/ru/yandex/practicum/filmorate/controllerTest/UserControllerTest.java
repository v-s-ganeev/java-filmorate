package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private final UserController userController = new UserController();

    @Test
    void createUserTest(){
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
