package diplom2.order;

import diplom2.models.Order;
import diplom2.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static diplom2.constants.Constants.CREATE_ORDER_BR_MESSAGE;
import static diplom2.order.OrderGenerator.correctOrder;
import static diplom2.user.UserGenearator.randomUser;
import static diplom2.utils.Utils.faker;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CreateOrderTest extends OrderBeforeAndAfter {

    @Test
    @DisplayName("Создание заказа со списком хэшей, без авторизации")
    @Description("Ожидаем код 200, получили номер заказа")
    public void createOrderWithoutTokenTest() {
        Response response = orderClient.getIngredients();
        assertEquals("Ожидался код 200", SC_OK, response.statusCode());
        assertNotNull("Не удалось получить данные ингредиентов", response.path("data"));
        List<String> ingredients = orderClient.setIngredients(response);
        Order order = correctOrder(ingredients);
        Response orderResponse = orderClient.orderCreateWithAuth(order);
        assertEquals("Неверный статус код", SC_OK, orderResponse.statusCode());
        assertNotNull("Номер заказа отсутствует", orderResponse.path("order.number"));
    }

    @Test
    @DisplayName("Создание заказа без списка ингредиентов")
    @Description("Ожидаем код 400, соответствующее сообщение об ошибке")
    public void createOrderWithoutIngredientsTest() {
        List<String> ingredients = new ArrayList<>();
        Order order = correctOrder(ingredients);
        Response orderResponse = orderClient.orderCreateWithAuth(order);
        assertEquals("Неверный статус код", SC_BAD_REQUEST, orderResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке", CREATE_ORDER_BR_MESSAGE, orderResponse.path("message"));
    }

    @Test
    @DisplayName("Создание заказа с невалидным ингредиентом")
    @Description("Ожидаем код 500")
    public void createOrderWithIncorrectIngredientTest() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(faker.name().username());
        Order order = correctOrder(ingredients);
        Response orderResponse = orderClient.orderCreateWithAuth(order);
        assertEquals("Неверный статус код", SC_INTERNAL_SERVER_ERROR, orderResponse.statusCode());
    }

    @Test
    @DisplayName("Создание заказа со списком хэшей, c авторизацией")
    @Description("Ожидаем код 200, получили номер заказа")
    public void createOrderWithTokenTest() {
        User user = randomUser();
        Response userResponse = userClient.userCreate(user);
        assertEquals("Неверный статус код", SC_OK, userResponse.statusCode());
        assertEquals("Неверное тело ответа", true, userResponse.path("success"));
        accessToken = userResponse.path("accessToken");

        Response getIngredientResponse = orderClient.getIngredients();
        assertEquals("Ожидался код 200", SC_OK, getIngredientResponse.statusCode());
        assertNotNull("Не удалось получить данные ингредиентов", getIngredientResponse.path("data"));
        List<String> ingredients = orderClient.setIngredients(getIngredientResponse);
        Order order = correctOrder(ingredients);
        Response orderResponse = orderClient.orderCreateWithAuth(order, accessToken);
        assertEquals("Неверный статус код", SC_OK, orderResponse.statusCode());
        assertNotNull("Номер заказа отсутствует", orderResponse.path("order.number"));
    }
}
