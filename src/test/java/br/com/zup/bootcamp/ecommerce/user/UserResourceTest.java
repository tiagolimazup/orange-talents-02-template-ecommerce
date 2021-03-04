package br.com.zup.bootcamp.ecommerce.user;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
public class UserResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    Users users;

    @Test
    void createNewUser() throws Exception {
        String json = json(new CreateNewUserRequest("tiago.lima@gmail.com", "abc123"));

        mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isOk());

        assertTrue(users.existsByLogin("tiago.lima@gmail.com"));
    }

    @Nested
    class Restrictions {

        @Test
        void loginCanNotBeBlank() throws Exception {
            String json = json(new CreateNewUserRequest(null, "abc123"));

            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest());

            assertTrue(users.count() == 0);
        }

        @Test
        void loginMustBeAValidEmail() throws Exception {
            String json = json(new CreateNewUserRequest("tiago.lima", "abc123"));

            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest());

            assertTrue(users.count() == 0);
        }

        @Test
        void passwordCanNotBeBlank() throws Exception {
            String json = json(new CreateNewUserRequest("tiago.lima@zup.com.br", null));

            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest());

            assertTrue(users.count() == 0);
        }

        @Test
        void passwordCanNotBeGreatherThan6Characters() throws Exception {
            String json = json(new CreateNewUserRequest("tiago.lima@zup.com.br", "abc1234"));

            mockMvc.perform(post("/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isBadRequest());

            assertTrue(users.count() == 0);
        }

    }

    private String json(CreateNewUserRequest createNewUserRequest) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(createNewUserRequest);
    }
}
