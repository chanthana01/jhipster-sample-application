package com.chanctn.jhipster_sample_application.repository;

import com.chanctn.jhipster_sample_application.domain.CustomerDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the CustomerDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerDetailsRepository extends R2dbcRepository<CustomerDetails, Long>, CustomerDetailsRepositoryInternal {
    @Query("SELECT * FROM customer_details entity WHERE entity.user_id = :id")
    Flux<CustomerDetails> findByUser(Long id);

    @Query("SELECT * FROM customer_details entity WHERE entity.user_id IS NULL")
    Flux<CustomerDetails> findAllWhereUserIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<CustomerDetails> findAll();

    @Override
    Mono<CustomerDetails> findById(Long id);

    @Override
    <S extends CustomerDetails> Mono<S> save(S entity);
}

interface CustomerDetailsRepositoryInternal {
    <S extends CustomerDetails> Mono<S> insert(S entity);
    <S extends CustomerDetails> Mono<S> save(S entity);
    Mono<Integer> update(CustomerDetails entity);

    Flux<CustomerDetails> findAll();
    Mono<CustomerDetails> findById(Long id);
    Flux<CustomerDetails> findAllBy(Pageable pageable);
    Flux<CustomerDetails> findAllBy(Pageable pageable, Criteria criteria);
}
