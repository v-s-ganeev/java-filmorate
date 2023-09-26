package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Film editFilm(Film film);

    boolean deleteFilm(int filmId);

    Film getFilm(int filmId);

    Collection<Film> getAllFilms();

}
