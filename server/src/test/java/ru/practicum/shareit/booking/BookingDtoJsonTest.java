package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .item(ItemDto.builder().id(2L).name("Item1").build())
            .booker(UserDto.builder().id(3L).name("Fedor").build())
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .status(Status.WAITING)
            .build();

    @Test
    void testSerialize() throws Exception {

        var result = json.write(bookingDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.start");
        assertThat(result).hasJsonPath("$.end");
        assertThat(result).hasJsonPath("$.booker");
        assertThat(result).hasJsonPath("$.item");
        assertThat(result).hasJsonPath("$.status");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(bookingDto.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(bookingDto
                .getBooker().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(bookingDto
                .getBooker().getName());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(bookingDto
                .getItem().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(bookingDto
                .getItem().getName());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingDto
                .getStatus().toString());
    }
}
