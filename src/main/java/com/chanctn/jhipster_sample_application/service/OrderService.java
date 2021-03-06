package com.chanctn.jhipster_sample_application.service;

import com.chanctn.jhipster_sample_application.domain.Order;
import com.chanctn.jhipster_sample_application.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * Save a order.
     *
     * @param order the entity to save.
     * @return the persisted entity.
     */
    public Mono<Order> save(Order order) {
        log.debug("Request to save Order : {}", order);
        return orderRepository.save(order);
    }

    /**
     * Partially update a order.
     *
     * @param order the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Order> partialUpdate(Order order) {
        log.debug("Request to partially update Order : {}", order);

        return orderRepository
            .findById(order.getId())
            .map(
                existingOrder -> {
                    if (order.getCustomerName() != null) {
                        existingOrder.setCustomerName(order.getCustomerName());
                    }
                    if (order.getOrderAddress() != null) {
                        existingOrder.setOrderAddress(order.getOrderAddress());
                    }
                    if (order.getTotalAmount() != null) {
                        existingOrder.setTotalAmount(order.getTotalAmount());
                    }
                    if (order.getOmiseTxnId() != null) {
                        existingOrder.setOmiseTxnId(order.getOmiseTxnId());
                    }
                    if (order.getTxnTimeStamp() != null) {
                        existingOrder.setTxnTimeStamp(order.getTxnTimeStamp());
                    }
                    if (order.getIsTxnSuccess() != null) {
                        existingOrder.setIsTxnSuccess(order.getIsTxnSuccess());
                    }
                    if (order.getCreateAt() != null) {
                        existingOrder.setCreateAt(order.getCreateAt());
                    }
                    if (order.getModifyAt() != null) {
                        existingOrder.setModifyAt(order.getModifyAt());
                    }

                    return existingOrder;
                }
            )
            .flatMap(orderRepository::save);
    }

    /**
     * Get all the orders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Order> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        return orderRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of orders available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return orderRepository.count();
    }

    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Order> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        return orderRepository.deleteById(id);
    }
}
