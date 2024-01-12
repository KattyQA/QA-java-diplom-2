import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import userpackage.User;
import userpackage.UserClient;
import userpackage.UserLogin;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static userpackage.UserGenerator.randomUser;

public class UserLoginTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";


    private String fullToken;
    private boolean success;
    private String message;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация существующим пользователем, статус ответ 200")
    public void loginUser() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        success = response.path("success");
        fullToken = response.path("accessToken");

        Response responseLogin = userClient.login(UserLogin.fromUser(user));

        assertEquals("Неверный статус код", SC_OK, responseLogin.statusCode());
        assertEquals(true, success);

        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());
    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя с неверным логином, статус ответ 401")
    public void loginUserWithWrongLogin() {
        Faker faker = new Faker();
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        fullToken = response.path("accessToken");

        user.setEmail(faker.internet().emailAddress());

        Response responseLogin = userClient.login(UserLogin.fromUser(user));
        message = responseLogin.path("message");
        success = responseLogin.path("success");

        assertEquals("Неверный статус код", SC_UNAUTHORIZED, responseLogin.statusCode());
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", message);
        assertEquals(false, success);

        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());

    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя с неверным паролем, статус ответ 401")
    public void loginUserWithWrongPassword() {
        Faker faker = new Faker();
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        fullToken = response.path("accessToken");

        user.setPassword(faker.letterify("????????"));

        Response response1 = userClient.login(UserLogin.fromUser(user));
        message = response1.path("message");
        success = response1.path("success");

        assertEquals("Неверный статус код", SC_UNAUTHORIZED, response1.statusCode());
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", message);
        assertEquals(false, success);

        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());

    }

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя с неверным логином и паролем, статус ответ 401")
    public void loginUserWithWrongLoginAndPassword() {
        Faker faker = new Faker();
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        fullToken = response.path("accessToken");

        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.letterify("????????"));

        Response response1 = userClient.login(UserLogin.fromUser(user));
        message = response1.path("message");
        success = response1.path("success");

        assertEquals("Неверный статус код", SC_UNAUTHORIZED, response1.statusCode());
        assertEquals("Неверное сообщение об ошибке", "email or password are incorrect", message);
        assertEquals(false, success);

        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());

    }


}
