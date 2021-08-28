package com.chanctn.jhipster_sample_application.repository;

import com.chanctn.jhipster_sample_application.domain.OrderInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the OrderInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderInfoRepository extends R2dbcRepository<OrderInfo, Long>, OrderInfoRepositoryInternal {
    Flux<OrderInfo> findAllBy(Pageable pageable);

    @Query("SELECT * FROM order_info entity WHERE entity.promotion_info_id = :id")
    Flux<OrderInfo> findByPromotionInfo(Long id);

    @Query("SELECT * FROM order_info entity WHERE entity.promotion_info_id IS NULL")
    Flux<OrderInfo> findAllWherePromotionInfoIsNull();

    @Query("SELECT * FROM order_info entity WHERE entity.product_id = :id")
    Flux<OrderInfo> findByProduct(Long id);

    @Query("SELECT * FROM order_info entity WHERE entity.product_id IS NULL")
    Flux<OrderInfo> findAllWhereProductIsNull();

    @Query("SELECT * FROM order_info entity WHERE entity.order_id = :id")
    Flux<OrderInfo> findByOrder(Long id);

    @Query("SELECT * FROM order_info entity WHERE entity.order_id IS NULL")
    Flux<OrderInfo> findAllWhereOrderIsNull();

    // just to avoid having unambigous methods
    @Override
    Flux<OrderInfo> findAll();

    @Override
    Mono<OrderInfo> findById(Long id);

    @Override
    <S extends OrderInfo> Mono<S> save(S entity);
}

interface OrderInfoRepositoryInternal {
    <S extends OrderInfo> Mono<S> insert(S entity);
    <S extends OrderInfo> Mono<S> save(S entity);
    Mono<Integer> update(OrderInfo entity);

    Flux<OrderInfo> findAll();
    Mono<OrderInfo> findById(Long id);
    Flux<OrderInfo> findAllBy(Pageable pageable);
    Flux<OrderInfo> findAllBy(Pageable pageable, Criteria criteria);
}
