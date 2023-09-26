package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
        User checkedUser = checkUser(user);
        checkedUser.setId(++id);
        users.put(checkedUser.getId(), checkedUser);
        log.debug("Добавлен новый пользователь: {}", user);
        return checkedUser;
    }

    @Override
    public User editUser(User user) {
        if (users.get(user.getId()) == null) throw new ValidationException("Пользователь с таким id не найден");
        User checkedUser = checkUser(user);
        users.put(checkedUser.getId(), checkedUser);
        log.debug("Внесены изменения в пользователя: {}", user);
        return checkedUser;
    }

    @Override
    public void deleteUser(int userId) {
        if (users.get(userId) == null) throw new ValidationException("Фильм с таким id не найден");
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

    private User checkUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("email не может быть пустым");
        }
        if (!user.getEmail().contains("@")) throw new ValidationException("Некорректный email");
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("login не может быть пустым");
        }
        if (user.getLogin().contains(" ")) throw new ValidationException("login не может содержать пробелы");
        if (user.getBirthday().isAfter(LocalDate.now())) throw new ValidationException("Некорректная дата рождения");
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
        return user;
    }
}