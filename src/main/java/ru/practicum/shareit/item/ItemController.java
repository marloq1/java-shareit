package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;


    @PostMapping
    public ItemDto postItem(@RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody ItemDto itemDto) {
        log.info("Добавление нового предмета пользователем с id {}",userId);
        return itemService.postItem(userId,itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto,
                             @PathVariable long itemId) {
        log.info("Изменение предмета пользователем с id {}",userId);
        return itemService.patchItem(userId, itemDto, itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookingAndComments> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение списка предметов пользователя с id {}",userId);
        return itemService.getItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable long itemId) {
        log.info("Получение предмета с id {}",itemId);
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        log.info("Поиск предметов по комбинации {}",text);
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader(value = "X-Sharer-User-Id", required = false) Long userId,
                                  @PathVariable long itemId,@RequestBody CommentDto commentDto) {
        log.info("Добавление комментария для предмета с id {}",itemId);
        return itemService.postComment(userId,itemId,commentDto);
    }
}
