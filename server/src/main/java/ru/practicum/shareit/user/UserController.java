package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto postuser(@RequestBody UserDto userDto) {
        log.info("Добавление нового пользователя");
        return userService.postuser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        log.info("Изменение пользователя с id {}", userId);
        return userService.patchUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userService.getUser(userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userService.deleteUser(userId);
    }

}
