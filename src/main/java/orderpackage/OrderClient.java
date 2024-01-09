package orderpackage;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_URL = "/api/orders";
    private static final String ORDER_LIST_URL = "/api/orders";

    @Step("Создание заказа c авторизацией {order}")
    public Response create(Order order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .body(order)
                .when()
                .post(ORDER_URL);
    }

    @Step("Получить список заказов {orderList}")
    public Response getOrderList(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .and()
                .when()
                .get(ORDER_LIST_URL);
    }

}
