package ru.practicum.shareit.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {

    private final ObjectMapper objectMapper;

    private static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder,
                      ObjectMapper mapper) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
        this.objectMapper = mapper;
    }

    public UserDto postUser(UserDto userDto) {
        return objectMapper.convertValue(post("", userDto).getBody(),
                new TypeReference<UserDto>() {
                });
    }

    public UserDto patchUser(UserDto userDto, Long userId) {
        return objectMapper.convertValue(patch("/" + userId, userDto).getBody(),
                new TypeReference<UserDto>() {
                });
    }

    public UserDto getUser(Long userId) {
        return objectMapper.convertValue(get("/" + userId).getBody(),
                new TypeReference<UserDto>() {
                });
    }

    public void deleteUser(Long userId) {
        delete("/" + userId);
    }

}
