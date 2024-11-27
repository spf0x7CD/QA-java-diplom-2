package site.nomoreparties.stellarburgers.models;

import lombok.*;


@ToString
@Value
public class CreateUserRequest {
    String email;
    String password;
    String name;
}
