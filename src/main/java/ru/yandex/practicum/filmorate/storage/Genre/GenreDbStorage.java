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

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public void addGenreInFilm(Integer filmId, Integer genreId) {
        getGenre(genreId);
        jdbcTemplate.update("INSERT INTO films_genre(film_id, genre_id) VALUES(?, ?)", filmId, genreId);
    }

    @Override
    public void deleteAllGenresFromFilm(Integer filmId) {
        jdbcTemplate.update("DELETE FROM films_genre WHERE film_id = ?", filmId);
    }

    @Override
    public List<Genre> getGenresFromFilm(Integer filmId) {
        return jdbcTemplate.query("SELECT * FROM genre WHERE id in (SELECT genre_id FROM films_genre WHERE film_id = ?)", genreRowMapper, filmId);
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
