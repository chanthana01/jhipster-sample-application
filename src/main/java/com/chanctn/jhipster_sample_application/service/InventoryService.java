package com.chanctn.jhipster_sample_application.service;

import com.chanctn.jhipster_sample_application.domain.Inventory;
import com.chanctn.jhipster_sample_application.repository.InventoryRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Inventory}.
 */
@Service
@Transactional
public class InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Save a inventory.
     *
     * @param inventory the entity to save.
     * @return the persisted entity.
     */
    public Mono<Inventory> save(Inventory inventory) {
        log.debug("Request to save Inventory : {}", inventory);
        return inventoryRepository.save(inventory);
    }

    /**
     * Partially update a inventory.
     *
     * @param inventory the entity to update partially.
     * @return the persisted entity.
     */
    public Mono<Inventory> partialUpdate(Inventory inventory) {
        log.debug("Request to partially update Inventory : {}", inventory);

        return inventoryRepository
            .findById(inventory.getId())
            .map(
                existingInventory -> {
                    if (inventory.getUnit() != null) {
                        existingInventory.setUnit(inventory.getUnit());
                    }

                    return existingInventory;
                }
            )
            .flatMap(inventoryRepository::save);
    }

    /**
     * Get all the inventories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Flux<Inventory> findAll() {
        log.debug("Request to get all Inventories");
        return inventoryRepository.findAll();
    }

    /**
     * Returns the number of inventories available.
     * @return the number of entities in the database.
     *
     */
    public Mono<Long> countAll() {
        return inventoryRepository.count();
    }

    /**
     * Get one inventory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Mono<Inventory> findOne(Long id) {
        log.debug("Request to get Inventory : {}", id);
        return inventoryRepository.findById(id);
    }

    /**
     * Delete the inventory by id.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Inventory : {}", id);
        return inventoryRepository.deleteById(id);
    }
}
