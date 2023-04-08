package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private int id;
    @NotBlank
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^[^ ]+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private final Set<Integer> friends = new HashSet<>();

    public Set<Integer> getFriends() {
        return friends;
    }
    public void addFriend(int friendId) {
        friends.add(friendId);
    }
    public boolean deleteFriend(int friendId) {
       return friends.remove(friendId);
    }
}
