package diplom2.models;

import java.util.List;

public class Order {

    private List<String> ingredients;

    public List<String> getIngredients() {
        return ingredients;
    }

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
