package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.util.List;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";
    private final ObjectMapper objectMapper;

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                         ObjectMapper mapper) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.objectMapper = mapper;
    }

    public List<BookingDto> getBookings(long userId, BookingState state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return objectMapper.convertValue(get("?state={state}&from={from}&size={size}", userId, parameters)
                .getBody(), new TypeReference<List<BookingDto>>() {
        });
    }


    public BookingDto bookItem(long userId, BookItemRequestDto requestDto) {
        return objectMapper.convertValue(post("", userId, requestDto).getBody(),
                new TypeReference<BookingDto>() {
                });
    }

    public BookingDto getBooking(long userId, Long bookingId) {

        return objectMapper.convertValue(get("/" + bookingId, userId).getBody(),
                new TypeReference<BookingDto>() {
                });
    }

    public BookingDto acceptBooking(long userId, boolean approved, Long bookingId) {
        return objectMapper.convertValue(patch("/" + bookingId + "?" + "approved=" + approved, userId)
                        .getBody(),
                new TypeReference<BookingDto>() {
                });
    }

    public List<BookingDto> findOwnerBookings(Long userId, BookingState state) {
        Map<String, Object> parameters = Map.of(
                "state", state.name()
        );
        return objectMapper.convertValue(get("/owner?state={state}", userId, parameters)
                .getBody(), new TypeReference<List<BookingDto>>() {
        });
    }
}
