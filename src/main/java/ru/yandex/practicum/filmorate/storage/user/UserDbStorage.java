package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private static final String INSERT_USER = "INSERT INTO users(email, login, name, birthday) VALUES (?, ?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String DELETE_USER = "DELETE FROM USERS WHERE id = ?";
    private static final String UPDATE_USER_BY_ID = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ";
    private static final String SELECT_FRIENDSHIP_BY_USER_AND_FRIEND_ID = "SELECT * FROM friendship WHERE user_id = ? and friend_id = ?;";
    private static final String UPDATE_FRIENDSHIP_CONFIRMED_TRUE = "UPDATE friendship SET confirmed = true WHERE user_id = ? AND friend_id = ?";
    private static final String INSERT_FRIENDSHIP = "INSERT INTO friendship(user_id, friend_id, confirmed) VALUES(?, ?, ?)";
    private static final String UPDATE_FRIENDSHIP_CONFIRMED_FALSE = "UPDATE friendship SET confirmed = false WHERE user_id = ? AND friend_id = ?";
    private static final String DELETE_FRIENDSHIP = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
    private static final String SELECT_ALL_FRIENDS = "SELECT * FROM users WHERE id in (SELECT friend_id FROM friendship WHERE user_id = ?)";

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public User addUser(User user) {
        if (user.getId() != null) {
            throw new ValidationException("Поле id не пустое");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("users").usingGeneratedKeyColumns("id");
        Map<String, Object> userInMap = new HashMap<>();
        userInMap.put("email", user.getEmail());
        userInMap.put("login", user.getLogin());
        userInMap.put("name", user.getName());
        userInMap.put("birthday", user.getBirthday());
        return user.setId(simpleJdbcInsert.executeAndReturnKey(userInMap).intValue());
    }

    @Override
    public User editUser(User user) {
        getUser(user.getId());
        jdbcTemplate.update(UPDATE_USER_BY_ID + user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
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
        if (!jdbcTemplate.queryForList(SELECT_FRIENDSHIP_BY_USER_AND_FRIEND_ID, friendId, userId).isEmpty()) {
            jdbcTemplate.update(UPDATE_FRIENDSHIP_CONFIRMED_TRUE, friendId, userId);
            friendshipConfirmed = "true";
        }
        jdbcTemplate.update(INSERT_FRIENDSHIP, userId, friendId, friendshipConfirmed);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        if (!jdbcTemplate.queryForList(SELECT_FRIENDSHIP_BY_USER_AND_FRIEND_ID, friendId, userId).isEmpty()) {
            jdbcTemplate.update(UPDATE_FRIENDSHIP_CONFIRMED_FALSE, friendId, userId);
        }
        jdbcTemplate.update(DELETE_FRIENDSHIP, userId, friendId);
    }

    @Override
    public List<User> getAllFriends(Integer userId) {
        return jdbcTemplate.query(SELECT_ALL_FRIENDS, userRowMapper, userId);

    }
}
