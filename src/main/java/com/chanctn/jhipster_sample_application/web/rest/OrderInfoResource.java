package com.chanctn.jhipster_sample_application.web.rest;

import com.chanctn.jhipster_sample_application.domain.OrderInfo;
import com.chanctn.jhipster_sample_application.repository.OrderInfoRepository;
import com.chanctn.jhipster_sample_application.service.OrderInfoService;
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
 * REST controller for managing {@link com.chanctn.jhipster_sample_application.domain.OrderInfo}.
 */
@RestController
@RequestMapping("/api")
public class OrderInfoResource {

    private final Logger log = LoggerFactory.getLogger(OrderInfoResource.class);

    private static final String ENTITY_NAME = "orderInfo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderInfoService orderInfoService;

    private final OrderInfoRepository orderInfoRepository;

    public OrderInfoResource(OrderInfoService orderInfoService, OrderInfoRepository orderInfoRepository) {
        this.orderInfoService = orderInfoService;
        this.orderInfoRepository = orderInfoRepository;
    }

    /**
     * {@code POST  /order-infos} : Create a new orderInfo.
     *
     * @param orderInfo the orderInfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderInfo, or with status {@code 400 (Bad Request)} if the orderInfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-infos")
    public Mono<ResponseEntity<OrderInfo>> createOrderInfo(@Valid @RequestBody OrderInfo orderInfo) throws URISyntaxException {
        log.debug("REST request to save OrderInfo : {}", orderInfo);
        if (orderInfo.getId() != null) {
            throw new BadRequestAlertException("A new orderInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return orderInfoService
            .save(orderInfo)
            .map(
                result -> {
                    try {
                        return ResponseEntity
                            .created(new URI("/api/order-infos/" + result.getId()))
                            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    /**
     * {@code PUT  /order-infos/:id} : Updates an existing orderInfo.
     *
     * @param id the id of the orderInfo to save.
     * @param orderInfo the orderInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderInfo,
     * or with status {@code 400 (Bad Request)} if the orderInfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-infos/{id}")
    public Mono<ResponseEntity<OrderInfo>> updateOrderInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderInfo orderInfo
    ) throws URISyntaxException {
        log.debug("REST request to update OrderInfo : {}, {}", id, orderInfo);
        if (orderInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return orderInfoRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    return orderInfoService
                        .save(orderInfo)
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
     * {@code PATCH  /order-infos/:id} : Partial updates given fields of an existing orderInfo, field will ignore if it is null
     *
     * @param id the id of the orderInfo to save.
     * @param orderInfo the orderInfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderInfo,
     * or with status {@code 400 (Bad Request)} if the orderInfo is not valid,
     * or with status {@code 404 (Not Found)} if the orderInfo is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderInfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/order-infos/{id}", consumes = "application/merge-patch+json")
    public Mono<ResponseEntity<OrderInfo>> partialUpdateOrderInfo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderInfo orderInfo
    ) throws URISyntaxException {
        log.debug("REST request to partial update OrderInfo partially : {}, {}", id, orderInfo);
        if (orderInfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderInfo.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return orderInfoRepository
            .existsById(id)
            .flatMap(
                exists -> {
                    if (!exists) {
                        return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                    }

                    Mono<OrderInfo> result = orderInfoService.partialUpdate(orderInfo);

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
     * {@code GET  /order-infos} : get all the orderInfos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderInfos in body.
     */
    @GetMapping("/order-infos")
    public Mono<ResponseEntity<List<OrderInfo>>> getAllOrderInfos(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of OrderInfos");
        return orderInfoService
            .countAll()
            .zipWith(orderInfoService.findAll(pageable).collectList())
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
     * {@code GET  /order-infos/:id} : get the "id" orderInfo.
     *
     * @param id the id of the orderInfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderInfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-infos/{id}")
    public Mono<ResponseEntity<OrderInfo>> getOrderInfo(@PathVariable Long id) {
        log.debug("REST request to get OrderInfo : {}", id);
        Mono<OrderInfo> orderInfo = orderInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderInfo);
    }

    /**
     * {@code DELETE  /order-infos/:id} : delete the "id" orderInfo.
     *
     * @param id the id of the orderInfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-infos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteOrderInfo(@PathVariable Long id) {
        log.debug("REST request to delete OrderInfo : {}", id);
        return orderInfoService
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
