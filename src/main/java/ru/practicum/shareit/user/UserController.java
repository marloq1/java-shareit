package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User postuser(@Valid @RequestBody User user) {
        log.info("Добавление нового пользователя");
        return userService.postuser(user);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@RequestBody User user,@PathVariable long userId) {
        log.info("Изменение пользователя с id {}", userId);
        return userService.patchUser(user,userId);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userService.getUser(userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userService.deleteUser(userId);
    }

}
