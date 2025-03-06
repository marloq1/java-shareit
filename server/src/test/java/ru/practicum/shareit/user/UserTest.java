package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class UserTest {

    @Test
    void testUserEqualsAndHash() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(1L).build();
        User user3 = User.builder().id(3L).build();
        assertThat(user1, equalTo(user1));
        assertThat(user1, not(equalTo(nullValue())));
        assertThat(user1, equalTo(user2));
        assertThat(user1.hashCode(), equalTo(user2.hashCode()));
        assertThat(user1, not(equalTo(user3)));
    }
}
