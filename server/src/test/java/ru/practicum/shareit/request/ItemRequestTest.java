package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.model.ItemRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ItemRequestTest {

    @Test
    void testUserEqualsAndHash() {
        ItemRequest itemRequest1 = ItemRequest.builder().id(1L).build();
        ItemRequest itemRequest2 = ItemRequest.builder().id(1L).build();
        ItemRequest itemRequest3 = ItemRequest.builder().id(3L).build();
        assertThat(itemRequest1, equalTo(itemRequest1));
        assertThat(itemRequest1, not(equalTo(nullValue())));
        assertThat(itemRequest1, equalTo(itemRequest2));
        assertThat(itemRequest1.hashCode(), equalTo(itemRequest2.hashCode()));
        assertThat(itemRequest1, not(equalTo(itemRequest3)));
    }
}
