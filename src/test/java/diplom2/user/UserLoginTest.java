package diplom2.user;

import diplom2.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static diplom2.constants.Constants.*;
import static diplom2.user.UserGenearator.randomUser;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class UserLoginTest extends UserBeforeAndAfter {

    @Test
    @DisplayName("Успешная авторизация существующим пользователем")
    @Description("Ожидаем код 200, получили токен")
    public void logInUserTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));
        accessToken = response.path("accessToken");

        User logInUser = new User()
                .withEmail(user.getEmail())
                .withPassword(user.getPassword());

        Response logInUserResponse = userClient.userLogIn(logInUser);
        assertEquals("Код не соотвествует ожидаемому 200"
                , SC_OK, logInUserResponse.statusCode());
        assertEquals("Ожидалось true"
                , true, logInUserResponse.path("success"));
        assertNotNull("Не получили токен", logInUserResponse.path("accessToken"));

    }

    @Test
    @DisplayName("Ошибка авторизации с несуществующим пользователем")
    @Description("Ожидаем код 401, соответствующее сообщение об ошибке")
    public void logInIncorrectCredUserTest() {

        User incorrectUser = new User()
                .withEmail(INCORRECT_EMAIL)
                .withPassword(INCORRECT_PASSWORD);

        Response incorrectUserResponse = userClient.userLogIn(incorrectUser);
        assertEquals("Код не соотвествует ожидаемому 401"
                , SC_UNAUTHORIZED, incorrectUserResponse.statusCode());
        assertEquals("Ожидалось другое сообщение"
                , INCORRECT_CRED_MESSAGE, incorrectUserResponse.path("message"));

    }

    @Test
    @DisplayName("Ошибка авторизации при отсутствии email")
    @Description("Ожидаем код 401, соответствующее сообщение об ошибке")
    public void logInWithoutEmailUserTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));
        accessToken = response.path("accessToken");

        User logInWoEmailUser = new User()
                .withPassword(user.getPassword());

        Response logInWoEmailUserResponse = userClient.userLogIn(logInWoEmailUser);
        assertEquals("Код не соотвествует ожидаемому 401"
                , SC_UNAUTHORIZED, logInWoEmailUserResponse.statusCode());
        assertEquals("Ожидалось другое сообщение"
                , INCORRECT_CRED_MESSAGE, logInWoEmailUserResponse.path("message"));

    }

    @Test
    @DisplayName("Ошибка авторизации при отсутствии пароля")
    @Description("Ожидаем код 401, соответствующее сообщение об ошибке")
    public void logInWithoutPasswordUserTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));
        accessToken = response.path("accessToken");

        User logInWoPasswordUser = new User()
                .withEmail(user.getEmail());

        Response logInWoPasswordUserResponse = userClient.userLogIn(logInWoPasswordUser);
        assertEquals("Код не соотвествует ожидаемому 401"
                , SC_UNAUTHORIZED, logInWoPasswordUserResponse.statusCode());
        assertEquals("ОжиÍдалось другое сообщение"
                , INCORRECT_CRED_MESSAGE, logInWoPasswordUserResponse.path("message"));

    }

}
