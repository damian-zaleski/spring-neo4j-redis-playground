package pl.degath.springneo4jredisplayground.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;
import pl.degath.springneo4jredisplayground.item.AddItem;
import pl.degath.springneo4jredisplayground.item.Item;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemAPI;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ItemServiceIntegrationTest {

    private static Neo4jContainer<?> neo4jContainer;
    private static GenericContainer<?> redisContainer;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemAPI itemAPI;

    @BeforeAll
    static void beforeAll() {
        redisContainer = new GenericContainer<>(DockerImageName.parse("redis"))
                .withExposedPorts(6379);
        redisContainer.start();

        neo4jContainer = new Neo4jContainer<>("neo4j")
                .withAdminPassword("somePassword");
        neo4jContainer.start();
    }

    @AfterAll
    static void stopNeo4j() {
        neo4jContainer.close();
        redisContainer.close();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.redis.host", () -> redisContainer.getContainerIpAddress());
        registry.add("spring.redis.port", () -> redisContainer.getFirstMappedPort());
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
