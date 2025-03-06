package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequest mapDtoToRequest(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .created(LocalDateTime.now())
                .owner(user)
                .build();
    }

    public static ItemRequestDto mapRequestToDto(ItemRequest itemRequest) {
        ItemRequestDto dto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .ownerId(itemRequest.getOwner().getId())
                .build();
        if (itemRequest.getItems() != null) {
            dto.setItems(itemRequest.getItems().stream().map(ItemMapper::mapItemToDto).toList());
        }
        return dto;
    }
}
