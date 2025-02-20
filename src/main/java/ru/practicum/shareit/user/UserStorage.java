package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User postuser(User user);

    User patchUser(User user);

    User deleteUser(long userId);

    Collection<User> getUsers();

}
