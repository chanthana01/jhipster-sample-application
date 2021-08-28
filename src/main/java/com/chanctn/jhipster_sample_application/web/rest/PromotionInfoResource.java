package com.chanctn.jhipster_sample_application.web.rest;

import com.chanctn.jhipster_sample_application.domain.PromotionInfo;
import com.chanctn.jhipster_sample_application.repository.PromotionInfoRepository;
import com.chanctn.jhipster_sample_application.service.PromotionInfoService;
import com.chanctn.jhipster_sample_application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.chanctn.jhipster_sample_application.domain.PromotionInfo}.
 */
@RestController
@RequestMapping("/api")
public class PromotionInfoResource {

    private final Logger log = LoggerFactory.getLogger(PromotionInfoResource.class);

    private static final String ENTITY_NAME = "promotionInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromotionInfoService promotionInfoService;

    private final PromotionInfoRepository promotionInfoRepository;

    public PromotionInfoResource(PromotionInfoService promotionInfoService, PromotionInfoRepository promotionInfoRepository) {
        this.promotionInfoService = promotionInfoService;
        this.promotionInfoRepository = promotionInfoRepository;
    }

    /**
     * {@code POST  /promotion-infos} : Create a new promotionInfo.
     *
     * @param promotionInfo the promotionInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promotionInfo, or with status {@code 400 (Bad Request)} if the promotionInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promotion-infos")
    public Mono<ResponseEntity<PromotionInfo>> createPromotionInfo(@RequestBody PromotionInfo promotionInfo) throws URISyntaxException {
        log.debug("REST request to save PromotionInfo : {}", promotionInfo);
        if (promotionInfo.getId() != null) {
            throw new BadRequestAlertException("A new promotionInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return promotionInfoService
            .save(promotionInfo)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/promotion-infos/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /promotion-infos/:id} : Updates an existing promotionInfo.
     *
     * @param id the id of the promotionInfo to save.
     * @param promotionInfo the promotionInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionInfo,
     * or with status {@code 400 (Bad Request)} if the promotionInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promotionInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promotion-infos/{id}")
    public Mono<ResponseEntity<PromotionInfo>> updatePromotionInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromotionInfo promotionInfo
    ) throws URISyntaxException {
        log.debug("REST request to update PromotionInfo : {}, {}", id, promotionInfo);
        if (promotionInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotionInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return promotionInfoRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return promotionInfoService
                        .save(promotionInfo)
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
     * {@code PATCH  /promotion-infos/:id} : Partial updates given fields of an existing promotionInfo, field will ignore if it is null
     *
     * @param id the id of the promotionInfo to save.
     * @param promotionInfo the promotionInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promotionInfo,
     * or with status {@code 400 (Bad Request)} if the promotionInfo is not valid,
     * or with status {@code 404 (Not Found)} if the promotionInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the promotionInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promotion-infos/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<PromotionInfo>> partialUpdatePromotionInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PromotionInfo promotionInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update PromotionInfo partially : {}, {}", id, promotionInfo);
        if (promotionInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promotionInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return promotionInfoRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<PromotionInfo> result = promotionInfoService.partialUpdate(promotionInfo);

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
     * {@code GET  /promotion-infos} : get all the promotionInfos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promotionInfos in body.
     */
    @GetMapping("/promotion-infos")
    public Mono<ResponseEntity<List<PromotionInfo>>> getAllPromotionInfos(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of PromotionInfos");
        return promotionInfoService
            .countAll()
            .zipWith(promotionInfoService.findAll(pageable).collectList())
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
     * {@code GET  /promotion-infos/:id} : get the "id" promotionInfo.
     *
     * @param id the id of the promotionInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promotionInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promotion-infos/{id}")
    public Mono<ResponseEntity<PromotionInfo>> getPromotionInfo(@PathVariable Long id) {
        log.debug("REST request to get PromotionInfo : {}", id);
        Mono<PromotionInfo> promotionInfo = promotionInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promotionInfo);
    }

    /**
     * {@code DELETE  /promotion-infos/:id} : delete the "id" promotionInfo.
     *
     * @param id the id of the promotionInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promotion-infos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePromotionInfo(@PathVariable Long id) {
        log.debug("REST request to delete PromotionInfo : {}", id);
        return promotionInfoService
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
