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
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private User user;

    @NotBlank
    private String name;

    @NotNull
    @DecimalMin("0.1")
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;

    @ElementCollection
    @CollectionTable(name = "product_characteristics")
    @Size(min = 3)
    private Set<Characteristic> characteristics = new HashSet<>();

    @NotBlank
    @Size(max = 1000)
    private String description;

    @ManyToOne
    @NotNull
    private Category category;

    @OneToMany
    private List<ProductImage> images = new ArrayList<>();

    private LocalDateTime createdAt;

    @Deprecated
    Product() { }

    Product(User user, String name, BigDecimal price, Integer quantity, Set<Characteristic> characteristics, String description, Category category) {
        this.user = user;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.characteristics = characteristics;
        this.description = description;
        this.category = category;
        this.createdAt = LocalDateTime.now();
    }

    Long getId() {
        return id;
    }

    boolean belongsTo(User user) {
        return user.getId().equals(user.getId());
    }

    Product upload(Collection<ImageFile> files, ImageStorage imageStorage) {
        Collection<ProductImage> newImages = imageStorage.upload(files).stream()
                .map(url -> new ProductImage(url, this))
                .collect(toList());
        images.addAll(newImages);
        return this;
    }

    List<ProductImage> getImages() {
        return Collections.unmodifiableList(images);
    }
}
