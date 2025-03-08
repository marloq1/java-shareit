package ru.practicum.shareit.item;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;


import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "spring.config.name = application-test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {

    private final ItemService itemService;
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final BookingService bookingService;
    private ItemDto itemDto1;
    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto userFromDb1;
    private UserDto userFromDb2;
    private ItemRequestDto itemRequestDto1;
    private final EntityManager entityManager;


    @BeforeEach
    void setUp() {
        userDto1 = makeUserDto("Fedor", "1@mail.ru");
        userDto2 = makeUserDto("Petr", "2@mail.ru");
        itemDto1 = makeItemDto("item1", "good item", true);
        itemRequestDto1 = ItemRequestDto.builder().description("one good item").build();
        userFromDb1 = userService.postuser(userDto1);
        userFromDb2 = userService.postuser(userDto2);
    }

    @Test
    void testPostItem() {

        ItemDto itemDtoFromDb = itemService.postItem(userFromDb1.getId(), itemDto1);
        assertThat(itemDtoFromDb.getName(), equalTo(itemDto1.getName()));
        assertThat(itemDtoFromDb.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(itemDtoFromDb.getAvailable(), equalTo(itemDto1.getAvailable()));
    }

    @Test
    void testPostItemAfterRequest() {

        ItemRequestDto itemRequestFromDb1 = itemRequestService.createItemRequest(userFromDb2.getId(), itemRequestDto1);
        itemDto1.setRequestId(itemRequestFromDb1.getId());
        ItemDto itemFromDb = itemService.postItem(userFromDb1.getId(), itemDto1);
        entityManager.flush();
        entityManager.clear();
        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(itemRequestFromDb1.getId());
        assertThat(itemRequestDto.getItems().get(0).getId(), equalTo(itemFromDb.getId()));
    }

    @Test
    void testPatchItemDescription() {
        ItemDto itemDtoFromDb1 = itemService.postItem(userFromDb1.getId(), itemDto1);
        ItemDto itemDtoFromDb2 = itemService.patchItem(userFromDb1.getId(),
                makeItemDto(null, "bad item", null), itemDtoFromDb1.getId());
        assertThat(itemDtoFromDb1.getId(), equalTo(itemDtoFromDb2.getId()));
        assertThat(itemDtoFromDb2.getDescription(), equalTo("bad item"));
        assertThat(itemDtoFromDb1.getName(), equalTo(itemDtoFromDb2.getName()));
        assertThat(itemDtoFromDb1.getOwnerId(), equalTo(itemDtoFromDb2.getOwnerId()));
        assertThat(itemDtoFromDb1.getAvailable(), equalTo(itemDtoFromDb2.getAvailable()));
    }

    @Test
    void testPatchItemName() {
        ItemDto itemDtoFromDb1 = itemService.postItem(userFromDb1.getId(), itemDto1);
        ItemDto itemDtoFromDb2 = itemService.patchItem(userFromDb1.getId(),
                makeItemDto("item2", null, null), itemDtoFromDb1.getId());
        assertThat(itemDtoFromDb1.getId(), equalTo(itemDtoFromDb2.getId()));
        assertThat(itemDtoFromDb2.getName(), equalTo("item2"));
        assertThat(itemDtoFromDb1.getDescription(), equalTo(itemDtoFromDb2.getDescription()));
        assertThat(itemDtoFromDb1.getOwnerId(), equalTo(itemDtoFromDb2.getOwnerId()));
        assertThat(itemDtoFromDb1.getAvailable(), equalTo(itemDtoFromDb2.getAvailable()));
    }

    @Test
    void testPatchItemAvailable() {
        ItemDto itemDtoFromDb1 = itemService.postItem(userFromDb1.getId(), itemDto1);
        ItemDto itemDtoFromDb2 = itemService.patchItem(userFromDb1.getId(),
                makeItemDto(null, null, false), itemDtoFromDb1.getId());
        assertThat(itemDtoFromDb1.getId(), equalTo(itemDtoFromDb2.getId()));
        assertThat(itemDtoFromDb2.getAvailable(), equalTo(false));
        assertThat(itemDtoFromDb1.getDescription(), equalTo(itemDtoFromDb2.getDescription()));
        assertThat(itemDtoFromDb1.getOwnerId(), equalTo(itemDtoFromDb2.getOwnerId()));
        assertThat(itemDtoFromDb1.getName(), equalTo(itemDtoFromDb2.getName()));
    }

    @Test
    void testPatchInvalidUser() {
        ItemDto itemDtoFromDb1 = itemService.postItem(userFromDb1.getId(), itemDto1);
        assertThrows(NotAllowedException.class, () -> itemService.patchItem(userFromDb2.getId(),
                makeItemDto(null, null, false), itemDtoFromDb1.getId()));

    }

    @Test
    void testItemSearch() {
        ItemDto itemDtoFromDb1 = itemService.postItem(userFromDb1.getId(), itemDto1);
        List<ItemDto> itemsDto = itemService.searchItems("te");
        assertThat(itemDtoFromDb1.getName(), equalTo(itemsDto.get(0).getName()));
        assertThat(itemDtoFromDb1.getDescription(), equalTo(itemsDto.get(0).getDescription()));
        assertThat(itemDtoFromDb1.getAvailable(), equalTo(itemsDto.get(0).getAvailable()));
    }


    @Test
    void testCommentPost() {
        ItemDto itemFromDb = itemService.postItem(userFromDb1.getId(), itemDto1);
        BookingDto bookingDtoFuture = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb.getId(), LocalDateTime.now().minusSeconds(20),
                        LocalDateTime.now().minusSeconds(10)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoFuture.getId());
        CommentDto commentDto = itemService.postComment(userFromDb2.getId(), itemFromDb.getId(),
                makeCommentDto("Comment1"));
        assertThat(commentDto.getText(), equalTo("Comment1"));
    }

    @Test
    void testGetItem() {
        ItemDto itemFromDb = itemService.postItem(userFromDb1.getId(), itemDto1);
        BookingDto bookingDtoPast = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb.getId(), LocalDateTime.now().minusSeconds(20),
                        LocalDateTime.now().minusSeconds(10)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoPast.getId());
        CommentDto commentDto = itemService.postComment(userFromDb2.getId(), itemFromDb.getId(),
                makeCommentDto("Comment1"));

        entityManager.flush();
        entityManager.clear();

        BookingDto bookingDtoFuture = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb.getId(), LocalDateTime.now().plusSeconds(10),
                        LocalDateTime.now().plusSeconds(20)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoFuture.getId());


        ItemDtoWithBookingAndComments itemFromDbWithBookings = itemService.getItem(userFromDb1.getId(),
                itemFromDb.getId());
        assertThat(itemFromDbWithBookings.getLastBooking().getId(), equalTo(bookingDtoPast.getId()));
        assertThat(itemFromDbWithBookings.getNextBooking().getId(), equalTo(bookingDtoFuture.getId()));
        assertThat(itemFromDbWithBookings.getName(), equalTo(itemDto1.getName()));
        assertThat(itemFromDbWithBookings.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(itemFromDbWithBookings.getAvailable(), equalTo(itemDto1.getAvailable()));
        assertThat(itemFromDbWithBookings.getOwnerId(), equalTo(userFromDb1.getId()));
        assertThat(itemFromDbWithBookings.getComments().getFirst().getText(), equalTo(commentDto.getText()));

        itemFromDbWithBookings = itemService.getItem(userFromDb2.getId(),
                itemFromDb.getId());
        assertNull(itemFromDbWithBookings.getLastBooking());
        assertNull(itemFromDbWithBookings.getNextBooking());
        assertThat(itemFromDbWithBookings.getName(), equalTo(itemDto1.getName()));
        assertThat(itemFromDbWithBookings.getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(itemFromDbWithBookings.getAvailable(), equalTo(itemDto1.getAvailable()));
        assertThat(itemFromDbWithBookings.getOwnerId(), equalTo(userFromDb1.getId()));
    }

    @Test
    void testGetItems() {
        ItemDto itemFromDb = itemService.postItem(userFromDb1.getId(), itemDto1);
        BookingDto bookingDtoPast = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb.getId(), LocalDateTime.now().minusSeconds(20),
                        LocalDateTime.now().minusSeconds(10)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoPast.getId());
        BookingDto bookingDtoFuture = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb.getId(), LocalDateTime.now().plusSeconds(10),
                        LocalDateTime.now().plusSeconds(20)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoFuture.getId());
        List<ItemDtoWithBookingAndComments> itemsFromDbWithBookings = itemService.getItems(userFromDb1.getId());
        assertThat(itemsFromDbWithBookings.size(), equalTo(1));
        assertThat(itemsFromDbWithBookings.getFirst().getLastBooking().getId(), equalTo(bookingDtoPast.getId()));
        assertThat(itemsFromDbWithBookings.getFirst().getNextBooking().getId(), equalTo(bookingDtoFuture.getId()));
        assertThat(itemsFromDbWithBookings.getFirst().getName(), equalTo(itemDto1.getName()));
        assertThat(itemsFromDbWithBookings.getFirst().getDescription(), equalTo(itemDto1.getDescription()));
        assertThat(itemsFromDbWithBookings.getFirst().getAvailable(), equalTo(itemDto1.getAvailable()));
        assertThat(itemsFromDbWithBookings.getFirst().getOwnerId(), equalTo(userFromDb1.getId()));
    }


    private ItemDto makeItemDto(String name, String description, Boolean available) {
        return ItemDto.builder()
                .name(name)
                .description(description)
                .available(available)
                .build();
    }

    private UserDto makeUserDto(String name, String email) {
        return UserDto.builder()
                .name(name)
                .email(email)
                .build();
    }

    private BookingRequestDto makeBookingDto(Long itemId, LocalDateTime start, LocalDateTime end) {
        return BookingRequestDto.builder()
                .itemId(itemId)
                .start(start)
                .end(end)
                .build();
    }

    private CommentDto makeCommentDto(String text) {
        return CommentDto.builder()
                .text(text)
                .build();
    }

}