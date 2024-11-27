package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.AuthorizeUserRequest;
import site.nomoreparties.stellarburgers.models.CreateUserRequest;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.clients.UserClient.*;

@DisplayName("POST /api/auth/login | Авторизация пользователя")
public class AuthorizeUserTest {
    private final Faker faker = new Faker();
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        email = faker.internet().safeEmailAddress();
        password = faker.internet().password();
        String name = faker.name().firstName();
        Response response = createUser(new CreateUserRequest(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");
        System.out.printf("========= Test values =========%nemail: %s%npassword: %s%nname: %s%ntoken: %s%n%n", email, password, name, accessToken);

    }

    @DisplayName("Логин под существующим пользователем")
    @Test()
    public void authorizeViaCreatedUserTest() {
        authorizeUser(new AuthorizeUserRequest(email, password))
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @DisplayName("Попытка авторизации с неверным полем email")
    @Test
    public void authorizeUserWithWrongEmailTest() {
        String wrongEmail = faker.internet().emailAddress();
        authorizeUser(new AuthorizeUserRequest(wrongEmail, password))
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @DisplayName("Попытка авторизации с неверным полем password")
    @Test
    public void authorizeUserWithWrongPasswordTest() {
        String wrongPassword = faker.internet().password();
        authorizeUser(new AuthorizeUserRequest(email, wrongPassword))
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }
}