package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import java.util.List;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@GetMapping
	public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
										@RequestParam(name = "state", defaultValue = "all") String stateParam,
										@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
										@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@PostMapping
	public BookingDto bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
							   @RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	@GetMapping("/{bookingId}")
	public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
								 @PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PatchMapping("/{bookingId}")
	public BookingDto acceptBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
									@RequestParam boolean approved, @PathVariable Long bookingId) {
		return bookingClient.acceptBooking(userId, approved, bookingId);
	}

	@GetMapping("/owner")
	public List<BookingDto> findOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
											  @RequestParam(name = "state", defaultValue = "all")
											  String stateParam) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		return bookingClient.findOwnerBookings(userId, state);
	}
}
