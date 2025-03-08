package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class ItemTest {


    @Test
    void testUserEqualsAndHash() {
        Item item1 = Item.builder().id(1L).build();
        Item item2 = Item.builder().id(1L).build();
        Item item3 = Item.builder().id(3L).build();
        assertThat(item1, equalTo(item1));
        assertThat(item1, not(equalTo(nullValue())));
        assertThat(item1, equalTo(item2));
        assertThat(item1.hashCode(), equalTo(item2.hashCode()));
        assertThat(item1, not(equalTo(item3)));
    }
}
