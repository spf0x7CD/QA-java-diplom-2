package site.nomoreparties.stellarburgers.models;

import lombok.Value;

@Value
public class AuthorizeUserRequest {
    String email;
    String password;
}
