package pl.degath.springneo4jredisplayground;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.degath.springneo4jredisplayground.adapters.ItemInMemoryRepository;
import pl.degath.springneo4jredisplayground.item.AddItem;
import pl.degath.springneo4jredisplayground.item.Item;
import pl.degath.springneo4jredisplayground.item.ItemService;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemAPI;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ItemServiceTest {

    ItemRepository itemRepository;
    ItemAPI itemAPI;

    @BeforeEach
    void setUp() {
        itemRepository = new ItemInMemoryRepository();
        itemAPI = new ItemService(itemRepository);
    }

    @Test
    void shouldAddItem() {
        var itemAdded = itemAPI.addItem(new AddItem("John", 5.5D));

        assertThat(itemAdded)
                .isNotNull();
    }

    @Test
    void shouldGetItemByName() {
        itemRepository.save(new Item(UUID.randomUUID().toString(), "John", 5.5D));

        var result = itemAPI.getByName("John");

        assertThat(result)
                .isNotNull()
                .satisfies(item -> {
                            assertThat(item.getName()).isEqualTo("John");
                            assertThat(item.getValue()).isEqualTo(5.5D);
                        }
                );
    }
}
