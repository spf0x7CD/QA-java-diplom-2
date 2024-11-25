package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.CreateUserRequest;

import java.util.PriorityQueue;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.clients.UserClient.*;
import static site.nomoreparties.stellarburgers.utils.Utils.buildKV;

@DisplayName("PATCH /api/auth/user | Обновление данных пользователя")
public class UpdateUserDataTest {
    private final Faker faker = new Faker();
    private final PriorityQueue<String> accessTokens = new PriorityQueue<>();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        String email = faker.internet().safeEmailAddress();
        String password = faker.internet().password();
        String name = faker.name().firstName();
        Response response = createUser(new CreateUserRequest(email, password, name));
        accessTokens.offer(response.jsonPath().getString("accessToken"));
        System.out.printf("========= Test values =========%nemail: %s%npassword: %s%nname: %s%ntoken: %s%n%n", email, password, name, accessTokens.peek());
    }

    @DisplayName("Изменение email пользователя с авторизацией")
    @Test
    public void updateUserEmailWithAuth() {
        String kv = buildKV("email", faker.internet().emailAddress());

        updateUser(accessTokens.peek(), kv)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @DisplayName("Изменение password пользователя с авторизацией")
    @Test
    public void updateUserPasswordWithAuth() {
        String kv = buildKV("password", faker.internet().password());

        updateUser(accessTokens.peek(), kv)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @DisplayName("Изменение name пользователя с авторизацией")
    @Test
    public void updateUserNameWithAuth() {
        String kv = buildKV("name", faker.name().firstName());
        updateUser(accessTokens.peek(), kv)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @DisplayName("Попытка изменения email пользователя без авторизации")
    @Test
    public void updateUserEmailWithoutAuth() {
        String kv = buildKV("email", faker.internet().emailAddress());
        updateUser("", kv)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @DisplayName("Попытка изменения password пользователя без авторизации")
    @Test
    public void updateUserPasswordWithoutAuth() {
        String kv = buildKV("password", faker.internet().password());
        updateUser("", kv)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @DisplayName("Попытка изменения name пользователя без авторизации")
    @Test
    public void updateUserNameWithoutAuth() {
        String kv = buildKV("name", faker.name().firstName());
        updateUser("", kv)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @DisplayName("Попытка изменения email пользователя на email, который уже используется")
    @Test
    public void updateUserEmailWithExistingEmail() {
        String emailPrev = faker.internet().emailAddress();
        String passwordPrev = faker.internet().password();
        String namePrev = faker.name().firstName();
        accessTokens.offer(createUser(new CreateUserRequest(emailPrev, passwordPrev, namePrev))
                .body()
                .jsonPath()
                .getString("accessToken")
        );

        String kv = buildKV("email", emailPrev);
        updateUser(accessTokens.peek(), kv)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

    @After
    public void tearDown() {
        while (!accessTokens.isEmpty()) deleteUser(accessTokens.poll());
    }
}