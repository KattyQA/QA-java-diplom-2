package userpackage;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String USER_URL = "/api/auth/register";
    private static final String USER_LOGIN_URL = "/api/auth/login";
    private static final String USER_PROFILE_URL = "/api/auth/user";
    private static final String DELETE_USER_URL = "/api/auth/user";

    @Step("Создание пользователя {user}")
    public Response create(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(USER_URL);
    }

    @Step("Удаление пользователя")
    public Response delete(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .delete(DELETE_USER_URL);

    }

    @Step("Авторизация пользователя {userLogin}")
    public Response login(UserLogin userLogin) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(userLogin)
                .when()
                .post(USER_LOGIN_URL);

    }

    @Step("Изменение данных электронной почты в профиле пользователя")
    public Response editEmail(String token, String newEmail) {
        return given()
                .header("Authorization", token)
                .and()
                .body(newEmail)
                .when()
                .patch(USER_PROFILE_URL);

    }

    @Step("Изменение данных имени в профиле пользователя")
    public Response editName(String token, String newName) {
        return given()
                .header("Authorization", token)
                .and()
                .body(newName)
                .when()
                .patch(USER_PROFILE_URL);

    }

    @Step("Изменение данных пароля в профиле пользователя")
    public Response editPassword(String token, String newPassword) {
        return given()
                .header("Authorization", token)
                .and()
                .body(newPassword)
                .when()
                .patch(USER_PROFILE_URL);

    }

    @Step("Изменение всех данных в профиле пользователя")
    public Response editAllData(String token, String data) {
        return given()
                .header("Authorization", token)
                .and()
                .body(data)
                .when()
                .patch(USER_PROFILE_URL);

    }

}
