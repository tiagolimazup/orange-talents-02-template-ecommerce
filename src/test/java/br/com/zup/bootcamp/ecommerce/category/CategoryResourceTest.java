package br.com.zup.bootcamp.ecommerce.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
public class CategoryResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    Categories categories;

    @Autowired
    PasswordEncoder encoder;

    @Test
    void createNewMotherCategory() throws Exception {
        mockMvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewCategoryRequest("Tecnologia"))))
                .andExpect(status().isOk());

        assertTrue(categories.existsByName("Tecnologia"));
    }

    @Test
    void createNewChildCategory() throws Exception {
        Category parent = categories.save(new Category("Tecnologia"));

        mockMvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewCategoryRequest("Celulares", parent.getId()))))
                .andExpect(status().isOk());

        categories.findByName("Celulares")
                .ifPresentOrElse(child -> assertTrue(child.isParentOf(parent)), Assertions::fail);
    }

    @Nested
    class Restrictions {

        @Test
        void nameCanNotBeBlank() throws Exception {
            mockMvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewCategoryRequest(null))))
                    .andExpect(status().isBadRequest());

            assertTrue(categories.count() == 0);
        }

        @Test
        void nameCanNotExist() throws Exception {
            Category category = categories.save(new Category("Tecnologia"));

            mockMvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewCategoryRequest(category.getName()))))
                    .andExpect(status().isBadRequest());

            assertTrue(categories.count() == 1);
        }

        @Test
        void motherCategoryMustExists() throws Exception {
            mockMvc.perform(post("/category")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewCategoryRequest("Celulares", 0l))))
                    .andExpect(status().isBadRequest());

            assertTrue(categories.count() == 0);
        }
    }

    private String json(CreateNewCategoryRequest createNewCategoryRequest) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(createNewCategoryRequest);
    }
}
