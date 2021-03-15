package br.com.zup.bootcamp.ecommerce.sec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

import org.springframework.security.core.Authentication;

public class JwtToken {

    private final String token;

    JwtToken(String token) {
        this.token = token;
    }

    Claims decode(String secret) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    @Override
    public String toString() {
        return token;
    }

    public String bearer() {
        return "Bearer " + token;
    }
}
