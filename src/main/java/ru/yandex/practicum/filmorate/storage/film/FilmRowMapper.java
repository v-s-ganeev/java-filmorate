package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    private final MpaStorage mpaStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film()
                .setId(rs.getInt("id"))
                .setName(rs.getString("name"))
                .setDescription(rs.getString("description"))
                .setReleaseDate(rs.getDate("release_date").toLocalDate())
                .setDuration(rs.getInt("duration"))
                .setMpa(mpaStorage.getMpa(rs.getInt("mpa_id")));
    }
}
