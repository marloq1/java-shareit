package ru.practicum.shareit.request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "spring.config.name = application-test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplIntegrationTest {

    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private UserDto userDto1;
    private ItemRequestDto itemRequestDto1;
    private UserDto userFromDb1;

    @BeforeEach
    void setUp() {
        userDto1 = makeUserDto("Fedor", "1@mail.ru");
        itemRequestDto1 = ItemRequestDto.builder().description("one good item").build();
        userFromDb1 = userService.postuser(userDto1);
    }

    @Test
    void testCreateItemRequests() {
        ItemRequestDto itemRequestFromDb = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        assertThat(itemRequestFromDb.getDescription(), equalTo(itemRequestDto1.getDescription()));
    }

    @Test
    void testCreateItemRequestByInvalidUser() {
        assertThrows(NotFoundException.class, () -> itemRequestService.createItemRequest(999L, itemRequestDto1));
    }

    @Test
    void testGetItemRequest() {
        ItemRequestDto itemRequestFromDb1 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        ItemRequestDto itemRequestFromDb2 = itemRequestService.getItemRequest(itemRequestFromDb1.getId());
        assertThat(itemRequestFromDb1.getDescription(), equalTo(itemRequestFromDb2.getDescription()));
        assertThat(itemRequestFromDb1.getId(), equalTo(itemRequestFromDb2.getId()));
        assertThat(itemRequestFromDb1.getOwnerId(), equalTo(itemRequestFromDb2.getOwnerId()));
    }

    @Test
    void testGetItemRequests() {
        ItemRequestDto itemRequestFromDb1 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        ItemRequestDto itemRequestFromDb2 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        ItemRequestDto itemRequestFromDb3 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        List<ItemRequestDto> items = itemRequestService.getItemRequests(userFromDb1.getId());
        assertThat(items.size(), equalTo(3));

    }

    @Test
    void testGetAllItemRequests() {
        UserDto userFromDb2 = userService.postuser(makeUserDto("Petr", "2@mail.ru"));
        ItemRequestDto itemRequestFromDb1 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        ItemRequestDto itemRequestFromDb2 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        ItemRequestDto itemRequestFromDb3 = itemRequestService.createItemRequest(userFromDb1.getId(), itemRequestDto1);
        List<ItemRequestDto> items = itemRequestService.getAllItemRequests(userFromDb2.getId());
        assertThat(items.size(), equalTo(3));
    }

    private UserDto makeUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }

}