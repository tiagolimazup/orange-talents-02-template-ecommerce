package br.com.zup.bootcamp.ecommerce.validation;

import javax.persistence.EntityManager;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

class CountQuery {

    private final EntityManager entityManager;

    CountQuery(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    <T> SelectCount<T> select(Class<T> entity) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<T> r = q.from(entity);

        return new SelectCount(r, cb, q.select(cb.count(r)));
    }

    class SelectCount<T> {

        private final Root<T> r;
        private final CriteriaBuilder cb;
        private final CriteriaQuery<Long> q;

        private SelectCount(Root<T> r, CriteriaBuilder cb, CriteriaQuery<Long> q) {
            this.r = r;
            this.cb = cb;
            this.q = q;
        }

        SelectCountQuery where(String field, Object value) {
            return new SelectCountQuery(q.where(cb.equal(r.get(field), value)));
        }
    }

    class SelectCountQuery {

        private final CriteriaQuery<Long> selectCount;

        private SelectCountQuery(CriteriaQuery<Long> selectCount) {
            this.selectCount = selectCount;
        }

        public long get() {
            return entityManager.createQuery(selectCount).getSingleResult();
        }
    }
}
