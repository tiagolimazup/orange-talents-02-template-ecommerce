package br.com.zup.bootcamp.ecommerce.sec;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestController
@RequestMapping("/auth")
class AuthenticationResource {

    final AuthenticationManager authenticationManager;
    final JwtTokens jwtTokens;

    AuthenticationResource(AuthenticationManager authenticationManager, JwtTokens jwtTokens) {
        this.authenticationManager = authenticationManager;
        this.jwtTokens = jwtTokens;
    }

    @PostMapping
    ResponseEntity<String> authenticate(@RequestBody Credentials credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(credentials.toAuthenticationToken());
            return ResponseEntity.ok().body(jwtTokens.generate(authentication).toString());
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED).build();
        }
    }
}
