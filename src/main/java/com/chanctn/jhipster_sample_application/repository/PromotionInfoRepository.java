package com.chanctn.jhipster_sample_application.repository;

import com.chanctn.jhipster_sample_application.domain.PromotionInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the PromotionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PromotionInfoRepository extends R2dbcRepository<PromotionInfo, Long>, PromotionInfoRepositoryInternal {
    Flux<PromotionInfo> findAllBy(Pageable pageable);

    @Query("SELECT * FROM promotion_info entity WHERE entity.promotion_id = :id")
    Flux<PromotionInfo> findByPromotion(Long id);

    @Query("SELECT * FROM promotion_info entity WHERE entity.promotion_id IS NULL")
    Flux<PromotionInfo> findAllWherePromotionIsNull();

    @Query("SELECT * FROM promotion_info entity WHERE entity.product_id = :id")
    Flux<PromotionInfo> findByProduct(Long id);

    @Query("SELECT * FROM promotion_info entity WHERE entity.product_id IS NULL")
    Flux<PromotionInfo> findAllWhereProductIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<PromotionInfo> findAll();

    @Override
    Mono<PromotionInfo> findById(Long id);

    @Override
    <S extends PromotionInfo> Mono<S> save(S entity);
}

interface PromotionInfoRepositoryInternal {
    <S extends PromotionInfo> Mono<S> insert(S entity);
    <S extends PromotionInfo> Mono<S> save(S entity);
    Mono<Integer> update(PromotionInfo entity);

    Flux<PromotionInfo> findAll();
    Mono<PromotionInfo> findById(Long id);
    Flux<PromotionInfo> findAllBy(Pageable pageable);
    Flux<PromotionInfo> findAllBy(Pageable pageable, Criteria criteria);
}
