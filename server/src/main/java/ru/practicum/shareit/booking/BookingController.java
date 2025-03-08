package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@Controller
@RequiredArgsConstructor
public class BookingController {


    private final BookingService bookingService;


    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody BookingRequestDto bookingRequestDto) {
        return bookingService.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto acceptBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam boolean approved, @PathVariable Long bookingId) {
        return bookingService.acceptBooking(userId, approved, bookingId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBooking(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long bookingId) {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> findBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.findBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.findOwnerBookings(userId, state);
    }
}
