package com.chanctn.jhipster_sample_application.service;

import com.chanctn.jhipster_sample_application.domain.OrderInfo;
import com.chanctn.jhipster_sample_application.repository.OrderInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link OrderInfo}.
 */
@Service
@Transactional
public class OrderInfoService {

    private final Logger log = LoggerFactory.getLogger(OrderInfoService.class);

    private final OrderInfoRepository orderInfoRepository;

    public OrderInfoService(OrderInfoRepository orderInfoRepository) {
        this.orderInfoRepository = orderInfoRepository;
    }

    /**
     * Save a orderInfo.
     *
     * @param orderInfo the entity to save.
     * @return the persisted entity.
     */
    public Mono<OrderInfo> save(OrderInfo orderInfo) {
        log.debug("Request to save OrderInfo : {}", orderInfo);
        return orderInfoRepository.save(orderInfo);
    }

    /**
     * Partially update a orderInfo.
     *
     * @param orderInfo the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<OrderInfo> partialUpdate(OrderInfo orderInfo) {
        log.debug("Request to partially update OrderInfo : {}", orderInfo);

        return orderInfoRepository
            .findById(orderInfo.getId())
            .map(
                existingOrderInfo -> {
                    if (orderInfo.getQuantity() != null) {
                        existingOrderInfo.setQuantity(orderInfo.getQuantity());
                    }
                    if (orderInfo.getPricePerUnit() != null) {
                        existingOrderInfo.setPricePerUnit(orderInfo.getPricePerUnit());
                    }
                    if (orderInfo.getCreateAt() != null) {
                        existingOrderInfo.setCreateAt(orderInfo.getCreateAt());
                    }
                    if (orderInfo.getModifyAt() != null) {
                        existingOrderInfo.setModifyAt(orderInfo.getModifyAt());
                    }

                    return existingOrderInfo;
                }
            )
            .flatMap(orderInfoRepository::save);
    }

    /**
     * Get all the orderInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<OrderInfo> findAll(Pageable pageable) {
        log.debug("Request to get all OrderInfos");
        return orderInfoRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of orderInfos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return orderInfoRepository.count();
    }

    /**
     * Get one orderInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<OrderInfo> findOne(Long id) {
        log.debug("Request to get OrderInfo : {}", id);
        return orderInfoRepository.findById(id);
    }

    /**
     * Delete the orderInfo by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete OrderInfo : {}", id);
        return orderInfoRepository.deleteById(id);
    }
}
