package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .description("request")
            .created(LocalDateTime.now())
            .ownerId(2L)
            .items(List.of(ItemDto.builder().id(3L).name("item1").build()))
            .build();

    @Test
    void testSerialize() throws Exception {

        var result = json.write(itemRequestDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).hasJsonPath("$.ownerId");
        assertThat(result).hasJsonPath("$.items");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(itemRequestDto
                .getOwnerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(itemRequestDto
                .getItems().getFirst().getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(itemRequestDto
                .getItems().getFirst().getName());
    }
}

