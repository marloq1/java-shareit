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
    public List<Item> searchItems(String text) {
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()))
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
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
