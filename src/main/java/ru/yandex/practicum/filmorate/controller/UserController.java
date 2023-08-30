package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private int id = 0;
    private HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers(){
        return users.values();
    }

    @PostMapping
    public User addUser(@RequestBody User user){
        if(user.getId() != null) {
            throw new ValidationException("Поле id не пустое");
        }
        User checkedUser = checkUser(user);
        checkedUser.setId(++id);
        users.put(checkedUser.getId(), checkedUser);
        log.debug("Добавлен новый пользователь: {}", user);
        return users.get(checkedUser.getId());
    }

    @PutMapping
    public User editUser(@RequestBody User user){
        if (users.get(user.getId()) == null) throw new ValidationException("Пользователь с таким id не найден");
        User checkedUser = checkUser(user);
        users.put(checkedUser.getId(), checkedUser);
        log.debug("Внесены изменения в пользователя: {}", user);
        return users.get(checkedUser.getId());
    }

    private User checkUser(User user){
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("email не может быть пустым");
        }
        if(!user.getEmail().contains("@")) throw new ValidationException("Некорректный email");
        if(user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login не может быть пустым");
        }
        if(user.getLogin().contains(" ")) throw new ValidationException("login не может содержать пробелы");
        if(user.getBirthday().isAfter(LocalDate.now())) throw new ValidationException("Некорректная дата рождения");
        if(user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return user;
    }
}
