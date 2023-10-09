package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

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
    public void deleteFilm(int filmId) {
        films.remove(filmId);
    }

    @Override
    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        getFilm(filmId).addLike(userId);
    }

    @Override
    public void deleteLike(Integer userId, Integer filmId) {
        getFilm(filmId).deleteLike(userId);
    }

    @Override
    public void deleteAllLike(Integer filmId) {
        getFilm(filmId).deleteAllLike();
    }

    @Override
    public List<Integer> getLikes(Integer filmId) {
        List<Integer> likes = new ArrayList<>();
        for (Integer userId : getFilm(filmId).getLikes()) {
            likes.add(userId);
        }
        return likes;
    }

    @Override
    public List<Integer> getAllLikes() {
        List<Integer> likes = new ArrayList<>();
        for (Film film : films.values()) {
            for (int i = 1; i < film.getLikes().size(); i++) {
                likes.add(film.getId());
            }
        }
        return likes;
    }
}
