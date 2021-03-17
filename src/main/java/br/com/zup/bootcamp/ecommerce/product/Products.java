package br.com.zup.bootcamp.ecommerce.product;

import org.springframework.data.jpa.repository.JpaRepository;

interface Products extends JpaRepository<Product, Long> {
}
