package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "spring.config.name = application-test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final UserService userService;
    private UserDto userDto1;
    private final EntityManager entityManager;

    @BeforeEach
    void setUp() {
        userDto1 = makeUserDto("Fedor", "1@mail.ru");
    }

    @Test
    void testPostUser() {
        UserDto userFromDb = userService.postuser(userDto1);
        assertThat(userFromDb.getName(), equalTo(userDto1.getName()));
        assertThat(userFromDb.getEmail(), equalTo(userDto1.getEmail()));
    }

    @Test
    void testPostSameEmailsUsers() {
        userService.postuser(userDto1);
        assertThrows(DataIntegrityViolationException.class, () -> userService
                .postuser(makeUserDto("Petr", "1@mail.ru")));
    }

    @Test
    void testPostUserWithoutEmail() {
        assertThrows(DataIntegrityViolationException.class, () -> userService
                .postuser(makeUserDto("Petr", null)));
    }

    @Test
    void testPostUserWithoutname() {
        assertThrows(DataIntegrityViolationException.class, () -> userService
                .postuser(makeUserDto(null, "1@mail.ru")));
    }

    @Test
    void testPatchUserName() {
        UserDto userFromDb1 = userService.postuser(userDto1);
        UserDto userFromDb2 = userService.patchUser(makeUserDto("Petr", null), userFromDb1.getId());
        assertThat(userFromDb1.getId(), equalTo(userFromDb2.getId()));
        assertThat(userFromDb1.getEmail(), equalTo(userFromDb2.getEmail()));
        assertThat(userFromDb2.getName(), equalTo("Petr"));
    }

    @Test
    void testPatchUserEmail() {
        UserDto userFromDb1 = userService.postuser(userDto1);
        UserDto userFromDb2 = userService.patchUser(makeUserDto(null, "2@mail.ru"), userFromDb1.getId());
        assertThat(userFromDb1.getId(), equalTo(userFromDb2.getId()));
        assertThat(userFromDb1.getName(), equalTo(userFromDb2.getName()));
        assertThat(userFromDb2.getEmail(), equalTo("2@mail.ru"));
    }

    @Test
    void testPatchInvalidIdUser() {
        assertThrows(NotFoundException.class, () -> userService
                .patchUser(makeUserDto(null, "2@mail.ru"), 999L));
    }

    @Test
    void testGetUser() {
        UserDto userFromDb1 = userService.postuser(userDto1);
        UserDto userFromDb2 = userService.getUser(userFromDb1.getId());
        assertThat(userFromDb1.getId(), equalTo(userFromDb2.getId()));
        assertThat(userFromDb1.getName(), equalTo(userFromDb2.getName()));
        assertThat(userFromDb1.getEmail(), equalTo(userFromDb2.getEmail()));
    }

    @Test
    void testDeleteUser() {
        UserDto userFromDb1 = userService.postuser(userDto1);
        userService.deleteUser(userFromDb1.getId());
        assertThrows(NotFoundException.class, () -> userService.getUser(userFromDb1.getId()));
    }

    @Test
    void testDeleteInvalidUser() {
        assertThrows(NotFoundException.class, () -> userService.deleteUser(999L));
    }


    private UserDto makeUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }

}