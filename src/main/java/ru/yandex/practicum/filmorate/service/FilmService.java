package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;
    private final GenreService genreService;

    public Film addFilm(Film film) {
        checkFilm(film);
        Film filmFromDb = filmStorage.addFilm(film);
        for (Genre genre : film.getGenres()) {
            genreService.addGenreInFilm(filmFromDb.getId(), genre.getId());
            filmFromDb.addGenre(genre);
        }
        return filmFromDb;
    }

    public Film editFilm(Film film) {
        if (!isMovieInDb(film.getId())) throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        checkFilm(film);
        filmStorage.editFilm(film);
        genreService.deleteAllGenresFromFilm(film.getId());
        for (Genre genre : film.getGenres()) {
            genreService.addGenreInFilm(film.getId(), genre.getId());
        }
        return getFilm(film.getId());
    }

    public void deleteFilm(int filmId) {
        if (!isMovieInDb(filmId)) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        genreService.deleteAllGenresFromFilm(filmId);
        filmStorage.deleteFilm(filmId);
        filmStorage.deleteAllLike(filmId);
    }

    public Film getFilm(int filmId) {
        Film filmFromDb = filmStorage.getFilm(filmId);
        if (filmFromDb == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        for (Genre genre : genreService.getGenresFromFilm(filmId)) {
            filmFromDb.addGenre(genre);
        }
        return filmFromDb;
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> allFilms = filmStorage.getAllFilms();
        for (Film film : allFilms) {
            for (Genre genre : genreService.getGenresFromFilm(film.getId())) {
                film.addGenre(genre);
            }
        }
        return allFilms;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (!isMovieInDb(filmId)) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        userStorage.getUser(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Integer> getLikes(Integer filmId) {
        return filmStorage.getLikes(filmId);
    }

    public List<Integer> getAllLikes() {
        return filmStorage.getAllLikes();
    }

    public List<Film> getPopularFilms(int count) {
        List<Integer> likes = getAllLikes();
        return filmStorage.getAllFilms()
                .stream()
                .sorted((film1, film2) -> getLikesCount(film1.getId(), likes) - getLikesCount(film2.getId(), likes))
                .limit(count)
                .collect(Collectors.toList());
    }

    private boolean isMovieInDb(Integer filmId) {
        return filmStorage.getFilm(filmId) != null;
    }

    private Integer getLikesCount(Integer filmId, List<Integer> likes) {
        Long likesCount = likes.stream().filter(like -> like == filmId).count();
        return likesCount.intValue();

    }

    private void checkFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата релиза");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

}
