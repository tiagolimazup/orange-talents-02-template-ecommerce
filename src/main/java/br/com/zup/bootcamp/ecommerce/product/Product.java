package br.com.zup.bootcamp.ecommerce.product;

import br.com.zup.bootcamp.ecommerce.category.Category;
import br.com.zup.bootcamp.ecommerce.user.User;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    User user;

    String name;

    BigDecimal price;

    Integer quantity;

    @ElementCollection
    @CollectionTable(name = "product_characteristics")
    Set<Characteristic> productCharacteristics = new HashSet<>();

    String description;

    @ManyToOne
    Category category;

    LocalDateTime createdAt;

    @Deprecated
    Product() { }

    Product(User user, String name, BigDecimal price, Integer quantity, Set<Characteristic> productCharacteristics, String description, Category category) {
        this.user = user;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productCharacteristics = productCharacteristics;
        this.description = description;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }
}
