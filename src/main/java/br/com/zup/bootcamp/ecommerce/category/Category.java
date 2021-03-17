package br.com.zup.bootcamp.ecommerce.category;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String name;

    @ManyToOne
    private Category parent;

    @Deprecated
    Category() { }

    public Category(String name) {
        this(name, null);
    }

    Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    public Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    boolean isParentOf(Category candidate) {
        return parent != null && parent.id == candidate.id;
    }

}
