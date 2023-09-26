package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    @ToString.Exclude
    private Map<Integer, User> friends = new HashMap<>();

    public void addFriend(User user) {
        friends.put(user.getId(), user);
    }

    public void deleteFriend(User user) {
        friends.remove(user.getId());
    }
}
