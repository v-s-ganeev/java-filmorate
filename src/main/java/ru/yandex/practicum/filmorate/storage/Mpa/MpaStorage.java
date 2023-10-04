package ru.yandex.practicum.filmorate.storage.Mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    Mpa getMpa(Integer id);

    List<Mpa> getAllMpa();
}
