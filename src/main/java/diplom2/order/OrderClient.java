package diplom2.order;

import diplom2.models.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;

import static diplom2.constants.Constants.*;
import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Получение ингредиентов")
    public Response getIngredients() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(GET_INGREDIENTS_URL);
    }

    @Step("Создание списка ингредиентов, которые будут переданы в заказ")
    public List<String> setIngredients(Response response) {
        List<String> ingredients = new ArrayList<>();
        String ingredient1 = response.path(String.format("data[%s]._id", 0));
        String ingredient2 = response.path(String.format("data[%s]._id", 1));
        String ingredient3 = response.path(String.format("data[%s]._id", 2));
        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);
        return ingredients;
    }

    @Step("Создание заказа неавторизованным пользователем")
    public Response orderCreateWithAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(GET_CREATE_ORDER_URL);
    }

    @Step("Создание заказа авторизованным пользователем")
    public Response orderCreateWithAuth(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(GET_CREATE_ORDER_URL);
    }

    @Step("Получение заказов конкретного пользователя")
    public Response orderGet(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .get(GET_CREATE_ORDER_URL);
    }

}
