package pl.degath.springneo4jredisplayground.item;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import pl.degath.springneo4jredisplayground.item.infrastructure.NodeRoot;

@Node
public class Item implements NodeRoot {

    @Id
    private final String id;
    private final String name;
    private final Double value;

    @JsonCreator
    public Item(@JsonProperty("id") String id,
                @JsonProperty("name") String name,
                @JsonProperty("value") Double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getValue() {
        return value;
    }
}