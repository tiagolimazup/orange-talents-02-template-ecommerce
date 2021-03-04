package br.com.zup.bootcamp.ecommerce.user;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotBlank
    @Email
    String login;

    @Embedded
    @NotNull
    @Valid
    Password password;

    @NotNull
    @PastOrPresent
    LocalDateTime createdAt;

    @Deprecated
    User() { }

    User(String login, Password password) {
        this.login = login;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
}
