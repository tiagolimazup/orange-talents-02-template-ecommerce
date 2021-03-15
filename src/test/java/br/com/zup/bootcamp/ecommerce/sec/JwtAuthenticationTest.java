package br.com.zup.bootcamp.ecommerce.sec;

import br.com.zup.bootcamp.ecommerce.SecuredWebTestConfiguration;
import br.com.zup.bootcamp.ecommerce.user.Password;
import br.com.zup.bootcamp.ecommerce.user.User;
import br.com.zup.bootcamp.ecommerce.user.Users;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecuredWebTestConfiguration.class)
@AutoConfigureMockMvc
class JwtAuthenticationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @MockBean
    Users users;

    @Autowired
    PasswordEncoder passwordEncoder;

    String accessToken;

    @BeforeEach
    void setup() throws Exception {
        User user = new User("tiago.lima@zup.com.br", Password.valueOf("123456", passwordEncoder));

        when(users.findByUsername("tiago.lima@zup.com.br")).thenReturn(Optional.of(user));

        accessToken = mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(new Credentials(user.getUsername(), "123456"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assumeTrue(accessToken != null);
    }

    @Test
    void authenticate() throws Exception {
        mockMvc.perform(post("/whatever")
                .header(AUTHORIZATION, new JwtToken(accessToken).bearer()))
                .andExpect(status().isNotFound());
    }

    private String json(Credentials credentials) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(credentials);
    }
}