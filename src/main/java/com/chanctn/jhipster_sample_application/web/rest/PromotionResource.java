package com.chanctn.jhipster_sample_application.web.rest;

import com.chanctn.jhipster_sample_application.domain.Promotion;
import com.chanctn.jhipster_sample_application.repository.PromotionRepository;
import com.chanctn.jhipster_sample_application.service.PromotionService;
import com.chanctn.jhipster_sample_application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.chanctn.jhipster_sample_application.domain.Promotion}.
 */
@RestController
@RequestMapping("/api")
public class PromotionResource {

    private final Logger log = LoggerFactory.getLogger(PromotionResource.class);

    private static final String ENTITY_NAME = "promotion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotionService promotionService;

    private final PromotionRepository promotionRepository;

    public PromotionResource(PromotionService promotionService, PromotionRepository promotionRepository) {
        this.promotionService = promotionService;
        this.promotionRepository = promotionRepository;
    }

    /**
     * {@code POST  /promotions} : Create a new promotion.
     *
     * @param promotion the promotion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promotion, or with status {@code 400 (Bad Request)} if the promotion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promotions")
    public Mono<ResponseEntity<Promotion>> createPromotion(@Valid @RequestBody Promotion promotion) throws URISyntaxException {
        log.debug("REST request to save Promotion : {}", promotion);
        if (promotion.getId() != null) {
            throw new BadRequestAlertException("A new promotion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return promotionService
            .save(promotion)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/promotions/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /promotions/:id} : Updates an existing promotion.
     *
     * @param id the id of the promotion to save.
     * @param promotion the promotion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotion,
     * or with status {@code 400 (Bad Request)} if the promotion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promotion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promotions/{id}")
    public Mono<ResponseEntity<Promotion>> updatePromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Promotion promotion
    ) throws URISyntaxException {
        log.debug("REST request to update Promotion : {}, {}", id, promotion);
        if (promotion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return promotionRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return promotionService
                        .save(promotion)
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
     * {@code PATCH  /promotions/:id} : Partial updates given fields of an existing promotion, field will ignore if it is null
     *
     * @param id the id of the promotion to save.
     * @param promotion the promotion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotion,
     * or with status {@code 400 (Bad Request)} if the promotion is not valid,
     * or with status {@code 404 (Not Found)} if the promotion is not found,
     * or with status {@code 500 (Internal Server Error)} if the promotion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promotions/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<Promotion>> partialUpdatePromotion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Promotion promotion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Promotion partially : {}, {}", id, promotion);
        if (promotion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return promotionRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<Promotion> result = promotionService.partialUpdate(promotion);

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
     * {@code GET  /promotions} : get all the promotions.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotions in body.
     */
    @GetMapping("/promotions")
    public Mono<ResponseEntity<List<Promotion>>> getAllPromotions(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Promotions");
        return promotionService
            .countAll()
            .zipWith(promotionService.findAll(pageable).collectList())
            .map(
                countWithEntities -> {
                    return ResponseEntity
                        .ok()
                        .headers(
                            PaginationUtil.generatePaginationHttpHeaders(
                                UriComponentsBuilder.fromHttpRequest(request),
                                new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                            )
                        )
                        .body(countWithEntities.getT2());
                }
            );
    }

    /**
     * {@code GET  /promotions/:id} : get the "id" promotion.
     *
     * @param id the id of the promotion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promotions/{id}")
    public Mono<ResponseEntity<Promotion>> getPromotion(@PathVariable Long id) {
        log.debug("REST request to get Promotion : {}", id);
        Mono<Promotion> promotion = promotionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promotion);
    }

    /**
     * {@code DELETE  /promotions/:id} : delete the "id" promotion.
     *
     * @param id the id of the promotion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promotions/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePromotion(@PathVariable Long id) {
        log.debug("REST request to delete Promotion : {}", id);
        return promotionService
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
