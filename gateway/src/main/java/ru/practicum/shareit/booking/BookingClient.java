package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreateUpdateDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(long userId, BookingCreateUpdateDto bookingRequestDto) {
        return post("/", userId, bookingRequestDto);
    }

    public ResponseEntity<Object> updateBooking(long bookingId, Boolean approved, long userId) {
        return patch("/" + bookingId + "/?approved=" + approved, userId);
    }

    public ResponseEntity<Object> getBookings(long userId, String state, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", state.toUpperCase(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingByUserIdAndState(String state, long userId, int from, int size) {
        return get("?state={state}&from={from}&size={size}",
                userId, Map.of("state", state.toUpperCase(), "from", from, "size", size)
        );
    }

    public ResponseEntity<Object> getOwnerBookedItems(String state, long userId, int from, int size) {
        return get("/owner?state={state}&from={from}&size={size}",
                userId, Map.of("state", state.toUpperCase(), "from", from, "size", size
                ));
    }

    public ResponseEntity<Object> getBooking(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }


}
