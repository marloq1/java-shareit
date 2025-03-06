package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class ItemMapper {

    public static Item mapDtoToItem(ItemDto itemDto, User user) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                .build();
    }

    public static ItemDto mapItemToDto(Item item) {
        ItemDto dto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .build();
        if (item.getItemRequest() != null) {
            dto.setRequestId(item.getItemRequest().getId());
        }
        return dto;
    }

    public static ItemDtoWithBookingAndComments mapItemToExtendDto(Item item, BookingDto last, BookingDto next) {
        ItemDtoWithBookingAndComments dto = ItemDtoWithBookingAndComments.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwner().getId())
                .lastBooking(last)
                .nextBooking(next)
                .build();
        if (item.getComments() != null) {
            dto.setComments(item.getComments().stream().map(CommentMapper::mapCommentToDto).toList());
        }
        return dto;

    }

}
