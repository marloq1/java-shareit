package ru.practicum.shareit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

public class BookingDatesValidator implements ConstraintValidator<ValidDates, BookingRequestDto> {

    @Override
    public boolean isValid(BookingRequestDto booking, ConstraintValidatorContext context) {
        if (booking.getStart() == null || booking.getEnd() == null) {
            return false; // Проверка уже есть в @NotNull, но можно оставить
        }
        return booking.getStart().isBefore(booking.getEnd());
    }
}

