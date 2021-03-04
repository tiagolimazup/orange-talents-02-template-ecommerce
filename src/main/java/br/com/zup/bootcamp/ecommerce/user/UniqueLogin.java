package br.com.zup.bootcamp.ecommerce.user;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueLoginValidator.class)
@interface UniqueLogin {

    String message() default "{login.already.exists}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
