package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.validation.ValidDates;

import java.time.LocalDateTime;


@Data
@ValidDates
public class BookingRequestDto {

    @NotNull
    private Long itemId;
    @NotNull

    @FutureOrPresent
    private LocalDateTime start;
    @NotNull
    @Future
    private LocalDateTime end;
}
