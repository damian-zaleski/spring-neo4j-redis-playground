package pl.degath.springneo4jredisplayground.item;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemAPI;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class ItemService implements ItemAPI {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item addItem(AddItem addItemCommand) {
        return this.findItem(addItemCommand)
                .orElse(this.createNewItem(addItemCommand));
    }

    private Optional<Item> findItem(AddItem command) {
        return itemRepository.findByName(command.name());
    }

    private Item createNewItem(AddItem addItemCommand) {
        Item item = new Item(UUID.randomUUID().toString(), addItemCommand.name(), addItemCommand.value());
        return itemRepository.save(item);
    }

    @Override
    @Cacheable(value = "itemCache")
    public Item getByName(String name) {
        return itemRepository.findByName(name)
                .orElseThrow();
    }
}
