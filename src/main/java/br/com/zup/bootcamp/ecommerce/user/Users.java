package br.com.zup.bootcamp.ecommerce.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface Users extends JpaRepository<User, Long> {

    boolean existsByLogin(String login);
}
