package site.nomoreparties.stellarburgers;

import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.CreateUserRequest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static site.nomoreparties.stellarburgers.clients.OrderClient.createOrder;
import static site.nomoreparties.stellarburgers.clients.OrderClient.getUserOrders;
import static site.nomoreparties.stellarburgers.clients.UserClient.createUser;
import static site.nomoreparties.stellarburgers.clients.UserClient.deleteUser;

@DisplayName("GET /api/orders | Получение заказов пользователя")
public class GetUserOrdersTest {
    private String accessToken;
    private List<String> ingredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        Faker faker = new Faker();
        accessToken = createUser(new CreateUserRequest(
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                faker.name().firstName())).
                jsonPath().getString("accessToken");
        ingredients = given()
                .get("/api/ingredients")
                .jsonPath().getList("data._id");
    }

    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Test
    public void getAuthorizedUserOrdersTest() {
        Response response = getUserOrders(accessToken);
        createOrder(ingredients);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("total", greaterThan(0));
    }

    @DisplayName("Попытка получения списка заказов пользователя без авторизации")
    @Test
    public void getUnauthorizedUserOrdersTest() {
        getUserOrders("")
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        deleteUser(accessToken);
    }
}
