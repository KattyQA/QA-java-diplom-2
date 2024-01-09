import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import userpackage.User;
import userpackage.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static userpackage.UserGenerator.*;

public class UserTests {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";


    private String fullToken;
    private String token;
    private boolean success;
    private String message;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя с валидными данными, статус ответ 200")
    public void createUser() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        success = response.path("success");
        fullToken = response.path("accessToken");


        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        assertEquals(true, success);

        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());

    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя, который уже зарегистрирован, статус ответа 403")
    public void createRegisterUser() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        fullToken = response.path("accessToken");

        Response response1 = userClient.create(user);
        success = response1.path("success");
        message = response1.path("message");
        assertEquals("Неверный статус код", SC_FORBIDDEN, response1.statusCode());
        assertEquals("Неверное сообщение об ошибке", "User already exists", message);
        assertEquals(false, success);

        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());

    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя без email, статус ответа 403")
    public void createUserWithoutEmail() {
        User user = userWithoutEmail();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        success = response.path("success");
        message = response.path("message");
        assertEquals("Неверный статус код", SC_FORBIDDEN, response.statusCode());
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", message);
        assertEquals(false, success);

    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя без пароля, статус ответа 403")
    public void createUserWithoutPassword() {
        User user = userWithoutPassword();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        success = response.path("success");
        message = response.path("message");
        assertEquals("Неверный статус код", SC_FORBIDDEN, response.statusCode());
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", message);
        assertEquals(false, success);

    }

    @Test
    @DisplayName("Создание пользователя")
    @Description("Создание пользователя без имени, статус ответа 403")
    public void createUserWithoutName() {
        User user = userWithoutName();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        success = response.path("success");
        message = response.path("message");
        assertEquals("Неверный статус код", SC_FORBIDDEN, response.statusCode());
        assertEquals("Неверное сообщение об ошибке", "Email, password and name are required fields", message);
        assertEquals(false, success);

    }

}
