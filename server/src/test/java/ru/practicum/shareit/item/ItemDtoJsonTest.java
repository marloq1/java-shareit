package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;


import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Item")
            .description("good item")
            .available(true)
            .ownerId(2L)
            .requestId(3L)
            .build();

    @Test
    void testSerialize() throws Exception {

        var result = json.write(itemDto);
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.ownerId");
        assertThat(result).hasJsonPath("$.requestId");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDto
                .getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto
                .getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(itemDto
                .getOwnerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDto
                .getRequestId().intValue());
    }
}

