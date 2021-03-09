package br.com.zup.bootcamp.ecommerce.validation;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class UniqueValidator implements ConstraintValidator<Unique, Object> {

    final EntityManager entityManager;

    private Class<?> entity;
    private String field;

    UniqueValidator(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void initialize(Unique unique) {
        this.entity = unique.entity();
        this.field = unique.field();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        long count = new CountQuery(entityManager)
                .select(entity)
                .where(field, value)
                .get();

        return count == 0;
    }
}
