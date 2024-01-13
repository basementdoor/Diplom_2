package diplom2.order;

import diplom2.models.Order;

import java.util.List;

public class OrderGenerator {

    public static Order correctOrder(List<String> ingredients) {
        return new Order(ingredients);
    }
}
