package ru.yandex.practicum.filmorate.storage.Genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenre(Integer id);

    List<Genre> getAllGenre();

    void addGenreInFilm(Integer filmId, Integer genreId);

    void deleteAllGenresFromFilm(Integer filmId);

    List<Genre> getGenresFromFilm(Integer filmId);
}
