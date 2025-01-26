package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto postItem(long userId, ItemDto itemDto) {
        if (userStorage.getUsers().stream().map(User::getId).toList().contains(userId)) {
            return ItemMapper.mapItemToDto(itemStorage.postItem(ItemMapper.mapDtoToItem(itemDto, userId)));
        } else {
            throw new NotFoundException("Пользователя с таким id нет");
        }
    }

    @Override
    public ItemDto patchItem(long userId, ItemDto itemDto, long itemId) {
        if (!userStorage.getUsers().stream().map(User::getId).toList().contains(userId)) {
            throw new NotFoundException("Пользователя с таким id нет");
        }
        Item item = ItemMapper.mapDtoToItem(itemDto, userId);
        item.setOwnerId(userId);
        Item itemFromMemory = itemStorage.getItems().stream().filter(it -> it.getId() == itemId)
                .findAny().orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        if (item.getName() == null) {
            item.setName(itemFromMemory.getName());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemFromMemory.getDescription());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemFromMemory.getAvailable());
        }
        return ItemMapper.mapItemToDto(itemStorage.patchItem(item));

    }

    @Override
    public ItemDto getItem(long itemId) {
        return ItemMapper.mapItemToDto(itemStorage.getItems().stream().filter(it -> it.getId() == itemId)
                .findAny().orElseThrow(() -> new NotFoundException("Пользователя с таким id нет")));
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        return itemStorage.getItemsOfOwner(userId)
                .stream()
                .map(ItemMapper::mapItemToDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemStorage.searchItems(text).stream().map(ItemMapper::mapItemToDto).toList();
    }
}
