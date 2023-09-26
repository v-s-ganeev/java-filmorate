package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return userService.getUser(Integer.valueOf(userId));
    }

    @GetMapping("/{userId}/friends")
    public List<User> getAllFriends(@PathVariable String userId) {
        return userService.getAllFriends(Integer.valueOf(userId));
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public Collection<User> getCommonFriends(@PathVariable String userId, @PathVariable String otherUserId) {
        return userService.getCommonFriends(Integer.valueOf(userId), Integer.valueOf(otherUserId));
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User editUser(@RequestBody User user) {
        return userService.editUser(user);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable String userId, @PathVariable String friendId) {
        userService.addFriend(Integer.valueOf(userId), Integer.valueOf(friendId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable String userId, @PathVariable String friendId) {
        userService.deleteFriend(Integer.valueOf(userId), Integer.valueOf(friendId));
    }


}
