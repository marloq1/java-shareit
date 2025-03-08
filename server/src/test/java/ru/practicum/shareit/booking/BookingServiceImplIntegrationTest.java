package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest(
        properties = "spring.config.name = application-test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {

    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;
    private ItemDto itemDto1;
    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto userFromDb1;
    private UserDto userFromDb2;
    private ItemDto itemFromDb1;
    private final EntityManager entityManager;

    @BeforeEach
    void setUp() {
        userDto1 = makeUserDto("Fedor", "1@mail.ru");
        userDto2 = makeUserDto("Petr", "2@mail.ru");
        itemDto1 = makeItemDto("item1", "good item", true);
        //itemRequestDto1 = ItemRequestDto.builder().description("one good item").build();
        userFromDb1 = userService.postuser(userDto1);
        userFromDb2 = userService.postuser(userDto2);
        itemFromDb1 = itemService.postItem(userFromDb1.getId(), itemDto1);
    }

    @Test
    void testPostBooking() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        assertThat(bookingFromDb.getStart(), equalTo(start));
        assertThat(bookingFromDb.getEnd(), equalTo(end));
        assertThat(bookingFromDb.getItem().getId(), equalTo(itemFromDb1.getId()));
        assertThat(bookingFromDb.getBooker().getId(), equalTo(userFromDb2.getId()));

    }

    @Test
    void testPostBookingInvalidItemId() {

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(999L, LocalDateTime.now(), LocalDateTime.now())));

    }

    @Test
    void testPostBookingInvalidUserId() {

        assertThrows(NotFoundException.class, () -> bookingService.createBooking(999L,
                makeBookingDto(itemFromDb1.getId(), LocalDateTime.now(), LocalDateTime.now())));

    }

    @Test
    void acceptBooking() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        BookingDto bookingFromDb2 = bookingService.acceptBooking(userFromDb1.getId(),
                true, bookingFromDb.getId());
        assertThat(bookingFromDb2.getStatus(), equalTo(Status.APPROVED));

    }

    @Test
    void notAcceptBooking() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        BookingDto bookingFromDb2 = bookingService.acceptBooking(userFromDb1.getId(),
                false, bookingFromDb.getId());
        assertThat(bookingFromDb2.getStatus(), equalTo(Status.REJECTED));

    }

    @Test
    void acceptBookingInvalidBookingId() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        assertThrows(NotFoundException.class, () -> bookingService.acceptBooking(userFromDb1.getId(),
                true, 999L));

    }


    @Test
    void findBookingBooker() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        BookingDto bookingFromDb2 = bookingService.findBooking(userFromDb2.getId(), bookingFromDb.getId());
        assertThat(bookingFromDb.getId(), equalTo(bookingFromDb2.getId()));
        assertThat(bookingFromDb.getStart(), equalTo(bookingFromDb2.getStart()));
        assertThat(bookingFromDb.getEnd(), equalTo(bookingFromDb2.getEnd()));
        assertThat(bookingFromDb.getItem().getId(), equalTo(bookingFromDb2.getItem().getId()));
        assertThat(bookingFromDb.getBooker().getId(), equalTo(bookingFromDb2.getBooker().getId()));
    }

    @Test
    void findBookingItemOwner() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        BookingDto bookingFromDb2 = bookingService.findBooking(userFromDb1.getId(), bookingFromDb.getId());
        assertThat(bookingFromDb.getId(), equalTo(bookingFromDb2.getId()));
        assertThat(bookingFromDb.getStart(), equalTo(bookingFromDb2.getStart()));
        assertThat(bookingFromDb.getEnd(), equalTo(bookingFromDb2.getEnd()));
        assertThat(bookingFromDb.getItem().getId(), equalTo(bookingFromDb2.getItem().getId()));
        assertThat(bookingFromDb.getBooker().getId(), equalTo(bookingFromDb2.getBooker().getId()));
    }

    @Test
    void findBookingInvalidBookingId() {
        LocalDateTime start = LocalDateTime.now().plusSeconds(10);
        LocalDateTime end = LocalDateTime.now().plusSeconds(20);
        BookingDto bookingFromDb = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDb1.getId(), start, end));
        assertThrows(NotFoundException.class, () -> bookingService.findBooking(userFromDb1.getId(), 999L));
    }

    @Test
    void testFindBookings() {
        ItemDto itemFromDbPast = itemService.postItem(userFromDb1.getId(), makeItemDto("item1",
                "past item", true));
        BookingDto bookingDtoPast = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDbPast.getId(), LocalDateTime.now().minusSeconds(20),
                        LocalDateTime.now().minusSeconds(10)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoPast.getId());

        ItemDto itemFromDbFuture = itemService.postItem(userFromDb1.getId(), makeItemDto("item2",
                "future item", true));
        BookingDto bookingDtoFuture = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDbFuture.getId(), LocalDateTime.now().plusSeconds(10),
                        LocalDateTime.now().plusSeconds(20)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoFuture.getId());

        ItemDto itemFromDbCurrent = itemService.postItem(userFromDb1.getId(), makeItemDto("item3",
                "current item", true));
        BookingDto bookingDtoCurrent = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDbCurrent.getId(), LocalDateTime.now().minusSeconds(10),
                        LocalDateTime.now().plusSeconds(10)));
        bookingService.acceptBooking(userFromDb1.getId(), true, bookingDtoCurrent.getId());

        ItemDto itemFromDbRejected = itemService.postItem(userFromDb1.getId(), makeItemDto("item4",
                "rejected item", true));
        BookingDto bookingDtoRejected = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDbRejected.getId(), LocalDateTime.now().plusSeconds(10),
                        LocalDateTime.now().plusSeconds(20)));
        bookingService.acceptBooking(userFromDb1.getId(), false, bookingDtoRejected.getId());

        ItemDto itemFromDbWaiting = itemService.postItem(userFromDb1.getId(), makeItemDto("item4",
                "waiting item", true));
        BookingDto bookingDtoWaiting = bookingService.createBooking(userFromDb2.getId(),
                makeBookingDto(itemFromDbWaiting.getId(), LocalDateTime.now().plusSeconds(10),
                        LocalDateTime.now().plusSeconds(20)));

        List<BookingDto> allBookings = bookingService.findBookings(userFromDb2.getId(), State.ALL);
        assertThat(allBookings.size(), equalTo(5));
        List<BookingDto> allBookingsOwner = bookingService.findOwnerBookings(userFromDb1.getId(), State.ALL);
        assertThat(allBookingsOwner.size(), equalTo(5));


        List<BookingDto> pastBookings = bookingService.findBookings(userFromDb2.getId(), State.PAST);
        assertThat(pastBookings.size(), equalTo(1));
        assertThat(pastBookings.get(0).getId(), equalTo(bookingDtoPast.getId()));
        List<BookingDto> pastBookingsOwner = bookingService.findOwnerBookings(userFromDb1.getId(), State.PAST);
        assertThat(pastBookingsOwner.size(), equalTo(1));
        assertThat(pastBookingsOwner.get(0).getId(), equalTo(bookingDtoPast.getId()));

        List<BookingDto> futureBookings = bookingService.findBookings(userFromDb2.getId(), State.FUTURE);
        assertThat(futureBookings.size(), equalTo(1));
        assertThat(futureBookings.get(0).getId(), equalTo(bookingDtoFuture.getId()));
        List<BookingDto> futureBookingsOwner = bookingService.findOwnerBookings(userFromDb1.getId(), State.FUTURE);
        assertThat(futureBookingsOwner.size(), equalTo(1));
        assertThat(futureBookingsOwner.get(0).getId(), equalTo(bookingDtoFuture.getId()));

        List<BookingDto> currentBookings = bookingService.findBookings(userFromDb2.getId(), State.CURRENT);
        assertThat(currentBookings.size(), equalTo(1));
        assertThat(currentBookings.get(0).getId(), equalTo(bookingDtoCurrent.getId()));
        List<BookingDto> currentBookingsOwner = bookingService.findOwnerBookings(userFromDb1.getId(), State.CURRENT);
        assertThat(currentBookingsOwner.size(), equalTo(1));
        assertThat(currentBookingsOwner.get(0).getId(), equalTo(bookingDtoCurrent.getId()));


        List<BookingDto> rejectedBookings = bookingService.findBookings(userFromDb2.getId(), State.REJECTED);
        assertThat(rejectedBookings.size(), equalTo(1));
        assertThat(rejectedBookings.get(0).getId(), equalTo(bookingDtoRejected.getId()));
        List<BookingDto> rejectedBookingsOwner = bookingService.findOwnerBookings(userFromDb1.getId(), State.REJECTED);
        assertThat(rejectedBookingsOwner.size(), equalTo(1));
        assertThat(rejectedBookingsOwner.get(0).getId(), equalTo(bookingDtoRejected.getId()));

        List<BookingDto> waitingBookings = bookingService.findBookings(userFromDb2.getId(), State.WAITING);
        assertThat(waitingBookings.size(), equalTo(1));
        assertThat(waitingBookings.get(0).getId(), equalTo(bookingDtoWaiting.getId()));
        List<BookingDto> waitingBookingsOwner = bookingService.findOwnerBookings(userFromDb1.getId(), State.WAITING);
        assertThat(waitingBookingsOwner.size(), equalTo(1));
        assertThat(waitingBookingsOwner.get(0).getId(), equalTo(bookingDtoWaiting.getId()));


    }

    @Test
    void testFindBookingWithInvalidUser() {
        assertThrows(NotFoundException.class, () -> bookingService.findBookings(999L, State.ALL));
        assertThrows(NotFoundException.class, () -> bookingService.findOwnerBookings(999L, State.ALL));
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


}