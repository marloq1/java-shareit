package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemClientTest {


    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private ItemClient itemClient;

    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item1")
            .description("good item")
            .available(true)
            .ownerId(2L)
            .requestId(3L)
            .build();

    private CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .authorName("Fedor")
            .itemId(2L)
            .created(LocalDateTime.now())
            .text("comment")
            .build();

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any())).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);
        itemClient = new ItemClient("", builder);
    }

    @Test
    void testPostItem() {
        Mockito
                .when(restTemplate.exchange("", HttpMethod.POST, new HttpEntity<>(itemDto,
                        defaultHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemDto));
        ResponseEntity<Object> response = itemClient.postItem(1L, itemDto);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDto));
    }

    @Test
    void testPatchItem() {
        Mockito
                .when(restTemplate.exchange("/2", HttpMethod.PATCH, new HttpEntity<>(itemDto,
                        defaultHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemDto));
        ResponseEntity<Object> response = itemClient.patchItem(1L, itemDto, 2L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDto));
    }

    @Test
    void testGetItems() {
        List<ItemDto> items = List.of(itemDto);
        Mockito
                .when(restTemplate.exchange("", HttpMethod.GET, new HttpEntity<>(null,
                        defaultHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(items));
        ResponseEntity<Object> response = itemClient.getItems(1L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(items));
    }

    @Test
    void testGetItem() {
        Mockito
                .when(restTemplate.exchange("/2", HttpMethod.GET, new HttpEntity<>(null,
                        defaultHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(itemDto));
        ResponseEntity<Object> response = itemClient.getItem(1L, 2L);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDto));
    }

    @Test
    void testSearch() {
        Mockito
                .when(restTemplate.exchange("/search?text={text}", HttpMethod.GET, new HttpEntity<>(null,
                        defaultHeaders(null)), Object.class, Map.of(
                        "text", "a"
                )))
                .thenReturn(ResponseEntity.ok(itemDto));
        ResponseEntity<Object> response = itemClient.searchItems("a");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(itemDto));
        ResponseEntity<Object> responseNull = itemClient.searchItems(null);
        ResponseEntity<Object> responseEmpty = itemClient.searchItems("");
        assertThat(responseNull.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseNull.getBody(), equalTo(List.of()));
        assertThat(responseEmpty.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(responseEmpty.getBody(), equalTo(List.of()));
    }

    @Test
    void testPostComment() {
        Mockito
                .when(restTemplate.exchange("/2/comment", HttpMethod.POST, new HttpEntity<>(commentDto,
                        defaultHeaders(1L)), Object.class))
                .thenReturn(ResponseEntity.ok(commentDto));
        ResponseEntity<Object> response = itemClient.postComment(1L, 2L, commentDto);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo(commentDto));
    }


    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }


}