package br.com.zup.bootcamp.ecommerce.user;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
class UserResource {

    final Users users;
    final PasswordEncoder passwordEncoder;

    UserResource(Users users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    ResponseEntity<Void> createNewUser(@Valid @RequestBody CreateNewUserRequest createNewUserRequest) {
        users.save(createNewUserRequest.toUser(passwordEncoder));
        return ResponseEntity.ok().build();
    }
}
