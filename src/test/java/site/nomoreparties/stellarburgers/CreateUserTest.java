package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.CreateUserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.clients.UserClient.createUser;
import static site.nomoreparties.stellarburgers.clients.UserClient.deleteUser;

@DisplayName("POST /api/auth/register | Создание пользователя")
public class CreateUserTest {
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        Faker faker = new Faker();
        email = faker.internet().safeEmailAddress();
        password = faker.internet().password();
        name = faker.name().firstName();
        System.out.printf("========= Test values =========%nemail: %s%npassword: %s%nname: %s%ntoken: %s%n%n", email, password, name, accessToken);
    }

    @DisplayName("Создание уникального пользователя")
    @Test
    public void createUserTest() {

        Response response = createUser(new CreateUserRequest(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");

        response
                .then()
                .statusCode(SC_CREATED)
                .body("success", equalTo(true));
    }

    @DisplayName("Попытка создания уже существующего пользователя")
    @Test
    public void createExistingUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest(email, password, name);

        accessToken = createUser(createUserRequest)
                .jsonPath()
                .getString("accessToken");

        createUser(createUserRequest)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @DisplayName("Попытка создания пользователя с пустым полем email")
    @Test
    public void createUserWithEmptyEmailTest() {
        email = "";
        Response response = createUser(new CreateUserRequest(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");

        response
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Попытка создания пользователя с пустым полем password")
    @Test
    public void createUserWithEmptyPasswordTest() {
        password = "";
        Response response = createUser(new CreateUserRequest(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");

        response
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @DisplayName("Попытка создания пользователя с пустым полем name")
    @Test
    public void createUserWithEmptyNameTest() {
        name = "";
        Response response = createUser(new CreateUserRequest(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");

        response
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }
}