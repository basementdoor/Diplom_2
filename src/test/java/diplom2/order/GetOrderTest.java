package diplom2.order;

import diplom2.models.Order;
import diplom2.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static diplom2.constants.Constants.EMPTY_TOKEN;
import static diplom2.constants.Constants.GET_ORDER_401_MESSAGE;
import static diplom2.order.OrderGenerator.correctOrder;
import static diplom2.user.UserGenearator.randomUser;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GetOrderTest extends OrderBeforeAndAfter {

    @Test
    @DisplayName("Ошибка при получении списка заказов неавторизованным пользователем")
    @Description("Ожидаем код 401, соответствующее сообщение об ошибке")
    public void getOrderWithoutTokenTest() {

        Response getOrderResponse = orderClient.orderGet(EMPTY_TOKEN);
        assertEquals("Неверный статус код", SC_UNAUTHORIZED, getOrderResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке", GET_ORDER_401_MESSAGE, getOrderResponse.path("message"));

    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("Ожидаем код 200, в ответе есть оформленные заказы")
    public void getOrderWithTokenTest() {
        // создаем пользователя
        User user = randomUser();
        Response userResponse = userClient.userCreate(user);
        assertEquals("Неверный статус код", SC_OK, userResponse.statusCode());
        assertEquals("Неверное тело ответа", true, userResponse.path("success"));
        accessToken = userResponse.path("accessToken");
        // создаем заказ
        Response getIngredientResponse = orderClient.getIngredients();
        assertEquals("Ожидался код 200", SC_OK, getIngredientResponse.statusCode());
        assertNotNull("Не удалось получить данные ингредиентов", getIngredientResponse.path("data"));
        List<String> ingredients = orderClient.setIngredients(getIngredientResponse);
        Order order = correctOrder(ingredients);
        Response orderResponse = orderClient.orderCreateWithAuth(order, accessToken);
        assertEquals("Неверный статус код", SC_OK, orderResponse.statusCode());
        assertNotNull("Номер заказа отсутствует", orderResponse.path("order.number"));
        // проверка получения списка заказов
        List<Integer> expectedOrderNumber = new ArrayList<>();
        expectedOrderNumber.add(orderResponse.path("order.number"));
        Response getOrderResponse = orderClient.orderGet(accessToken);
        assertEquals("Неверный статус код", SC_OK, getOrderResponse.statusCode());
        assertEquals("В списке нет оформленного заказа", expectedOrderNumber, getOrderResponse.path("orders.number"));
    }
}
