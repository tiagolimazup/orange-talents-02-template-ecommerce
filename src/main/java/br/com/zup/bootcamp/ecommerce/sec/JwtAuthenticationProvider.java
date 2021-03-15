package br.com.zup.bootcamp.ecommerce.sec;

import br.com.zup.bootcamp.ecommerce.user.Users;
import io.jsonwebtoken.Claims;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
class JwtAuthenticationProvider implements AuthenticationProvider {

    final Users users;
    final JwtTokens jwtTokens;

    JwtAuthenticationProvider(Users users, JwtTokens jwtTokens) {
        this.users = users;
        this.jwtTokens = jwtTokens;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

        return jwtTokens.decode(jwtAuthenticationToken)
                .map(Claims::getSubject)
                .flatMap(users::findByUsername)
                .map(AuthenticatedUser::new)
                .map(user -> new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()))
                .orElseThrow(() -> new BadCredentialsException("Invalid jwt token."));
    }

    @Override
    public boolean supports(Class<?> candidate) {
        return candidate.equals(JwtAuthenticationToken.class);
    }
}
