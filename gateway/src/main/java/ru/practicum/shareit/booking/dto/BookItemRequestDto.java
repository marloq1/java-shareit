package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookItemRequestDto {
	private long itemId;
	@FutureOrPresent
	private LocalDateTime start;
	@Future
	private LocalDateTime end;


}
