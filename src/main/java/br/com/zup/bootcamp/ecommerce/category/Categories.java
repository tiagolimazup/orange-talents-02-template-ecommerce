package br.com.zup.bootcamp.ecommerce.category;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Categories extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    Optional<Category> findByName(String celulares);
}
