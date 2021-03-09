package br.com.zup.bootcamp.ecommerce.validation;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class MustExistsValidator implements ConstraintValidator<MustExists, Object> {

    final EntityManager entityManager;

    private Class<?> entity;
    private String field;

    MustExistsValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(MustExists mustExists) {
        this.entity = mustExists.entity();
        this.field = mustExists.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        long count = new CountQuery(entityManager)
                .select(entity)
                .where(field, value)
                .get();

        return count != 0;
    }
}
