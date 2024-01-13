import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orderpackage.Order;
import orderpackage.OrderClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userpackage.User;
import userpackage.UserClient;
import userpackage.UserLogin;

import static orderpackage.OrderGenerator.*;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static userpackage.UserGenerator.randomUser;

public class OrderTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String fullToken;
    private boolean success;
    private String message;
    private String fakeFullToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа авторизованным пользователем с ингредиентами, статус ответ 200")
    public void createOrderWithAuth() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);

        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");

        Order order = defaultOrder();
        OrderClient orderClient = new OrderClient();

        Response orderResponse = orderClient.create(order, fullToken);
        success = orderResponse.path("success");
        assertEquals("Неверный статус код", SC_OK, orderResponse.statusCode());
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа авторизованным пользователем без ингредиентов, статус ответ 400")
    public void createOrderWithoutIngredients() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);

        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");

        Order order = orderWithoutIngredients();
        OrderClient orderClient = new OrderClient();

        Response orderResponse = orderClient.create(order, fullToken);
        success = orderResponse.path("success");
        message = orderResponse.path("message");

        assertEquals("Неверный статус код", SC_BAD_REQUEST, orderResponse.statusCode());
        assertEquals("Неверное сообщение об ошибке", "Ingredient ids must be provided", message);
        assertEquals(false, success);
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа авторизованным пользователем с неверным хэшем ингредиентов, статус ответ 500")
    public void createOrderWithWrongHashIngredients() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);

        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");

        Order order = orderWithRandomHash();
        OrderClient orderClient = new OrderClient();

        Response orderResponse = orderClient.create(order, fullToken);

        assertEquals("Неверный статус код", SC_INTERNAL_SERVER_ERROR, orderResponse.statusCode());
    }


    @Test
    @DisplayName("Создание заказа")
    @Description("Создание заказа неавторизованным пользователем c ингредиентами, статус ответ 200")
    public void createOrderWithoutAuth() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);
        fullToken = response.path("accessToken");

        fakeFullToken = "";

        Order order = defaultOrder();
        OrderClient orderClient = new OrderClient();

        Response orderResponse = orderClient.create(order, fakeFullToken);
        success = orderResponse.path("success");

        assertEquals("Неверный статус код", SC_OK, orderResponse.statusCode());
        assertEquals(true, success);
    }

    @After
    public void deleteUser(){
        UserClient userClient = new UserClient();
        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());
    }
}
