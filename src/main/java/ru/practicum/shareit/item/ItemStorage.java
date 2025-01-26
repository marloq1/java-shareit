package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemStorage {

    Item postItem(Item item);

    Item patchItem(Item item);

    Collection<Item> getItems();

    Collection<Item> getItemsOfOwner(long userId);

    List<Item> searchItems(String text);
}
