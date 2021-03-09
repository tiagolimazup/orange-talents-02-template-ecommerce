package br.com.zup.bootcamp.ecommerce.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MustExistsValidator.class)
public @interface MustExists {

    String message() default "{must.exists}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<?> entity();

    String field();
}
