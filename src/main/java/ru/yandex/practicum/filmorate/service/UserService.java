package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
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

    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(checkUser(user));
    }

    public User editUser(User user) {
        return userStorage.editUser(checkUser(user));
    }

    public void deleteUsers(int userId) {
        userStorage.deleteUser(userId);
    }

    public User getUser(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        return userStorage.getUser(userId);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (friend == null) throw new NotFoundException("Друг с id = " + friendId + " не найден");
        if (userId == friendId) throw new ValidationException("Нельзя добавить себя в друзья");
        user.addFriend(friend);
        friend.addFriend(user);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (friend == null) throw new NotFoundException("Друг с id = " + friendId + " не найден");
        user.deleteFriend(friend);
        friend.deleteFriend(user);
    }

    public List<User> getAllFriends(int userId) {
        if (getUser(userId) == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.getUser(userId).getFriends()) {
            friends.add(getUser(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        if (getUser(userId) == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        if (getUser(otherUserId) == null)
            throw new NotFoundException("Пользователь с id = " + otherUserId + " не найден");
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendId : userStorage.getUser(userId).getFriends()) {
            if (userStorage.getUser(otherUserId).getFriends().contains(friendId)) {
                commonFriends.add(getUser(friendId));
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
