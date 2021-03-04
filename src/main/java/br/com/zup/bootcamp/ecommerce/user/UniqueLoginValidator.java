package br.com.zup.bootcamp.ecommerce.user;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    final Users users;

    public UniqueLoginValidator(Users users) {
        this.users = users;
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        return !users.existsByLogin(login);
    }
}
