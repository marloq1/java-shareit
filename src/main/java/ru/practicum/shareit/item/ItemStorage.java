package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemStorage {

    Item postItem(Item item);

    Item patchItem(Item item);

    Collection<Item> getItems();

    List<Item> searchItems(String text);
}
