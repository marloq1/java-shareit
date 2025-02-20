package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();


    @Override
    public Item postItem(Item item) {

        item.setId(getNextId());
        items.put(item.getId(), item);
        return items.get(item.getId());

    }

    @Override
    public Item patchItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());

    }

    @Override
    public Collection<Item> getItems() {
        return items.values();
    }

    @Override
    public Collection<Item> getItemsOfOwner(long userId) {
        //return getItems().stream().filter(item -> item.getOwnerId() == userId).toList();
        return null;
    }

    @Override
    public List<Item> searchItems(String text) {
        String lowText = text.toLowerCase();
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toLowerCase().contains(lowText))
                        || item.getDescription().toLowerCase().contains(lowText))
                .toList();

    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
