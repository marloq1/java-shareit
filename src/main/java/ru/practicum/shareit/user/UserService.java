package ru.practicum.shareit.user;

public interface UserService {

    User postuser(User user);

    User patchUser(User user,Long userId);

    User getUser(long userId);

    void deleteUser(long userId);
}
