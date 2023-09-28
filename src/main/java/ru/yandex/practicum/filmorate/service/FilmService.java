package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        checkFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film editFilm(Film film) {
        getFilm(film.getId());
        checkFilm(film);
        return filmStorage.editFilm(film);
    }

    public boolean deleteFilm(int filmId) {
        if (filmStorage.getFilm(filmId) == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        return filmStorage.deleteFilm(filmId);
    }

    public Film getFilm(int filmId) {
        if (filmStorage.getFilm(filmId) == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");
        return filmStorage.getFilm(filmId);
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(int filmId, int userId) {
        if (userStorage.getUser(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        getFilm(filmId).addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (userStorage.getUser(userId) == null)
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        getFilm(filmId).deleteLike(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms()
                .stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
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
