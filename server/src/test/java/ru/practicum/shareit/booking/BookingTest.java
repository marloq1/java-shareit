package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookingTest {
    @Test
    void testBookingEqualsAndHash() {
        Booking booking1 = Booking.builder().id(1L).build();
        Booking booking2 = Booking.builder().id(1L).build();
        Booking booking3 = Booking.builder().id(3L).build();
        assertThat(booking1, equalTo(booking1));
        assertThat(booking1, not(equalTo(nullValue())));
        assertThat(booking1, equalTo(booking2));
        assertThat(booking1.hashCode(), equalTo(booking2.hashCode()));
        assertThat(booking1, not(equalTo(booking3)));
    }
}
