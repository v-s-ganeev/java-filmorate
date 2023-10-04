package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User()
                .setId(rs.getInt("id"))
                .setEmail(rs.getString("email"))
                .setLogin(rs.getString("login"))
                .setName(rs.getString("name"))
                .setBirthday(rs.getDate("birthday").toLocalDate());
    }
}
