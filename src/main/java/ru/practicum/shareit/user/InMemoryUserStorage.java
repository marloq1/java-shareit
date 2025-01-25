package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.ConflictException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {


    private final Map<Long,User> users = new HashMap<>();

    @Override
    public User postuser(User user) {
        if (!users.containsValue(user)) {
            user.setId(getNextId());
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            throw new ConflictException("Пользователь с таким email уже есть");
        }
    }

    @Override
    public User patchUser(User user) {
        User userFromMemory = users.get(user.getId());
        users.remove(user.getId());
        if (!users.containsValue(user)) {
            users.put(user.getId(), user);
            return users.get(user.getId());
        } else {
            users.put(user.getId(),userFromMemory);
            throw new ConflictException("Пользователь с таким email уже есть");
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User deleteUser(long userId) {
        return users.remove(userId);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
