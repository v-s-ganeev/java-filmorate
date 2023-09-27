package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if (film.getId() != null) throw new ValidationException("Поле id не пустое");
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @Override
    public Film editFilm(Film film) {
        films.put(film.getId(), film);
        log.info("Внесены изменения в фильм: {}", film);
        return film;
    }

    @Override
    public boolean deleteFilm(int filmId) {
        films.remove(filmId);
        return true;
    }

    @Override
    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }
}
