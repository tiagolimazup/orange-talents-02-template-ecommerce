package br.com.zup.bootcamp.ecommerce.user;

import br.com.zup.bootcamp.ecommerce.validation.Unique;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonProperty;

class CreateNewUserRequest {

    @JsonProperty
    @NotBlank
    @Email
    @Unique(entity = User.class, field = "username")
    final String username;

    @JsonProperty
    @NotBlank
    @Size(max = 6)
    final String password;

    CreateNewUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    User toUser(PasswordEncoder encoder) {
        return new User(username, Password.valueOf(password, encoder));
    }
}
