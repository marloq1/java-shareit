package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;


import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long userId, BookingRequestDto bookingRequestDtoDto);

    BookingDto acceptBooking(Long userId, boolean approved, Long bookingId);

    BookingDto findBooking(Long userId, Long bookingId);

    List<BookingDto> findBookings(Long userId, State state);

    List<BookingDto> findOwnerBookings(Long userId, State state);


}
