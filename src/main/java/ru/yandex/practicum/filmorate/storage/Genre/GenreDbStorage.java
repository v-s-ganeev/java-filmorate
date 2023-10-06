package ru.yandex.practicum.filmorate.storage.Genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genre WHERE id = ?";
    private static final String SELECT_ALL_GENRE = "SELECT * FROM genre";
    private static final String INSERT_FILMS_GENRE = "INSERT INTO films_genre(film_id, genre_id) VALUES(?, ?)";
    private static final String DELETE_FILM_GENRE_FROM_FILM = "DELETE FROM films_genre WHERE film_id = ?";
    private static final String SELECT_GENRES_FROM_FILMS = "SELECT * FROM genre WHERE id in (SELECT genre_id FROM films_genre WHERE film_id = ?)";

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public void addGenreInFilm(Integer filmId, Integer genreId) {
        getGenre(genreId);
        jdbcTemplate.update(INSERT_FILMS_GENRE, filmId, genreId);
    }

    @Override
    public void deleteAllGenresFromFilm(Integer filmId) {
        jdbcTemplate.update(DELETE_FILM_GENRE_FROM_FILM, filmId);
    }

    @Override
    public List<Genre> getGenresFromFilm(Integer filmId) {
        return jdbcTemplate.query(SELECT_GENRES_FROM_FILMS, genreRowMapper, filmId);
    }

    @Override
    public Genre getGenre(Integer genreId) {
        Genre genre = jdbcTemplate
                .query(SELECT_GENRE_BY_ID, gs -> gs.setInt(1, genreId), genreRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        if (genre == null) throw new NotFoundException("Жанр с id = " + genreId + " не найден");
        return genre;
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query(SELECT_ALL_GENRE, genreRowMapper);
    }
}
