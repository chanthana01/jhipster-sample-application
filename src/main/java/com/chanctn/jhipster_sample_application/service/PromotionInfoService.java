package com.chanctn.jhipster_sample_application.service;

import com.chanctn.jhipster_sample_application.domain.PromotionInfo;
import com.chanctn.jhipster_sample_application.repository.PromotionInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link PromotionInfo}.
 */
@Service
@Transactional
public class PromotionInfoService {

    private final Logger log = LoggerFactory.getLogger(PromotionInfoService.class);

    private final PromotionInfoRepository promotionInfoRepository;

    public PromotionInfoService(PromotionInfoRepository promotionInfoRepository) {
        this.promotionInfoRepository = promotionInfoRepository;
    }

    /**
     * Save a promotionInfo.
     *
     * @param promotionInfo the entity to save.
     * @return the persisted entity.
     */
    public Mono<PromotionInfo> save(PromotionInfo promotionInfo) {
        log.debug("Request to save PromotionInfo : {}", promotionInfo);
        return promotionInfoRepository.save(promotionInfo);
    }

    /**
     * Partially update a promotionInfo.
     *
     * @param promotionInfo the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<PromotionInfo> partialUpdate(PromotionInfo promotionInfo) {
        log.debug("Request to partially update PromotionInfo : {}", promotionInfo);

        return promotionInfoRepository
            .findById(promotionInfo.getId())
            .map(
                existingPromotionInfo -> {
                    return existingPromotionInfo;
                }
            )
            .flatMap(promotionInfoRepository::save);
    }

    /**
     * Get all the promotionInfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<PromotionInfo> findAll(Pageable pageable) {
        log.debug("Request to get all PromotionInfos");
        return promotionInfoRepository.findAllBy(pageable);
    }

    /**
     * Returns the number of promotionInfos available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return promotionInfoRepository.count();
    }

    /**
     * Get one promotionInfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<PromotionInfo> findOne(Long id) {
        log.debug("Request to get PromotionInfo : {}", id);
        return promotionInfoRepository.findById(id);
    }

    /**
     * Delete the promotionInfo by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete PromotionInfo : {}", id);
        return promotionInfoRepository.deleteById(id);
    }
}
