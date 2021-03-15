package br.com.zup.bootcamp.ecommerce.sec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokens {

    final String secret;
    final long expirationInMillis;

    JwtTokens(@Value("${ecommerce.jwt.secret}") String secret,
              @Value("${ecommerce.jwt.expiration}") long expirationInMillis) {
        this.secret = secret;
        this.expirationInMillis = expirationInMillis;
    }

    public JwtToken generate(String username) {
        Instant now = Instant.now();

        return new JwtToken(Jwts.builder()
                .setIssuer("bootcamp-zup")
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationInMillis)))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact());
    }

    JwtToken generate(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

        return generate(user.getUsername());
    }


    Optional<Claims> decode(JwtAuthenticationToken jwtAuthenticationToken) {
        return jwtAuthenticationToken.decode(secret);
    }
}
