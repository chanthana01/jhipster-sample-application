package com.chanctn.jhipster_sample_application.service;

import com.chanctn.jhipster_sample_application.domain.Promotion;
import com.chanctn.jhipster_sample_application.repository.PromotionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Promotion}.
 */
@Service
@Transactional
public class PromotionService {

    private final Logger log = LoggerFactory.getLogger(PromotionService.class);

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    /**
     * Save a promotion.
     *
     * @param promotion the entity to save.
     * @return the persisted entity.
     */
    public Mono<Promotion> save(Promotion promotion) {
        log.debug("Request to save Promotion : {}", promotion);
        return promotionRepository.save(promotion);
    }

    /**
     * Partially update a promotion.
     *
     * @param promotion the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Promotion> partialUpdate(Promotion promotion) {
        log.debug("Request to partially update Promotion : {}", promotion);

        return promotionRepository
            .findById(promotion.getId())
            .map(
                existingPromotion -> {
                    if (promotion.getPromotionDescription() != null) {
                        existingPromotion.setPromotionDescription(promotion.getPromotionDescription());
                    }
                    if (promotion.getPromorionFormular() != null) {
                        existingPromotion.setPromorionFormular(promotion.getPromorionFormular());
                    }
                    if (promotion.getExpireAt() != null) {
                        existingPromotion.setExpireAt(promotion.getExpireAt());
                    }
                    if (promotion.getCreateAt() != null) {
                        existingPromotion.setCreateAt(promotion.getCreateAt());
                    }
                    if (promotion.getModifyAt() != null) {
                        existingPromotion.setModifyAt(promotion.getModifyAt());
                    }

                    return existingPromotion;
                }
            )
            .flatMap(promotionRepository::save);
    }

    /**
     * Get all the promotions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Promotion> findAll(Pageable pageable) {
        log.debug("Request to get all Promotions");
        return promotionRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of promotions available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return promotionRepository.count();
    }

    /**
     * Get one promotion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Promotion> findOne(Long id) {
        log.debug("Request to get Promotion : {}", id);
        return promotionRepository.findById(id);
    }

    /**
     * Delete the promotion by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Promotion : {}", id);
        return promotionRepository.deleteById(id);
    }
}
