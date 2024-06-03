package diplom2.constants;

public class Constants {

    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    // url пользователя
    public static final String USER_CREATE_URL = "/api/auth/register";
    public static final String USER_LOGIN_URL = "/api/auth/login";
    public static final String USER_DELETE_PATCH_URL = "/api/auth/user";
    // url заказов
    public static final String GET_INGREDIENTS_URL = "/api/ingredients";
    public static final String GET_CREATE_ORDER_URL = "/api/orders";
    // сообщения ошибок ответов для пользователя
    public static final String SAME_COURIER_MESSAGE = "User already exists";
    public static final String WITHOUT_FIELD_MESSAGE = "Email, password and name are required fields";
    public static final String INCORRECT_CRED_MESSAGE = "email or password are incorrect";
    public static final String PATCH_WO_TOKEN_MESSAGE = "You should be authorised";
    // сообщения ошибок ответов для заказов
    public static final String CREATE_ORDER_BR_MESSAGE ="Ingredient ids must be provided";
    public static final String GET_ORDER_401_MESSAGE = "You should be authorised";
    // данные, используемые для получения ошибок
    public static final String INCORRECT_EMAIL = "incorrectEmail";
    public static final String INCORRECT_PASSWORD = "incorrectPassword";
    public static final String EMPTY_TOKEN = "";

}
