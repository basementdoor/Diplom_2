package diplom2.user;

import diplom2.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static diplom2.constants.Constants.SAME_COURIER_MESSAGE;
import static diplom2.constants.Constants.WITHOUT_FIELD_MESSAGE;
import static diplom2.user.UserGenearator.randomUser;
import static diplom2.user.UserGenearator.userWithoutName;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class UserCreateTest extends UserBeforeAndAfter {

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    @Description("Ожидаем код 200, тело, успех в ответе")
    public void createUniqUserTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));

        accessToken = response.path("accessToken");

    }

    @Test
    @DisplayName("Ошибка при создании двух одинаковых пользователей")
    @Description("Ожидаем код 403, соответствующее сообщение об ошибке")
    public void createSameUserTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));
        accessToken = response.path("accessToken");

        User sameUser = new User()
                .withEmail(user.getEmail())
                .withPassword(user.getPassword())
                .withName(user.getName());

        Response sameUserResponse = userClient.userCreate(sameUser);
        assertEquals("Код не соотвествует ожидаемому 403"
                , SC_FORBIDDEN, sameUserResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке"
                , SAME_COURIER_MESSAGE, sameUserResponse.path("message"));

    }

    @Test
    @DisplayName("Ошибка при создании пользователя без обязательного поля")
    @Description("Ожидаем код 403, тело, соответствующее сообщение об ошибке")
    public void createUserWithoutRequiredFieldTest() {

        User user = userWithoutName();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_FORBIDDEN, response.statusCode());
        assertEquals("Неверное сообщение об ошибке", WITHOUT_FIELD_MESSAGE, response.path("message"));

        accessToken = response.path("accessToken");

    }



}
