package br.com.zup.bootcamp.ecommerce.product;

import br.com.zup.bootcamp.ecommerce.category.Categories;
import br.com.zup.bootcamp.ecommerce.sec.AuthenticatedUser;

import javax.validation.Valid;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
class ProductResource {

    final Products products;
    final Categories categories;

    ProductResource(Products products, Categories categories) {
        this.products = products;
        this.categories = categories;
    }

    @PostMapping
    ResponseEntity<Void> createNewProduct(@Valid @RequestBody CreateNewProductRequest request,
                                          @AuthenticationPrincipal AuthenticatedUser user) {

        return request.toProduct(user.get(), categories)
                .map(products::save)
                .map(product -> ResponseEntity.ok().<Void> build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
