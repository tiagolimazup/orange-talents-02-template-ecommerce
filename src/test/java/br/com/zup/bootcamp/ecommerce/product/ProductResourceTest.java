package br.com.zup.bootcamp.ecommerce.product;

import br.com.zup.bootcamp.ecommerce.SecuredWebTestConfiguration;
import br.com.zup.bootcamp.ecommerce.category.Categories;
import br.com.zup.bootcamp.ecommerce.category.Category;
import br.com.zup.bootcamp.ecommerce.sec.AuthenticatedUser;
import br.com.zup.bootcamp.ecommerce.user.Users;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecuredWebTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
class ProductResourceTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    Authentication authentication;

    @Autowired
    AuthenticatedUser authenticatedUser;

    @Autowired
    Products products;

    @BeforeEach
    void setup(@Autowired Users users) {
        users.save(authenticatedUser.get());
    }

    @Test
    void createNewProduct(@Autowired Categories categories) throws Exception {
        Category category = categories.saveAndFlush(new Category("Smartphones"));

        CreateNewProductRequest newProductRequest = new CreateNewProductRequest("IPhone",
                BigDecimal.valueOf(19.90),
                10,
                Map.of("Cor", "Preto",
                        "Memoria", "128GB",
                        "Camera Frontal", "12 Mpx"),
                "Esse é o novo IPhone",
                category.getId());

        mockMvc.perform(post("/product")
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(newProductRequest)))
                .andExpect(status().isOk());

        assertTrue(products.count() != 0);
    }

    @Nested
    class Restrictions {

        CreateNewProductRequest newProductRequest = new CreateNewProductRequest(null, null, null, null, null, null);

        @Test
        void rejectWhenNameIsBlank() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(newProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'name')].message").value("must not be blank"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenPriceIsNull() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(newProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'price')].message").value("must not be null"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenPriceIsIsNotGreatherThanZero() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewProductRequest(null, BigDecimal.ZERO, null, null, null, null))))
                    .andExpect(status().isBadRequest())
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(jsonPath("$.errors[?(@.field == 'price')].message").value("must be greater than or equal to 0.1"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenQuantityIsNull() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(newProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'quantity')].message").value("must not be null"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenQuantityIsNotGreatherThanZero() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewProductRequest(null, null, 0, null, null, null))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'quantity')].message").value("must be greater than or equal to 1"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenDescriptionIsBlank() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(newProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'description')].message").value("must not be blank"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenDescriptionIsGreatherThan1000Characters() throws Exception {
            String description = Stream.generate(() -> "a").limit(1001).collect(Collectors.joining());

            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewProductRequest(null, null, null, null, description, null))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'description')].message").value("size must be between 0 and 1000"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenThereIsNoAtLeastThreeCharacteristics() throws Exception {
            Map<String, String> characteristics = Map.of("Cor", "Preto", "Memoria", "128GB");

            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewProductRequest(null, null, null, characteristics, null, null))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'characteristics')].message").exists());

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenCategoryIsNull() throws Exception {
            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(newProductRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'categoryId')].message").value("must not be null"));

            assertTrue(products.count() == 0);
        }

        @Test
        void rejectWhenCategoryDoesNotExists() throws Exception {
            long categoryId = 1l;

            mockMvc.perform(post("/product")
                    .with(authentication(authentication))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json(new CreateNewProductRequest(null, null, null, null, null, categoryId))))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors[?(@.field == 'categoryId')].message").value("value must exists"));

            assertTrue(products.count() == 0);
        }

    }

    private String json(CreateNewProductRequest createNewProductRequest) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(createNewProductRequest);
    }
}
