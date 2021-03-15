package br.com.zup.bootcamp.ecommerce.sec;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super("/**", authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return Optional.ofNullable(authentication)
                .or(() -> Optional.ofNullable(request.getHeader("Authorization"))
                    .filter(authorization -> authorization.startsWith("Bearer "))
                    .map(authorization -> authorization.substring(7))
                    .map(token -> getAuthenticationManager().authenticate(new JwtAuthenticationToken(token))))
                .orElseThrow(() -> new InsufficientAuthenticationException("JWT token is missing."));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}
