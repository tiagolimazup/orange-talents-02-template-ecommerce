package br.com.zup.bootcamp.ecommerce.sec;

import br.com.zup.bootcamp.ecommerce.user.Password;
import br.com.zup.bootcamp.ecommerce.user.User;
import br.com.zup.bootcamp.ecommerce.user.Users;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationProviderTest {

    @Spy
    JwtTokens jwtTokens = new JwtTokens("my-secret", Duration.ofDays(1).toMillis());

    @Mock
    Users users;

    @InjectMocks
    JwtAuthenticationProvider jwtAuthenticationProvider;

    JwtAuthenticationToken jwtAuthenticationToken;

    User user = new User("tiago.lima@zup.com.br", Password.valueOf("abc1234", NoOpPasswordEncoder.getInstance()));;

    @BeforeEach
    void setup() {
        JwtToken jwtToken = jwtTokens.generate(user.getUsername());

        jwtAuthenticationToken = new JwtAuthenticationToken(jwtToken.toString());
    }

    @Test
    @DisplayName("should authenticate an user using a JWT token")
    void authenticate() {
        when(users.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Authentication authenticated = jwtAuthenticationProvider.authenticate(jwtAuthenticationToken);

        assertAll(() -> assertTrue(authenticated instanceof UsernamePasswordAuthenticationToken),
                  () -> assertTrue(authenticated.getPrincipal() instanceof AuthenticatedUser),
                  () -> assertEquals("tiago.lima@zup.com.br", ((AuthenticatedUser) authenticated.getPrincipal()).getUsername()));

        verify(jwtTokens).decode(jwtAuthenticationToken);
    }

    @Test
    @DisplayName("should reject authentication when username is not found")
    void rejectWhenUsernameIsNotFound() {
        when(users.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> jwtAuthenticationProvider.authenticate(jwtAuthenticationToken));
    }

    @Test
    @DisplayName("should reject authentication when JWT token is invalid")
    void rejectWhenJwtTokenIsInvalid() {
        assertThrows(BadCredentialsException.class, () -> jwtAuthenticationProvider.authenticate(new JwtAuthenticationToken("abc1234")));
    }

}