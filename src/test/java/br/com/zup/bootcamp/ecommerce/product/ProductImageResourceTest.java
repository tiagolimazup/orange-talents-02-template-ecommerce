package br.com.zup.bootcamp.ecommerce.product;

import br.com.zup.bootcamp.ecommerce.SecuredWebTestConfiguration;
import br.com.zup.bootcamp.ecommerce.category.Categories;
import br.com.zup.bootcamp.ecommerce.category.Category;
import br.com.zup.bootcamp.ecommerce.sec.AuthenticatedUser;
import br.com.zup.bootcamp.ecommerce.user.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(SecuredWebTestConfiguration.class)
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
class ProductImageResourceTest {

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
    void createNewImage(@Autowired Categories categories) throws Exception {
        Product product = products.save(new Product(authenticatedUser.get(), "IPhone", BigDecimal.TEN, 10,
                Set.of(new Characteristic("Cor", "Preto"),
                       new Characteristic("Memoria", "128GB"),
                       new Characteristic("Camera Frontal", "12 Mpx")),
                "Novo Iphone",
                categories.save(new Category("Celulares"))));

        MockMultipartFile file = new MockMultipartFile("images", "image-1-iphone.jpg",
                MediaType.IMAGE_JPEG_VALUE, getClass().getResourceAsStream("iphone.jpg"));

        mockMvc.perform(multipart("/product/{id}/images", product.getId())
                            .file(file)
                            .with(authentication(authentication)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isOk());

        Product updated = products.getOne(product.getId());

        assertTrue(updated.getImages().size() == 1);

        ProductImage image = updated.getImages().get(0);

        assertAll(() -> assertSame(product, image.getProduct()),
                  () -> assertNotNull(image.getPath()));
    }

}
