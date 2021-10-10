package pl.degath.springneo4jredisplayground.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.utility.DockerImageName;
import pl.degath.springneo4jredisplayground.item.AddItem;
import pl.degath.springneo4jredisplayground.item.Item;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemAPI;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ItemServiceIntegrationTest {

    private static final Item AN_ITEM = new Item(UUID.randomUUID().toString(), "John", 5.5D);
    private static Neo4jContainer<?> neo4jContainer;
    private static GenericContainer<?> redisContainer;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemAPI itemAPI;

    @Autowired
    private CacheManager cacheManager;

    @BeforeAll
    static void beforeAll() {
        redisContainer = new GenericContainer<>(DockerImageName.parse("redis"))
                .withExposedPorts(6379);
        redisContainer.start();

        neo4jContainer = new Neo4jContainer<>("neo4j")
                .withAdminPassword("somePassword");
        neo4jContainer.start();
    }

    @BeforeEach
    void setUp() {
        cacheManager.getCache("itemCache").clear();
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
        itemRepository.save(AN_ITEM);

        var item = itemAPI.getByName("John");

        assertThat(item).isNotNull();
    }


    @Test
    void shouldCacheItem() {
        itemRepository.save(AN_ITEM);

        itemAPI.getByName("John");

        Item anItemFromCache = Objects.requireNonNull(cacheManager.getCache("itemCache")).get("John", Item.class);
        assertThat(anItemFromCache)
                .satisfies(item -> {
                            assertThat(item.getId()).isNotNull();
                            assertThat(item.getName()).isEqualTo("John");
                            assertThat(item.getValue()).isEqualTo(5.5D);
                        }
                );
    }
}
