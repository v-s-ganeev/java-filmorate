package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(checkUser(user));
    }

    public User editUser(User user) {
        return userStorage.editUser(checkUser(user));
    }

    public void deleteUser(int userId) {
        userStorage.deleteUser(userId);
    }

    public User getUser(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    public List<User> getAllFriends(int userId) {
        return userStorage.getAllFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        getUser(userId);
        getUser(otherUserId);
        List<User> otherUserFriends = getAllFriends(otherUserId);
        List<User> commonFriends = new ArrayList<>();
        for (User friend : getAllFriends(userId)) {
            if (otherUserFriends.contains(friend)) {
                commonFriends.add(friend);
            }
        }
        return commonFriends;
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
