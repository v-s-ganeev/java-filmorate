package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (user.getId() != null) {
            throw new ValidationException("Поле id не пустое");
        }
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь: {}", user);
        return user;
    }

    @Override
    public User editUser(User user) {
        if (users.get(user.getId()) == null)
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        users.put(user.getId(), user);
        log.info("Внесены изменения в пользователя: {}", user);
        return user;
    }

    @Override
    public void deleteUser(int userId) {
        if (users.get(userId) == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        users.remove(userId);
    }

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }
}