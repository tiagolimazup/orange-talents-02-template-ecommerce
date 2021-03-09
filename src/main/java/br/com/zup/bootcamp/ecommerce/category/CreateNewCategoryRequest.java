package br.com.zup.bootcamp.ecommerce.category;

import br.com.zup.bootcamp.ecommerce.validation.MustExists;
import br.com.zup.bootcamp.ecommerce.validation.Unique;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

class CreateNewCategoryRequest {

    @JsonProperty
    @NotBlank
    @Unique(entity = Category.class, field = "name")
    final String name;

    @JsonProperty
    @MustExists(entity = Category.class, field = "id")
    final Long categoryParentId;

    CreateNewCategoryRequest(String name) {
        this(name, null);
    }

    CreateNewCategoryRequest(String name, Long categoryParentId) {
        this.name = name;
        this.categoryParentId = categoryParentId;
    }

    Category toCategory(Categories categories) {
        Category parent = Optional.ofNullable(categoryParentId)
                .flatMap(parentId -> categories.findById(parentId))
                .orElse(null);
        return new Category(name, parent);
    }
}
