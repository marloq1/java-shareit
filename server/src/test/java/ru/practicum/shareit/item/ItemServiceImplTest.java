package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public class ItemServiceImplTest {


    @Mock
    ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemService itemService;




    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository,userRepository,bookingRepository,
                commentRepository,itemRequestRepository);
    }

    @Test
    void testItemEmptySearch() {
        List<ItemDto> itemsDto = itemService.searchItems("");
        assertThat(itemsDto, empty());
    }

    @Test
    void testItemNullSearch() {
        List<ItemDto> itemsDto = itemService.searchItems(null);
        assertThat(itemsDto, empty());
    }
}
