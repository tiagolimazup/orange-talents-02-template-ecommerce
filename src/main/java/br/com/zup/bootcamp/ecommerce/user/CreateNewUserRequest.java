package br.com.zup.bootcamp.ecommerce.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;

class CreateNewUserRequest {

    @JsonProperty
    @NotBlank
    @Email
    final String login;

    @JsonProperty
    @NotBlank
    @Size(max = 6)
    final String password;

    CreateNewUserRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    User toUser(PasswordEncoder encoder) {
        return new User(login, Password.valueOf(password, encoder));
    }
}
