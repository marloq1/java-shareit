package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User postuser(@Valid @RequestBody User user) {
        return userService.postuser(user);
    }

    @PatchMapping("/{userId}")
    public User patchUser(@RequestBody User user,@PathVariable long userId) {
        return userService.patchUser(user,userId);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }

}
