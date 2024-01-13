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

import static orderpackage.OrderList.defaultOrder;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static userpackage.UserGenerator.randomUser;

public class OrderListTests {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String fullToken;
    private String fakeFullToken;
    private String number;
    private String numberOrder;
    private boolean success;
    private String message;
    private UserClient userClient;
    private User user;
    private OrderClient orderClient;
    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = BASE_URL;
        user = randomUser();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов конкретного пользователя с авторизацией, статус ответ 200")
    public void createOrderWithAuth() {
        Response response = userClient.create(user);
        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");
        Order order = defaultOrder();
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
        Response response = userClient.create(user);
        fullToken = response.path("accessToken");
        fakeFullToken = "";
        Response orderListResponse = orderClient.getOrderList(fakeFullToken);
        success = orderListResponse.path("success");
        message = orderListResponse.path("message");
        assertEquals("Неверный статус код", SC_UNAUTHORIZED, orderListResponse.statusCode());
        assertEquals(false, success);
        assertEquals("Неверное сообщение об ошибке", "You should be authorised", message);
    }

    @After
    public void deleteUser(){
        UserClient userClient = new UserClient();
        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());
    }
}
