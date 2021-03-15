package br.com.zup.bootcamp.ecommerce.sec;

import br.com.zup.bootcamp.ecommerce.SecuredWebTestConfiguration;
import br.com.zup.bootcamp.ecommerce.user.Password;
import br.com.zup.bootcamp.ecommerce.user.User;
import br.com.zup.bootcamp.ecommerce.user.Users;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecuredWebTestConfiguration.class)
@AutoConfigureMockMvc
class AuthenticationResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @MockBean
    Users users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        when(users.findByUsername("tiago.lima@zup.com.br"))
                .thenReturn(Optional.of(new User("tiago.lima@zup.com.br", Password.valueOf("123456", passwordEncoder))));
    }

    @Test
    void authenticate() throws Exception {
        mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(new Credentials("tiago.lima@zup.com.br", "123456"))))
                .andExpect(status().isOk())
                .andExpect(content().string(not(blankOrNullString())));
    }

    private String json(Credentials credentials) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(credentials);
    }
}
