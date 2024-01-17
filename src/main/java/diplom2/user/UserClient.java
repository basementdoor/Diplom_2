package diplom2.user;

import diplom2.models.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static diplom2.constants.Constants.*;
import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Создание пользователя")
    public Response userCreate(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USER_CREATE_URL);
    }

    @Step("Авторизация пользователем")
    public Response userLogIn(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USER_LOGIN_URL);
    }

    @Step("Обновление данных пользователя")
    public Response userPatch(User user, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(user)
                .when()
                .patch(USER_DELETE_PATCH_URL);
    }

    @Step("Удаление пользователя")
    public static void userDelete(String token) {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .delete(USER_DELETE_PATCH_URL);
    }

}
