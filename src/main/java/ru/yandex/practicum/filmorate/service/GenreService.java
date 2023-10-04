package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Genre getGenre(Integer id) {
        return genreStorage.getGenre(id);
    }

    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }

    public void addGenreInFilm(Integer filmId, Integer genreId) {
        genreStorage.addGenreInFilm(filmId, genreId);
    }

    public void deleteAllGenresFromFilm(Integer filmId) {
        genreStorage.deleteAllGenresFromFilm(filmId);
    }

    public List<Genre> getGenresFromFilm(Integer filmId) {
        return genreStorage.getGenresFromFilm(filmId);
    }


}
