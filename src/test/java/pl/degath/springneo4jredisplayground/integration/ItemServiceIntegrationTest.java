package pl.degath.springneo4jredisplayground.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import pl.degath.springneo4jredisplayground.item.AddItem;
import pl.degath.springneo4jredisplayground.item.Item;
import pl.degath.springneo4jredisplayground.item.ItemService;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemAPI;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ItemServiceIntegrationTest {

    private static Neo4jContainer<?> neo4jContainer;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemAPI itemAPI;

    @BeforeAll
    static void beforeAll() {
        neo4jContainer = new Neo4jContainer<>("neo4j")
                .withAdminPassword("somePassword");
        neo4jContainer.start();
    }

    @AfterAll
    static void stopNeo4j() {
        neo4jContainer.close();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", neo4jContainer::getAdminPassword);
    }

    @Test
    void shouldAddItem() {
        var itemAdded = itemAPI.addItem(new AddItem("John", 5.5D));

        assertThat(itemAdded).isNotNull();
    }

    @Test
    void shouldGetItemByName() {
        itemRepository.save(new Item(UUID.randomUUID().toString(), "John", 5.5D));

        var item = itemAPI.getByName("John");

        assertThat(item).isNotNull();
    }
}
