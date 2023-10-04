package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private static final String INSERT_USER = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String DELETE_USER = "DELETE FROM USERS WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User addUser(User user) {
        if (user.getId() != null) {
            throw new ValidationException("Поле id не пустое");
        }
        jdbcTemplate.update(INSERT_USER, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return jdbcTemplate.query(SELECT_ALL_USERS, userRowMapper)
                .stream()
                .reduce((a, b) -> b)
                .orElse(null);
    }

    @Override
    public User editUser(User user) {
        jdbcTemplate.update("UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = " + user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return getUser(user.getId());
    }

    @Override
    public void deleteUser(int userId) {
        getUser(userId);
        jdbcTemplate.update(DELETE_USER, userId);
    }

    @Override
    public User getUser(int userId) {
        User user = jdbcTemplate
                .query(SELECT_USER_BY_ID, ps -> ps.setInt(1, userId), userRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        if (user == null) throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        return jdbcTemplate.query(SELECT_ALL_USERS, userRowMapper);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        getUser(userId);
        getUser(friendId);
        String friendshipConfirmed = "false";
        if (!jdbcTemplate.queryForList("SELECT * FROM friendship WHERE user_id = ? and friend_id = ?;", friendId, userId).isEmpty()) {
            jdbcTemplate.update("UPDATE friendship SET confirmed = true WHERE user_id = ? AND friend_id = ?", friendId, userId);
            friendshipConfirmed = "true";
        }
        jdbcTemplate.update("INSERT INTO friendship(user_id, friend_id, confirmed) VALUES(?, ?, ?)", userId, friendId, friendshipConfirmed);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        if (!jdbcTemplate.queryForList("SELECT * FROM friendship WHERE user_id = ? and friend_id = ?;", friendId, userId).isEmpty()) {
            jdbcTemplate.update("UPDATE friendship SET confirmed = false WHERE user_id = ? AND friend_id = ?", friendId, userId);
        }
        jdbcTemplate.update("DELETE FROM friendship WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id in (SELECT friend_id FROM friendship WHERE user_id = ?)", userRowMapper, userId);

    }
}
