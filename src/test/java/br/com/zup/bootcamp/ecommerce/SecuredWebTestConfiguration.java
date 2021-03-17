package br.com.zup.bootcamp.ecommerce;

import br.com.zup.bootcamp.ecommerce.sec.AuthenticatedUser;
import br.com.zup.bootcamp.ecommerce.user.Password;
import br.com.zup.bootcamp.ecommerce.user.User;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@TestConfiguration
public class SecuredWebTestConfiguration {

    @Autowired
    WebApplicationContext context;

    @Bean
    MockMvc mockMvc() {
        return MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Bean
    UsernamePasswordAuthenticationToken authentication(AuthenticatedUser authenticatedUser) {
        return new UsernamePasswordAuthenticationToken(authenticatedUser, null, List.of(new SimpleGrantedAuthority("user")));
    }

    @Bean
    AuthenticatedUser authenticatedUser(PasswordEncoder passwordEncoder) {
        User user = new User("tiago.lima@zup.com.br", Password.valueOf("123456", passwordEncoder));

        return new AuthenticatedUser(user);
    }
}
