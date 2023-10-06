package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    Film editFilm(Film film);

    void deleteFilm(int filmId);

    Film getFilm(int filmId);

    Collection<Film> getAllFilms();

    void addLike(Integer userId, Integer filmId);

    void deleteLike(Integer userId, Integer filmId);

    void deleteAllLike(Integer filmId);

    List<Integer> getLikes(Integer filmId);

    List<Integer> getAllLikes();

}
