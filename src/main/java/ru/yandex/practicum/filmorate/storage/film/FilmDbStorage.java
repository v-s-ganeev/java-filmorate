package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final String INSERT_FILM = "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_FILM_BY_ID = "SELECT * FROM films WHERE id = ?";
    private static final String SELECT_ALL_FILMS = "SELECT * FROM films";
    private static final String DELETE_FILM = "DELETE FROM films WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Film addFilm(Film film) {
        if (film.getId() != null) {
            throw new ValidationException("Поле id не пустое");
        }
        jdbcTemplate.update(INSERT_FILM, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return jdbcTemplate.query(SELECT_ALL_FILMS, filmRowMapper)
                .stream()
                .reduce((a, b) -> b)
                .orElse(null);
    }

    @Override
    public Film editFilm(Film film) {
        jdbcTemplate.update("UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = " + film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return film;
    }

    @Override
    public void deleteFilm(int filmId) {
        jdbcTemplate.update(DELETE_FILM, filmId);
    }

    @Override
    public Film getFilm(int filmId) {
        return jdbcTemplate
                .query(SELECT_FILM_BY_ID, fs -> fs.setInt(1, filmId), filmRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return jdbcTemplate.query(SELECT_ALL_FILMS, filmRowMapper);
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) VALUES(?, ?)", userId, filmId);
    }

    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    @Override
    public void deleteAllLike(Integer filmId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?", filmId);
    }

    @Override
    public List<Integer> getLikes(Integer filmId) {
        return jdbcTemplate.query("SELECT user_id FROM likes WHERE film_id = ?", this::mapLike, filmId);
    }

    private Integer mapLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }
}
