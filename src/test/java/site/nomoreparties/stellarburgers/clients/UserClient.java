package site.nomoreparties.stellarburgers.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.models.AuthorizeUserRequest;
import site.nomoreparties.stellarburgers.models.CreateUserRequest;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step
    public static Response createUser(CreateUserRequest createUserRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(createUserRequest)
                .post("/api/auth/register");
    }

    @Step
    public static Response authorizeUser(AuthorizeUserRequest authorizeUserRequest) {
        return given()
                .header("Content-type", "application/json")
                .body(authorizeUserRequest)
                .post("/api/auth/login");
    }

    @Step
    public static Response deleteUser(String bearerToken) {
        if (bearerToken == null) bearerToken = "";
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", bearerToken)
                .delete("/api/auth/user");
    }

    @Step
    public static Response updateUser(String bearerToken, String... keyValue) {
        StringBuilder body = new StringBuilder("{");
        for (String s : keyValue) {
            body.append(String.format("%s,", s));
        }
        body.replace(body.length() - 1, body.length(), "}");
        System.out.println("new value " + body);
        return given()
                .header("Authorization", bearerToken)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .patch("/api/auth/user");
    }


}