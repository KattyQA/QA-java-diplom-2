import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import orderpackage.Order;
import orderpackage.OrderClient;
import org.junit.Before;
import org.junit.Test;
import userpackage.User;
import userpackage.UserClient;
import userpackage.UserLogin;

import static orderpackage.OrderGenerator.defaultOrder;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static userpackage.UserGenerator.randomUser;

public class OrderListTests {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";


    private String fullToken;

    private String number;

    private String numberOrder;

    private String token;
    private boolean success;
    private String message;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов конкретного пользователя с авторизацией, статус ответ 200")
    public void createOrderWithAuth() {
        User user = randomUser();
        UserClient userClient = new UserClient();

        Response response = userClient.create(user);

        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");

        Order order = defaultOrder();
        OrderClient orderClient = new OrderClient();

        Response orderResponse = orderClient.create(order, fullToken);
        number = orderResponse.path("number");

        Response orderListResponse = orderClient.getOrderList(fullToken);
        numberOrder = orderListResponse.path("number");
        success = orderListResponse.path("success");

        assertEquals("Неверный статус код", SC_OK, orderListResponse.statusCode());
        assertEquals(true, success);
        assertEquals(number, numberOrder);


    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов конкретного пользователя без авторизации, статус ответ 401")
    public void createOrderWithoutAuth() {
        User user = randomUser();
        UserClient userClient = new UserClient();
        OrderClient orderClient = new OrderClient();

        Response response = userClient.create(user);

        fullToken = "";

        Response orderListResponse = orderClient.getOrderList(fullToken);
        success = orderListResponse.path("success");
        message = orderListResponse.path("message");

        assertEquals("Неверный статус код", SC_UNAUTHORIZED, orderListResponse.statusCode());
        assertEquals(false, success);
        assertEquals("Неверное сообщение об ошибке", "You should be authorised", message);


    }
}
