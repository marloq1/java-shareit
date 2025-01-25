package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto postItem(long userId, ItemDto itemDto);

    ItemDto patchItem(long userId, ItemDto itemDto, long itemId);

    ItemDto getItem(long itemId);

    List<ItemDto> getItems(Long userId);

    List<ItemDto> searchItems(String text);

}
