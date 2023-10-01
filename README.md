# java-filmorate
Template repository for Filmorate project.
![Untitled](https://github.com/v-s-ganeev/java-filmorate/assets/130279093/378496e1-7523-4aea-badf-63f38526c03b)

Получить список всех пользователей:
```sql
SELECT *
FROM users;
```

Получить список всех фильмов:
```sql
SELECT *
FROM films;
```

Получить список всех жанров:
```sql
SELECT *
FROM genre;
```

Получить список всех возрастных ограничений:
```sql
SELECT *
FROM mpa;
```

Получить прользователя по id:
```sql
SELECT *
FROM users
WHERE id = ?;
```

Получить фильм по id:
```sql
SELECT *
FROM films
WHERE id = ?;
```
