package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User editUser(User user);

    void deleteUser(int userId);

    User getUser(int userId);

    Collection<User> getAllUsers();
}
