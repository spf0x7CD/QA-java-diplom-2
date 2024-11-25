package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import site.nomoreparties.stellarburgers.models.CreateUserRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.clients.OrderClient.createOrder;
import static site.nomoreparties.stellarburgers.clients.UserClient.createUser;
import static site.nomoreparties.stellarburgers.clients.UserClient.deleteUser;

@DisplayName("POST /api/orders | Создание заказа")
public class CreateOrderTest {

    private List<String> ingredients;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        ingredients = given()
                .get("/api/ingredients")
                .jsonPath().getList("data._id");
    }

    @DisplayName("Создание заказа c ингредиентами с авторизацией")
    @Test
    public void createOrderWithAuthTest() {
        Faker faker = new Faker();
        accessToken = createUser(
                new CreateUserRequest(
                        faker.internet().safeEmailAddress(),
                        faker.internet().password(),
                        faker.name().firstName())
        ).body().jsonPath().getString("accessToken");

        createOrder(ingredients)
                .then()
                .statusCode(SC_CREATED)
                .body("success", equalTo(true));

    }

    @DisplayName("Создание заказа с ингредиентами без авторизации")
    @Test
    public void createOrderWithoutAuthTest() {
        createOrder(ingredients)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @DisplayName("Создание заказа без ингредиентов")
    @Test
    public void createOrderWithoutIngredientsTest() {
        createOrder(List.of())
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @DisplayName("Создание заказа с неправильным хэшем ингредиентов")
    @Test
    public void createOrderWithWrongHashTest() {
        createOrder(List.of("wrongHash"))
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }
}
