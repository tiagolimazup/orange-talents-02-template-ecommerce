package br.com.zup.bootcamp.ecommerce.sec;

import br.com.zup.bootcamp.ecommerce.user.User;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class AuthenticatedUser extends org.springframework.security.core.userdetails.User {

    private final User user;

    public AuthenticatedUser(User user) {
        super(user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority("user")));
        this.user = user;
    }

    public User get() {
        return user;
    }
}
