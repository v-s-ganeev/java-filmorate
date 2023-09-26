package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User editUser(User user) {
        if (getUser(user.getId()) == null) throw new NotFoundException("Пользователь с таким id не найден");
        return userStorage.editUser(user);
    }

    public void deleteUsers(int userId) {
        userStorage.deleteUser(userId);
    }

    public User getUser(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) throw new NotFoundException("Пользователь с таким id не найден");
        return userStorage.getUser(userId);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) throw new NotFoundException("Пользователь с таким id не найден");
        if (friend == null) throw new NotFoundException("Друг с таким id не найден");
        if (userId == friendId) throw new ValidationException("Нельзя добавить себя в друзья");
        user.addFriend(friend);
        friend.addFriend(user);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user == null) throw new NotFoundException("Пользователь с таким id не найден");
        if (friend == null) throw new NotFoundException("Друг с таким id не найден");
        user.deleteFriend(friend);
        friend.deleteFriend(user);
    }

    public List<User> getAllFriends(int userId) {
        List<User> friends = new ArrayList<>();
        for (User user : userStorage.getUser(userId).getFriends().values()) {
            friends.add(user);
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        List<User> commonFriends = new ArrayList<>();
        for (User user : userStorage.getUser(userId).getFriends().values()) {
            if (userStorage.getUser(otherUserId).getFriends().values().contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }

}
