package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;

import java.util.List;

public interface ItemService {

    ItemDto postItem(long userId, ItemDto itemDto);

    ItemDto patchItem(long userId, ItemDto itemDto, long itemId);

    ItemDtoWithBookingAndComments getItem(Long userId, long itemId);

    List<ItemDtoWithBookingAndComments> getItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto postComment(Long userId, long itemId,CommentDto commentDto);

}
