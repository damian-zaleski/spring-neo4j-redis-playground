package pl.degath.springneo4jredisplayground.adapters;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import pl.degath.springneo4jredisplayground.item.Item;
import pl.degath.springneo4jredisplayground.item.infrastructure.ItemRepository;

import java.util.Optional;

@Repository
public interface ItemSpringNeo4jRepository extends Neo4jRepository<Item, String>, ItemRepository {

    @Override
    default Optional<Item> findByName(String name) {
        return this.findAll().stream()
                .filter(item -> item.getName().equals(name))
                .findFirst();
    }
}
