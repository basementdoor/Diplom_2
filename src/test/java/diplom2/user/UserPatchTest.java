package diplom2.user;

import diplom2.models.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static diplom2.constants.Constants.EMPTY_TOKEN;
import static diplom2.constants.Constants.PATCH_WO_TOKEN_MESSAGE;
import static diplom2.user.UserGenearator.randomUser;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class UserPatchTest extends UserBeforeAndAfter {

    @Test
    @DisplayName("Успешное обновление всех данных пользователя с токеном")
    @Description("Ожидаем код 200, вернулись данные нового пользователя")
    public void patchUserWithTokenTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));
        accessToken = response.path("accessToken");

        User patchUser = randomUser();

        Response patchUserResponse = userClient.userPatch(patchUser, accessToken);
        assertEquals("Код не соотвествует ожидаемому 200"
                , SC_OK, patchUserResponse.statusCode());
        assertEquals("Email не обновился"
                , patchUser.getEmail(), patchUserResponse.path("user.email"));
        assertEquals("Имя пользователя не обновилось", patchUser.getName(), patchUserResponse.path("user.name"));

    }

    @Test
    @DisplayName("Ошибка при попытке обновления данных пользователя без передачи токена")
    @Description("Ожидаем код 401, соответствующее сообщение об ошибке")
    public void patchUserWithoutTokenTest() {

        User user = randomUser();
        Response response = userClient.userCreate(user);

        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals("Неверное тело ответа", true, response.path("success"));
        accessToken = response.path("accessToken");

        User patchUser = randomUser();

        Response patchUserResponse = userClient.userPatch(patchUser, EMPTY_TOKEN);
        assertEquals("Код не соотвествует ожидаемому 401"
                , SC_UNAUTHORIZED, patchUserResponse.statusCode());
        assertEquals("Ожидается другое сообщение"
                , PATCH_WO_TOKEN_MESSAGE, patchUserResponse.path("message"));
    }
}
