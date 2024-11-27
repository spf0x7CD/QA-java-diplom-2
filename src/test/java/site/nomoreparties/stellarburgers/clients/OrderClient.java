package site.nomoreparties.stellarburgers.clients;

import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.utils.Utils.buildKV;

public class OrderClient {
    public static Response createOrder(List<String> ingredients) {
        String body = String.format("{%s}", buildKV("ingredients", ingredients));
        return given()
                .header("Content-Type", "application/json")
                .body(body)
                .post("/api/orders");
    }

    public static Response getUserOrders(String bearerToken) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", bearerToken)
                .get("/api/orders");
    }
}
