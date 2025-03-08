package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;

import java.util.List;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private final ObjectMapper objectMapper;


    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                      ObjectMapper objectMapper) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.objectMapper = objectMapper;
    }

    public ItemDto postItem(Long userId, ItemDto itemDto) {
        return objectMapper.convertValue(post("", userId, itemDto).getBody(),
                new TypeReference<ItemDto>() {
                });
    }

    public ItemDto patchItem(Long userId, ItemDto itemDto, Long itemId) {
        return objectMapper.convertValue(patch("/" + itemId, userId, itemDto).getBody(),
                new TypeReference<ItemDto>() {
                });
    }

    public List<ItemDtoWithBookingAndComments> getItems(Long userId) {
        return objectMapper.convertValue(get("", userId).getBody(),
                new TypeReference<List<ItemDtoWithBookingAndComments>>() {
                });
    }

    public ItemDtoWithBookingAndComments getItem(Long userId, Long itemId) {
        return objectMapper.convertValue(get("/" + itemId, userId).getBody(),
                new TypeReference<ItemDtoWithBookingAndComments>() {
                });
    }

    public List<ItemDto> searchItems(String text)  {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return objectMapper.convertValue(get("/search?text={text}", null, parameters).getBody(),
                new TypeReference<List<ItemDto>>() {
                });
    }

    public CommentDto postComment(Long userId, Long itemId, CommentDto commentDto) {
        return objectMapper.convertValue(post("/" + itemId + "/comment", userId, commentDto).getBody(),
                new TypeReference<CommentDto>() {
                });
    }
}
