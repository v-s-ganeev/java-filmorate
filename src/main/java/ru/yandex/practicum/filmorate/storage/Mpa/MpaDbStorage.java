package ru.yandex.practicum.filmorate.storage.Mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Mpa getMpa(Integer mpaId) {
        Mpa mpa = jdbcTemplate
                .query("SELECT * FROM mpa WHERE id = ?", ms -> ms.setInt(1, mpaId), mpaRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
        if (mpa == null) throw new NotFoundException("MPA с id = " + mpaId + " не найдено");
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa", mpaRowMapper);
    }
}
