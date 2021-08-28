package com.chanctn.jhipster_sample_application.web.rest;

import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameInstant;
import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.OrderInfo;
import com.chanctn.jhipster_sample_application.repository.OrderInfoRepository;
import com.chanctn.jhipster_sample_application.service.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link OrderInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class OrderInfoResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final BigDecimal DEFAULT_PRICE_PER_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_UNIT = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/order-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderInfoRepository orderInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private OrderInfo orderInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderInfo createEntity(EntityManager em) {
        OrderInfo orderInfo = new OrderInfo()
            .quantity(DEFAULT_QUANTITY)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .createAt(DEFAULT_CREATE_AT)
            .modifyAt(DEFAULT_MODIFY_AT);
        return orderInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderInfo createUpdatedEntity(EntityManager em) {
        OrderInfo orderInfo = new OrderInfo()
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);
        return orderInfo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(OrderInfo.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        orderInfo = createEntity(em);
    }

    @Test
    void createOrderInfo() throws Exception {
        int databaseSizeBeforeCreate = orderInfoRepository.findAll().collectList().block().size();
        // Create the OrderInfo
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeCreate + 1);
        OrderInfo testOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
        assertThat(testOrderInfo.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderInfo.getPricePerUnit()).isEqualByComparingTo(DEFAULT_PRICE_PER_UNIT);
        assertThat(testOrderInfo.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testOrderInfo.getModifyAt()).isEqualTo(DEFAULT_MODIFY_AT);
    }

    @Test
    void createOrderInfoWithExistingId() throws Exception {
        // Create the OrderInfo with an existing ID
        orderInfo.setId(1L);

        int databaseSizeBeforeCreate = orderInfoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderInfoRepository.findAll().collectList().block().size();
        // set the field null
        orderInfo.setQuantity(null);

        // Create the OrderInfo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPricePerUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderInfoRepository.findAll().collectList().block().size();
        // set the field null
        orderInfo.setPricePerUnit(null);

        // Create the OrderInfo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreateAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderInfoRepository.findAll().collectList().block().size();
        // set the field null
        orderInfo.setCreateAt(null);

        // Create the OrderInfo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifyAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderInfoRepository.findAll().collectList().block().size();
        // set the field null
        orderInfo.setModifyAt(null);

        // Create the OrderInfo, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOrderInfos() {
        // Initialize the database
        orderInfoRepository.save(orderInfo).block();

        // Get all the orderInfoList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(orderInfo.getId().intValue()))
            .jsonPath("$.[*].quantity")
            .value(hasItem(DEFAULT_QUANTITY))
            .jsonPath("$.[*].pricePerUnit")
            .value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT)))
            .jsonPath("$.[*].createAt")
            .value(hasItem(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.[*].modifyAt")
            .value(hasItem(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getOrderInfo() {
        // Initialize the database
        orderInfoRepository.save(orderInfo).block();

        // Get the orderInfo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, orderInfo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(orderInfo.getId().intValue()))
            .jsonPath("$.quantity")
            .value(is(DEFAULT_QUANTITY))
            .jsonPath("$.pricePerUnit")
            .value(is(sameNumber(DEFAULT_PRICE_PER_UNIT)))
            .jsonPath("$.createAt")
            .value(is(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.modifyAt")
            .value(is(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getNonExistingOrderInfo() {
        // Get the orderInfo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewOrderInfo() throws Exception {
        // Initialize the database
        orderInfoRepository.save(orderInfo).block();

        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();

        // Update the orderInfo
        OrderInfo updatedOrderInfo = orderInfoRepository.findById(orderInfo.getId()).block();
        updatedOrderInfo
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOrderInfo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOrderInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
        OrderInfo testOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
        assertThat(testOrderInfo.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderInfo.getPricePerUnit()).isEqualTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testOrderInfo.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testOrderInfo.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void putNonExistingOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();
        orderInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, orderInfo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();
        orderInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();
        orderInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrderInfoWithPatch() throws Exception {
        // Initialize the database
        orderInfoRepository.save(orderInfo).block();

        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();

        // Update the orderInfo using partial update
        OrderInfo partialUpdatedOrderInfo = new OrderInfo();
        partialUpdatedOrderInfo.setId(orderInfo.getId());

        partialUpdatedOrderInfo.quantity(UPDATED_QUANTITY).pricePerUnit(UPDATED_PRICE_PER_UNIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrderInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
        OrderInfo testOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
        assertThat(testOrderInfo.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderInfo.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testOrderInfo.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testOrderInfo.getModifyAt()).isEqualTo(DEFAULT_MODIFY_AT);
    }

    @Test
    void fullUpdateOrderInfoWithPatch() throws Exception {
        // Initialize the database
        orderInfoRepository.save(orderInfo).block();

        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();

        // Update the orderInfo using partial update
        OrderInfo partialUpdatedOrderInfo = new OrderInfo();
        partialUpdatedOrderInfo.setId(orderInfo.getId());

        partialUpdatedOrderInfo
            .quantity(UPDATED_QUANTITY)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrderInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrderInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
        OrderInfo testOrderInfo = orderInfoList.get(orderInfoList.size() - 1);
        assertThat(testOrderInfo.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderInfo.getPricePerUnit()).isEqualByComparingTo(UPDATED_PRICE_PER_UNIT);
        assertThat(testOrderInfo.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testOrderInfo.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void patchNonExistingOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();
        orderInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, orderInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();
        orderInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrderInfo() throws Exception {
        int databaseSizeBeforeUpdate = orderInfoRepository.findAll().collectList().block().size();
        orderInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(orderInfo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the OrderInfo in the database
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrderInfo() {
        // Initialize the database
        orderInfoRepository.save(orderInfo).block();

        int databaseSizeBeforeDelete = orderInfoRepository.findAll().collectList().block().size();

        // Delete the orderInfo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, orderInfo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<OrderInfo> orderInfoList = orderInfoRepository.findAll().collectList().block();
        assertThat(orderInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
