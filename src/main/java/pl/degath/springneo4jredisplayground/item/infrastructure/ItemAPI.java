package pl.degath.springneo4jredisplayground.item.infrastructure;

import pl.degath.springneo4jredisplayground.item.AddItem;
import pl.degath.springneo4jredisplayground.item.Item;

public interface ItemAPI {

    Item addItem(AddItem addItemCommand);

    Item getByName(String name);
}
