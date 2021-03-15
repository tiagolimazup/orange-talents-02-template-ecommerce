package br.com.zup.bootcamp.ecommerce.sec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

class Credentials {

    @JsonProperty
    final String username;

    @JsonProperty
    final String password;

    Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    UsernamePasswordAuthenticationToken toAuthenticationToken() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }
}
