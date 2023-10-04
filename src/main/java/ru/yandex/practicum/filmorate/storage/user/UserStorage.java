package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User editUser(User user);

    void deleteUser(int userId);

    User getUser(int userId);

    Collection<User> getAllUsers();

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getAllFriends(Integer userId);
}
