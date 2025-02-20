package ru.practicum.shareit.booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotAllowedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public BookingDto createBooking(Long userId, BookingRequestDto bookingRequestDto) {

        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещи с таким id нет"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("пользователя с таким id нет"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь нельзя забронировать");
        }
        return BookingMapper.mapBookingToDto(bookingRepository.save(BookingMapper
                .mapDtoToBooking(bookingRequestDto, user, item)));
    }

    @Transactional
    @Override
    public BookingDto acceptBooking(Long userId, boolean approved, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Брони с таким id нет"));
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new NotAllowedException("Эта вещь не пренадлежит пользователю с id " + userId);
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.mapBookingToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto findBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Брони с таким id нет"));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.mapBookingToDto(booking);
        } else {
            throw new NotAllowedException("Нет доступа к брони");
        }
    }

    @Override
    public List<BookingDto> findBookings(Long bookerId, State state) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("пользователя с таким id нет"));
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
            case PAST -> bookingRepository
                    .findByEndBeforeAndBookerIdAndStatusOrderByStartDesc(LocalDateTime.now(),
                            bookerId, Status.APPROVED);
            case FUTURE -> bookingRepository
                    .findByStartAfterAndBookerIdAndStatusOrderByStartDesc(LocalDateTime.now(),
                            bookerId, Status.APPROVED);
            case CURRENT -> bookingRepository
                    .findByStartBeforeAndEndAfterAndBookerIdAndStatusOrderByStartDesc(LocalDateTime.now(),
                            LocalDateTime.now(), bookerId, Status.APPROVED);
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, Status.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, Status.REJECTED);
        };
        return bookings.stream().map(BookingMapper::mapBookingToDto).toList();
    }


    @Override
    public List<BookingDto> findOwnerBookings(Long ownerId, State state) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("пользователя с таким id нет"));
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByOwnerId(ownerId);
            case PAST -> bookingRepository
                    .findByOwnerIdStatePast(ownerId);
            case FUTURE -> bookingRepository
                    .findByOwnerIdStateFuture(ownerId);
            case CURRENT -> bookingRepository
                    .findByOwnerIdStateCurrent(ownerId);
            case WAITING -> bookingRepository
                    .findByOwnerIdWaiting(ownerId);
            case REJECTED -> bookingRepository
                    .findByOwnerIdRejected(ownerId);
        };
        return bookings.stream().map(BookingMapper::mapBookingToDto).toList();
    }
}
