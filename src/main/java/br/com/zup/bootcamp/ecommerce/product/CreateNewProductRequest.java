package br.com.zup.bootcamp.ecommerce.product;

import br.com.zup.bootcamp.ecommerce.category.Categories;
import br.com.zup.bootcamp.ecommerce.category.Category;
import br.com.zup.bootcamp.ecommerce.user.User;
import br.com.zup.bootcamp.ecommerce.validation.MustExists;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

class CreateNewProductRequest {

    @JsonProperty
    @NotNull
    final String name;

    @JsonProperty
    @NotNull
    @DecimalMin("0.1")
    final BigDecimal price;

    @JsonProperty
    @NotNull
    @Min(1)
    final Integer quantity;

    @JsonProperty
    @Size(min = 3)
    final Map<String, ? extends Object> characteristics;

    @JsonProperty
    @NotBlank
    @Size(max = 1000)
    final String description;

    @JsonProperty
    @NotNull
    @MustExists(entity = Category.class, field = "id")
    final Long categoryId;

    CreateNewProductRequest(String name, BigDecimal price, Integer quantity, Map<String, ? extends Object> characteristics,
                            String description, Long categoryId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.characteristics = characteristics;
        this.description = description;
        this.categoryId = categoryId;
    }

    Optional<Product> toProduct(User user, Categories categories) {
        return categories.findById(categoryId).map(category -> toProduct(user, category));
    }

    private Product toProduct(User user, Category category) {
        Set<Characteristic> productCharacteristics = characteristics.entrySet().stream().map(e -> new Characteristic(e.getKey(), e.getValue().toString())).collect(Collectors.toSet());
        return new Product(user, name, price, quantity, productCharacteristics, description, category);
    }
}
