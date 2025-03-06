package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImpTest {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
    }


    @Test
    void acceptBookingNotValidUser() {

        Mockito
                .when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(Booking.builder()
                        .id(1L)
                        .item(Item.builder().owner(User.builder().id(1L).build()).build())
                        .build()));
        assertThrows(NotAllowedException.class, () -> bookingService.acceptBooking(2L,
                true, 1L));
    }

    @Test
    void testPostBookingNotAvailableItem() {
        Mockito
                .when(itemRepository.findById(any()))
                .thenReturn(Optional.of(Item.builder()
                        .available(false).build()));
        Mockito
                .when(userRepository.findById(any()))
                .thenReturn(Optional.of(User.builder().build()));
        assertThrows(ValidationException.class, () -> bookingService.createBooking(1L,
                BookingRequestDto.builder().build()));
    }

    @Test
    void testBookingNullMapper() {
        assertThat(BookingMapper.mapBookingToDto(null),is(nullValue()));
    }

    @Test
    void testNotAllowedFind() {
        Mockito
                .when(bookingRepository.findById(1L))
                .thenReturn(Optional.of(Booking.builder()
                        .id(1L)
                        .booker(User.builder().id(2L).build())
                        .item(Item.builder().owner(User.builder().id(1L).build()).build())
                        .build()));
        assertThrows(NotAllowedException.class, () -> bookingService.findBooking(3L, 1L));

    }

}
