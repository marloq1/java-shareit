package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto postuser(UserDto userDto);

    UserDto patchUser(UserDto userDto,Long userId);

    UserDto getUser(long userId);

    void deleteUser(long userId);
}
