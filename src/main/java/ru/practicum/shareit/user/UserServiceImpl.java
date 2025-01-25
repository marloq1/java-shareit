package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User postuser(User user) {
        return userStorage.postuser(user);
    }

    @Override
    public User patchUser(User user, Long userId) {
        user.setId(userId);
        User userFromMemory = userStorage.getUsers().stream().filter(us -> us.getId() == userId)
                .findAny().orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        if (user.getName() == null) {
            user.setName(userFromMemory.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userFromMemory.getEmail());
        }
        return userStorage.patchUser(user);
    }

    @Override
    public User getUser(long userId) {
        return userStorage.getUsers().stream().filter(user -> user.getId() == userId).findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
    }

    @Override
    public void deleteUser(long userId) {
        userStorage.getUsers().stream().filter(user -> user.getId() == userId).findAny()
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        userStorage.deleteUser(userId);
    }
}
