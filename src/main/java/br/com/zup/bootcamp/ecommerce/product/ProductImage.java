package br.com.zup.bootcamp.ecommerce.product;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URL;

@Entity
class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private URL path;

    @ManyToOne
    @NotNull
    private Product product;

    @Deprecated
    ProductImage() { }

    ProductImage(URL path, Product product) {
        this.path = path;
        this.product = product;
    }

    URL getPath() {
        return path;
    }

    Product getProduct() {
        return product;
    }
}
