package pl.degath.springneo4jredisplayground.item.infrastructure;

import pl.degath.springneo4jredisplayground.item.Item;

import java.util.Optional;

public interface ItemRepository {

    Item save(Item item);

    Optional<Item> findByName(String name);
}
