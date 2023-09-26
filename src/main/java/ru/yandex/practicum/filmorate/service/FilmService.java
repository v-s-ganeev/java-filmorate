package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film editFilm(Film film) {
        if (filmStorage.getFilm(film.getId()) == null) throw new NotFoundException("Фильм с таким id не найден");
        return filmStorage.editFilm(film);
    }

    public boolean deleteFilm(int filmId) {
        if (filmStorage.getFilm(filmId) == null) throw new NotFoundException("Фильм с таким id не найден");
        return filmStorage.deleteFilm(filmId);
    }

    public Film getFilm(int filmId) {
        if (filmStorage.getFilm(filmId) == null) throw new NotFoundException("Фильм с таким id не найден");
        return filmStorage.getFilm(filmId);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        if (userStorage.getUser(userId) == null) throw new NotFoundException("Пользователь с таким id не найден");
        if (filmStorage.getFilm(filmId) == null) throw new NotFoundException("Фильм с таким id не найден");
        filmStorage.getFilm(filmId).addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (userStorage.getUser(userId) == null) throw new NotFoundException("Пользователь с таким id не найден");
        if (filmStorage.getFilm(filmId) == null) throw new NotFoundException("Фильм с таким id не найден");
        filmStorage.getFilm(filmId).deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> popularFilms = filmStorage.getAllFilms()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
        return popularFilms;
    }

}
