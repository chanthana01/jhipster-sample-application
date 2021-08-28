package com.chanctn.jhipster_sample_application.repository;

import com.chanctn.jhipster_sample_application.domain.Promotion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Promotion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionRepository extends R2dbcRepository<Promotion, Long>, PromotionRepositoryInternal {
    Flux<Promotion> findAllBy(Pageable pageable);

    // just to avoid having unambigous methods
    @Override
    Flux<Promotion> findAll();

    @Override
    Mono<Promotion> findById(Long id);

    @Override
    <S extends Promotion> Mono<S> save(S entity);
}

interface PromotionRepositoryInternal {
    <S extends Promotion> Mono<S> insert(S entity);
    <S extends Promotion> Mono<S> save(S entity);
    Mono<Integer> update(Promotion entity);

    Flux<Promotion> findAll();
    Mono<Promotion> findById(Long id);
    Flux<Promotion> findAllBy(Pageable pageable);
    Flux<Promotion> findAllBy(Pageable pageable, Criteria criteria);
}
