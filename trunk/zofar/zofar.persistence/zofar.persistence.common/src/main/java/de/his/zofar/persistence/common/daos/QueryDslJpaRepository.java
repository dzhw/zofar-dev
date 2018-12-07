package de.his.zofar.persistence.common.daos;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

import de.his.zofar.persistence.common.entities.BaseEntity;

public interface QueryDslJpaRepository<T extends BaseEntity, ID extends Serializable> {
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#count(com
     * .mysema.query.types.Predicate)
     */
    public long count(Predicate predicate);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findAll(com
     * .mysema.query.types.Predicate)
     */
    public List<T> findAll(Predicate predicate);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findAll(com
     * .mysema.query.types.Predicate,
     * com.mysema.query.types.OrderSpecifier<?>[])
     */
    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findAll(com
     * .mysema.query.types.Predicate, org.springframework.data.domain.Pageable)
     */
    public Page<T> findAll(Predicate predicate, Pageable pageable);

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.data.querydsl.QueryDslPredicateExecutor#findOne(com
     * .mysema.query.types.Predicate)
     */
    public T findOne(Predicate predicate);

}
