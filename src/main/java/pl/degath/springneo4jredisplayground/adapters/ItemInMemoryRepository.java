package pl.degath.springneo4jredisplayground.adapters;

import pl.degath.springneo4jredisplayground.item.Item;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class ItemInMemoryRepository implements ItemRepository {

    private final Map<String, Item> database = new ConcurrentHashMap<>();

    @Override
    public Item save(Item item) {
        database.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findByName(String name) {
        return find(it -> it.getName().equals(name));
    }

    private Optional<Item> find(Predicate<Item> predicate) {
        return database.values().stream()
                .filter(predicate)
                .findFirst();
    }
}
