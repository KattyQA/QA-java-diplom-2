import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import userpackage.User;
import userpackage.UserClient;
import userpackage.UserLogin;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static userpackage.UserGenerator.randomUser;

public class UserProfileTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private String fullToken;
    private String token;
    private boolean success;
    private User user;
    private UserClient userClient;
    private Faker faker;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        user = randomUser();
        userClient = new UserClient();
        faker = new Faker();
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование email авторизованного пользователя, статус ответа 200")
    public void editEmailInUserProfile() {
        Response response = userClient.create(user);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");
        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
        String email = faker.internet().emailAddress();
        String newEmail = "{\"email\": \"" + email + "\"}";
        Response editResponse = userClient.editEmail(fullToken, newEmail);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_OK, editResponse.statusCode());
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование имени авторизованного пользователя, статус ответа 200")
    public void editNameInUserProfile() {
        Response response = userClient.create(user);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");
        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
        String name = faker.name().name();
        String newName = "{\"name\": \"" + name + "\"}";
        Response editResponse = userClient.editName(fullToken, newName);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_OK, editResponse.statusCode());
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование пароля авторизованного пользователя, статус ответа 200")
    public void editPasswordInUserProfile() {
        Response response = userClient.create(user);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");
        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
        String password = faker.letterify("????????");
        String newPassword = "{\"password\": \"" + password + "\"}";
        Response editResponse = userClient.editPassword(fullToken, newPassword);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_OK, editResponse.statusCode());
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование всех данных авторизованного пользователя, статус ответа 200")
    public void editAllDataInUserProfile() {
        Response response = userClient.create(user);
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        Response loginResponse = userClient.login(UserLogin.fromUser(user));
        fullToken = loginResponse.path("accessToken");
        assertEquals("Неверный статус код", SC_OK, loginResponse.statusCode());
        String password = faker.letterify("????????");
        String name = faker.name().name();
        String email = faker.internet().emailAddress();
        String data = "{\"email\": \"" + email + "\"\", \"name\": \"" + name + "\"\",\"password\": \"" + password + "\"}";
        Response editResponse = userClient.editAllData(fullToken, data);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_OK, editResponse.statusCode());
        assertEquals(true, success);
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование email неавторизованного пользователя, статус ответа 401")
    public void editEmailInUserProfileWithoutAuth() {
        Response response = userClient.create(user);
        fullToken = response.path("accessToken");
        token = "";
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        String email = faker.internet().emailAddress();
        String newEmail = "{\"email\": \"" + email + "\"}";
        Response editResponse = userClient.editEmail(token, newEmail);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_UNAUTHORIZED, editResponse.statusCode());
        assertEquals(false, success);
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование имени неавторизованного пользователя, статус ответа 401")
    public void editNameInUserProfileWithoutAuth() {
        Response response = userClient.create(user);
        fullToken = response.path("accessToken");
        token = "";
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        String name = faker.name().name();
        String newName = "{\"name\": \"" + name + "\"}";
        Response editResponse = userClient.editName(token, newName);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_UNAUTHORIZED, editResponse.statusCode());
        assertEquals(false, success);
    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование пароля неавторизованного пользователя, статус ответа 401")
    public void editPasswordInUserProfileWithoutAuth() {
        Response response = userClient.create(user);
        fullToken = response.path("accessToken");
        token = "";
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        String password = faker.letterify("????????");
        String newPassword = "{\"password\": \"" + password + "\"}";
        Response editResponse = userClient.editPassword(token, newPassword);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_UNAUTHORIZED, editResponse.statusCode());
        assertEquals(false, success);

    }

    @Test
    @DisplayName("Редактирование профиля пользователя")
    @Description("Редактирование всех данных неавторизованного пользователя, статус ответа 401")
    public void editAllDataInUserProfileWithoutAuth() {
        Response response = userClient.create(user);
        fullToken = response.path("accessToken");
        token = "";
        assertEquals("Неверный статус код", SC_OK, response.statusCode());
        String password = faker.letterify("????????");
        String name = faker.name().name();
        String email = faker.internet().emailAddress();
        String data = "{\"email\": \"" + email + "\"\", \"name\": \"" + name + "\"\",\"password\": \"" + password + "\"}";
        Response editResponse = userClient.editAllData(token, data);
        success = editResponse.path("success");
        assertEquals("Неверный статус код", SC_UNAUTHORIZED, editResponse.statusCode());
        assertEquals(false, success);
    }

    @After
    public void deleteUser(){
        Response delete = userClient.delete(fullToken);
        assertEquals("Неверный статус код", SC_ACCEPTED, delete.statusCode());
    }
}