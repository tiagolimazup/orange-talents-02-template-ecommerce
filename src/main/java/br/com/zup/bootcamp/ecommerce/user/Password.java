package br.com.zup.bootcamp.ecommerce.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import org.springframework.security.crypto.password.PasswordEncoder;

@Embeddable
public class Password {

    @NotBlank
    @Column(name = "password")
    String value;

    @Deprecated
    Password() { }

    private Password(String value) {
        this.value = value;
    }

    String get() {
        return value;
    }

    public static Password valueOf(String raw, PasswordEncoder encoder) {
        return new Password(encoder.encode(raw));
    }
}
