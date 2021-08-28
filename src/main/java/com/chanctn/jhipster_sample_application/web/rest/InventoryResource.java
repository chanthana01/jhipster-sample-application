package com.chanctn.jhipster_sample_application.web.rest;

import com.chanctn.jhipster_sample_application.domain.Inventory;
import com.chanctn.jhipster_sample_application.repository.InventoryRepository;
import com.chanctn.jhipster_sample_application.service.InventoryService;
import com.chanctn.jhipster_sample_application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.chanctn.jhipster_sample_application.domain.Inventory}.
 */
@RestController
@RequestMapping("/api")
public class InventoryResource {

    private final Logger log = LoggerFactory.getLogger(InventoryResource.class);

    private static final String ENTITY_NAME = "inventory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InventoryService inventoryService;

    private final InventoryRepository inventoryRepository;

    public InventoryResource(InventoryService inventoryService, InventoryRepository inventoryRepository) {
        this.inventoryService = inventoryService;
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * {@code POST  /inventories} : Create a new inventory.
     *
     * @param inventory the inventory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inventory, or with status {@code 400 (Bad Request)} if the inventory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/inventories")
    public Mono<ResponseEntity<Inventory>> createInventory(@Valid @RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to save Inventory : {}", inventory);
        if (inventory.getId() != null) {
            throw new BadRequestAlertException("A new inventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return inventoryService
            .save(inventory)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/inventories/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /inventories/:id} : Updates an existing inventory.
     *
     * @param id the id of the inventory to save.
     * @param inventory the inventory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventory,
     * or with status {@code 400 (Bad Request)} if the inventory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inventory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/inventories/{id}")
    public Mono<ResponseEntity<Inventory>> updateInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Inventory inventory
    ) throws URISyntaxException {
        log.debug("REST request to update Inventory : {}, {}", id, inventory);
        if (inventory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inventoryRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return inventoryService
                        .save(inventory)
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            result ->
                                ResponseEntity
                                    .ok()
                                    .headers(
                                        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString())
                                    )
                                    .body(result)
                        );
                }
            );
    }

    /**
     * {@code PATCH  /inventories/:id} : Partial updates given fields of an existing inventory, field will ignore if it is null
     *
     * @param id the id of the inventory to save.
     * @param inventory the inventory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inventory,
     * or with status {@code 400 (Bad Request)} if the inventory is not valid,
     * or with status {@code 404 (Not Found)} if the inventory is not found,
     * or with status {@code 500 (Internal Server Error)} if the inventory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/inventories/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Inventory>> partialUpdateInventory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Inventory inventory
    ) throws URISyntaxException {
        log.debug("REST request to partial update Inventory partially : {}, {}", id, inventory);
        if (inventory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inventory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return inventoryRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Inventory> result = inventoryService.partialUpdate(inventory);

                    return result
                        .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                        .map(
                            res ->
                                ResponseEntity
                                    .ok()
                                    .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                                    .body(res)
                        );
                }
            );
    }

    /**
     * {@code GET  /inventories} : get all the inventories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inventories in body.
     */
    @GetMapping("/inventories")
    public Mono<List<Inventory>> getAllInventories() {
        log.debug("REST request to get all Inventories");
        return inventoryService.findAll().collectList();
    }

    /**
     * {@code GET  /inventories} : get all the inventories as a stream.
     * @return the {@link Flux} of inventories.
     */
    @GetMapping(value = "/inventories", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Inventory> getAllInventoriesAsStream() {
        log.debug("REST request to get all Inventories as a stream");
        return inventoryService.findAll();
    }

    /**
     * {@code GET  /inventories/:id} : get the "id" inventory.
     *
     * @param id the id of the inventory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inventory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/inventories/{id}")
    public Mono<ResponseEntity<Inventory>> getInventory(@PathVariable Long id) {
        log.debug("REST request to get Inventory : {}", id);
        Mono<Inventory> inventory = inventoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inventory);
    }

    /**
     * {@code DELETE  /inventories/:id} : delete the "id" inventory.
     *
     * @param id the id of the inventory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/inventories/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteInventory(@PathVariable Long id) {
        log.debug("REST request to delete Inventory : {}", id);
        return inventoryService
            .delete(id)
            .map(
                result ->
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
            );
    }
}
