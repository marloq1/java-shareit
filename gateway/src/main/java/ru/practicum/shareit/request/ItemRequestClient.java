package ru.practicum.shareit.request;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Service
public class ItemRequestClient extends BaseClient {

    private final ObjectMapper objectMapper;

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                             ObjectMapper mapper) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.objectMapper = mapper;
    }

    public ItemRequestDto createItemRequest(long userId, ItemRequestDto itemRequestDto) {
        return objectMapper.convertValue(post("", userId, itemRequestDto).getBody(),
                new TypeReference<ItemRequestDto>() {
                });
    }

    public List<ItemRequestDto> getItemRequests(long userId) {
        return objectMapper.convertValue(get("", userId).getBody(),
                new TypeReference<List<ItemRequestDto>>() {
                });
    }

    public List<ItemRequestDto> getAllItemRequests(long userId) {
        return objectMapper.convertValue(get("", userId).getBody(),
                new TypeReference<List<ItemRequestDto>>() {
                });
    }

    public ItemRequestDto getItemRequest(long requestId) {
        return objectMapper.convertValue(get("/" + requestId).getBody(),
                new TypeReference<ItemRequestDto>() {
                });
    }
}
