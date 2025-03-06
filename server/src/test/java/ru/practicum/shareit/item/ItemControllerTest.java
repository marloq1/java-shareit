package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item1")
            .description("good item")
            .available(true)
            .ownerId(2L)
            .requestId(3L)
            .build();

    private ItemDtoWithBookingAndComments itemDtoWithBookingAndComments = ItemDtoWithBookingAndComments.builder()
            .id(1L)
            .name("item1")
            .description("good item")
            .available(true)
            .ownerId(2L)
            .lastBooking(BookingDto.builder().id(4L).build())
            .nextBooking(BookingDto.builder().id(5L).build())
            .comments(List.of(CommentDto.builder().id(6L).build()))
            .build();

    private CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .authorName("Fedor")
            .itemId(2L)
            .created(LocalDateTime.now())
            .text("comment")
            .build();

    @Test
    void testSaveNewItem() throws Exception {
        when(itemService.postItem(2L, itemDto))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void testPatchNewItem() throws Exception {
        when(itemService.patchItem(2L, itemDto, 1L))
                .thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.requestId", is(itemDto.getRequestId()), Long.class));
    }

    @Test
    void testGetItems() throws Exception {
        when(itemService.getItems(2L))
                .thenReturn(List.of(itemDtoWithBookingAndComments));
        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoWithBookingAndComments.getId()), Long.class))
                .andExpect(jsonPath("$[0].ownerId", is(itemDtoWithBookingAndComments.getOwnerId()), Long.class))
                .andExpect(jsonPath("$[0].available", is(itemDtoWithBookingAndComments.getAvailable())))
                .andExpect(jsonPath("$[0].description", is(itemDtoWithBookingAndComments.getDescription())))
                .andExpect(jsonPath("$[0].lastBooking.id", is(itemDtoWithBookingAndComments.getLastBooking()
                        .getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id", is(itemDtoWithBookingAndComments.getNextBooking()
                        .getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].id", is(itemDtoWithBookingAndComments.getComments()
                        .getFirst().getId()), Long.class));
    }

    @Test
    void testGetItem() throws Exception {
        when(itemService.getItem(2L, 1L))
                .thenReturn(itemDtoWithBookingAndComments);
        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoWithBookingAndComments.getId()), Long.class))
                .andExpect(jsonPath("$.ownerId", is(itemDtoWithBookingAndComments.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.available", is(itemDtoWithBookingAndComments.getAvailable())))
                .andExpect(jsonPath("$.description", is(itemDtoWithBookingAndComments.getDescription())))
                .andExpect(jsonPath("$.lastBooking.id", is(itemDtoWithBookingAndComments.getLastBooking()
                        .getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id", is(itemDtoWithBookingAndComments.getNextBooking()
                        .getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].id", is(itemDtoWithBookingAndComments.getComments()
                        .getFirst().getId()), Long.class));
    }

    @Test
    void testSearch() throws Exception {
        when(itemService.searchItems("item"))
                .thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search?text=item")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoWithBookingAndComments.getId()), Long.class))
                .andExpect(jsonPath("$[0].ownerId", is(itemDtoWithBookingAndComments.getOwnerId()), Long.class))
                .andExpect(jsonPath("$[0].available", is(itemDtoWithBookingAndComments.getAvailable())))
                .andExpect(jsonPath("$[0].description", is(itemDtoWithBookingAndComments.getDescription())));
    }

    @Test
    void testPostComment() throws Exception {
        when(itemService.postComment(2L, 1L, commentDto))
                .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 2L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.itemId", is(commentDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())));
    }


}