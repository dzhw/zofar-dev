package de.his.zofar.persistence.common.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import de.his.zofar.persistence.common.entities.BaseEntity;

public interface GenericCRUDDao<T extends BaseEntity> extends
        QueryDslJpaRepository<T, Long>, JpaRepository<T, Long>,
        QueryDslPredicateExecutor<T> {}
