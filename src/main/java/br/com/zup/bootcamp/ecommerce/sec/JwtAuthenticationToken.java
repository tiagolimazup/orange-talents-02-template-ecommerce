package br.com.zup.bootcamp.ecommerce.sec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.authentication.AbstractAuthenticationToken;

class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public JwtAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    public Optional<Claims> decode(String secret) {
        try {
            JwtToken jwtToken = new JwtToken(token);
            return Optional.ofNullable(jwtToken.decode(secret));
        } catch (JwtException e) {
            return Optional.empty();
        }

    }
}
