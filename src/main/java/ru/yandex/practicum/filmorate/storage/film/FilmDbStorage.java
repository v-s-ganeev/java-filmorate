package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private static final String INSERT_FILM = "INSERT INTO films(name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_FILM_BY_ID = "SELECT f.id, f.name, f.description, f.release_date, f.duration, m.id AS mpa_id, m.name AS mpa_name FROM films f JOIN mpa m ON f.mpa_id = m.id WHERE f.id = ?";
    private static final String SELECT_ALL_FILMS = "SELECT f.id, f.name, f.description, f.release_date, f.duration, m.id AS mpa_id, m.name AS mpa_name FROM films f JOIN mpa m ON f.mpa_id = m.id";
    private static final String DELETE_FILM = "DELETE FROM films WHERE id = ?";
    private static final String UPDATE_FILM_BY_ID = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ";
    private static final String INSERT_LIKE = "INSERT INTO likes (user_id, film_id) VALUES(?, ?)";
    private static final String DELETE_LIKE = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
    private static final String DELETE_ALL_LIKES = "DELETE FROM likes WHERE film_id = ?";
    private static final String SELECT_ALL_LIKES_BY_FILM_ID = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String SELECT_ALL_LIKES = "SELECT film_id FROM likes";

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public Film addFilm(Film film) {
        if (film.getId() != null) {
            throw new ValidationException("Поле id не пустое");
        }
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("films").usingGeneratedKeyColumns("id");
        Map<String, Object> filmInMap = new HashMap<>();
        filmInMap.put("name", film.getName());
        filmInMap.put("description", film.getDescription());
        filmInMap.put("release_date", film.getReleaseDate());
        filmInMap.put("duration", film.getDuration());
        filmInMap.put("mpa_id", film.getMpa().getId());
        return film.setId(simpleJdbcInsert.executeAndReturnKey(filmInMap).intValue());
    }

    @Override
    public Film editFilm(Film film) {
        jdbcTemplate.update(UPDATE_FILM_BY_ID + film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
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
        jdbcTemplate.update(INSERT_LIKE, userId, filmId);
    }

    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        jdbcTemplate.update(DELETE_LIKE, userId, filmId);
    }

    @Override
    public void deleteAllLike(Integer filmId) {
        jdbcTemplate.update(DELETE_ALL_LIKES, filmId);
    }

    @Override
    public List<Integer> getLikes(Integer filmId) {
        return jdbcTemplate.query(SELECT_ALL_LIKES_BY_FILM_ID, this::mapUserIdFromLike, filmId);
    }

    @Override
    public List<Integer> getAllLikes() {
        return jdbcTemplate.query(SELECT_ALL_LIKES, this::mapFilmIdFromLike);
    }

    private Integer mapUserIdFromLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("user_id");
    }

    private Integer mapFilmIdFromLike(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("film_id");
    }
}
