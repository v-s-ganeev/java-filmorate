package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Integer> friends = new HashSet<>();

    public void addFriend(User user) {
        friends.add(user.getId());
    }

    public void deleteFriend(User user) {
        friends.remove(user.getId());
    }
}
