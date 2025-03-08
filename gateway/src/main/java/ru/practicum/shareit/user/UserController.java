package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> postuser(@Valid @RequestBody UserDto userDto) {
        log.info("Добавление нового пользователя");
        return userClient.postUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> patchUser(@RequestBody UserDto userDto, @PathVariable long userId) {
        log.info("Изменение пользователя с id {}", userId);
        return userClient.patchUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userClient.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        log.info("Удаление пользователя с id {}", userId);
        userClient.deleteUser(userId);
    }

}
