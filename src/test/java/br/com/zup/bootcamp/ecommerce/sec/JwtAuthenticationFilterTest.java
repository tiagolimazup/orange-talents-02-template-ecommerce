package br.com.zup.bootcamp.ecommerce.sec;

import javax.servlet.ServletException;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock(lenient = true)
    AuthenticationManager authenticationManager;

    @InjectMocks
    JwtAuthenticationFilter jwtAuthenticationFilter;

    MockHttpServletRequest request = new MockHttpServletRequest();

    MockHttpServletResponse response = new MockHttpServletResponse();

    @BeforeEach
    void setup() {
        when(authenticationManager.authenticate(any())).thenAnswer(returnsFirstArg());

        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("should authenticate an user using a Bearer token, present in the Authorization header")
    void authenticate() throws IOException, ServletException {
        JwtToken jwtToken = new JwtToken("abc1234");

        request.addHeader("Authorization", jwtToken.bearer());

        Authentication authentication = jwtAuthenticationFilter.attemptAuthentication(request, response);

        assertAll(() -> assertTrue(authentication instanceof JwtAuthenticationToken),
                  () -> assertEquals(jwtToken.toString(), authentication.getPrincipal()));

        verify(authenticationManager).authenticate(authentication);
    }

    @Test
    @DisplayName("should reject an authenticatication attempt when Authorization header is missing")
    void rejectWhenAuthorizationIsMissing() throws IOException, ServletException {
        assertThrows(InsufficientAuthenticationException.class, () -> jwtAuthenticationFilter.attemptAuthentication(request, response));

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    @DisplayName("should reject an authenticatication attempt when Authorization header is not a Bearer token")
    void rejectWhenAuthorizationIsNotABearerToken() throws IOException, ServletException {
        request.addHeader("Authorization", "whatever");

        assertThrows(InsufficientAuthenticationException.class, () -> jwtAuthenticationFilter.attemptAuthentication(request, response));

        verify(authenticationManager, never()).authenticate(any());
    }

    @Test
    @DisplayName("should return the same Authentication object, if there is one already authenticated")
    void returnSameAuthenticationWhenIsPresent() throws IOException, ServletException {
        JwtAuthenticationToken authentication = new JwtAuthenticationToken("abc1234");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertSame(authentication, jwtAuthenticationFilter.attemptAuthentication(request, response));

        verify(authenticationManager, never()).authenticate(any());
    }
}
