package br.com.zup.bootcamp.ecommerce.product;

import br.com.zup.bootcamp.ecommerce.sec.AuthenticatedUser;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/product")
class ProductImageResource {

    final Products products;
    final ImageStorage imageStorage;

    ProductImageResource(Products products, ImageStorage imageStorage) {
        this.products = products;
        this.imageStorage = imageStorage;
    }

    @PostMapping("/{id}/images")
    ResponseEntity<Void> uploadImages(@PathVariable long id,
                                      @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                      @RequestParam("images") List<MultipartFile> files) {
        return products.findById(id)
                .filter(product -> product.belongsTo(authenticatedUser.get()))
                .map(product -> product.upload(files.stream().map(UploadedFile::new).flatMap(f -> f.toJpg().stream()).collect(toList()), imageStorage))
                .map(product -> ResponseEntity.ok().<Void> build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
