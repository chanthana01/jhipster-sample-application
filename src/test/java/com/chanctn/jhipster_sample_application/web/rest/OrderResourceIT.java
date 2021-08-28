package com.chanctn.jhipster_sample_application.web.rest;

import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameInstant;
import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.Order;
import com.chanctn.jhipster_sample_application.repository.OrderRepository;
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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class OrderResourceIT {

    private static final String DEFAULT_CUSTOMER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_ADDRESS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_OMISE_TXN_ID = "AAAAAAAAAA";
    private static final String UPDATED_OMISE_TXN_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TXN_TIME_STAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TXN_TIME_STAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_TXN_SUCCESS = false;
    private static final Boolean UPDATED_IS_TXN_SUCCESS = true;

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .customerName(DEFAULT_CUSTOMER_NAME)
            .orderAddress(DEFAULT_ORDER_ADDRESS)
            .totalAmount(DEFAULT_TOTAL_AMOUNT)
            .omiseTxnId(DEFAULT_OMISE_TXN_ID)
            .txnTimeStamp(DEFAULT_TXN_TIME_STAMP)
            .isTxnSuccess(DEFAULT_IS_TXN_SUCCESS)
            .createAt(DEFAULT_CREATE_AT)
            .modifyAt(DEFAULT_MODIFY_AT);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .customerName(UPDATED_CUSTOMER_NAME)
            .orderAddress(UPDATED_ORDER_ADDRESS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .omiseTxnId(UPDATED_OMISE_TXN_ID)
            .txnTimeStamp(UPDATED_TXN_TIME_STAMP)
            .isTxnSuccess(UPDATED_IS_TXN_SUCCESS)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);
        return order;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Order.class).block();
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
        order = createEntity(em);
    }

    @Test
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().collectList().block().size();
        // Create the Order
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getCustomerName()).isEqualTo(DEFAULT_CUSTOMER_NAME);
        assertThat(testOrder.getOrderAddress()).isEqualTo(DEFAULT_ORDER_ADDRESS);
        assertThat(testOrder.getTotalAmount()).isEqualByComparingTo(DEFAULT_TOTAL_AMOUNT);
        assertThat(testOrder.getOmiseTxnId()).isEqualTo(DEFAULT_OMISE_TXN_ID);
        assertThat(testOrder.getTxnTimeStamp()).isEqualTo(DEFAULT_TXN_TIME_STAMP);
        assertThat(testOrder.getIsTxnSuccess()).isEqualTo(DEFAULT_IS_TXN_SUCCESS);
        assertThat(testOrder.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testOrder.getModifyAt()).isEqualTo(DEFAULT_MODIFY_AT);
    }

    @Test
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);

        int databaseSizeBeforeCreate = orderRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCustomerNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setCustomerName(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOrderAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setOrderAddress(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTotalAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setTotalAmount(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOmiseTxnIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setOmiseTxnId(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTxnTimeStampIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setTxnTimeStamp(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkIsTxnSuccessIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setIsTxnSuccess(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreateAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setCreateAt(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifyAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().collectList().block().size();
        // set the field null
        order.setModifyAt(null);

        // Create the Order, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllOrders() {
        // Initialize the database
        orderRepository.save(order).block();

        // Get all the orderList
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
            .value(hasItem(order.getId().intValue()))
            .jsonPath("$.[*].customerName")
            .value(hasItem(DEFAULT_CUSTOMER_NAME))
            .jsonPath("$.[*].orderAddress")
            .value(hasItem(DEFAULT_ORDER_ADDRESS))
            .jsonPath("$.[*].totalAmount")
            .value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .jsonPath("$.[*].omiseTxnId")
            .value(hasItem(DEFAULT_OMISE_TXN_ID))
            .jsonPath("$.[*].txnTimeStamp")
            .value(hasItem(sameInstant(DEFAULT_TXN_TIME_STAMP)))
            .jsonPath("$.[*].isTxnSuccess")
            .value(hasItem(DEFAULT_IS_TXN_SUCCESS.booleanValue()))
            .jsonPath("$.[*].createAt")
            .value(hasItem(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.[*].modifyAt")
            .value(hasItem(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getOrder() {
        // Initialize the database
        orderRepository.save(order).block();

        // Get the order
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, order.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(order.getId().intValue()))
            .jsonPath("$.customerName")
            .value(is(DEFAULT_CUSTOMER_NAME))
            .jsonPath("$.orderAddress")
            .value(is(DEFAULT_ORDER_ADDRESS))
            .jsonPath("$.totalAmount")
            .value(is(sameNumber(DEFAULT_TOTAL_AMOUNT)))
            .jsonPath("$.omiseTxnId")
            .value(is(DEFAULT_OMISE_TXN_ID))
            .jsonPath("$.txnTimeStamp")
            .value(is(sameInstant(DEFAULT_TXN_TIME_STAMP)))
            .jsonPath("$.isTxnSuccess")
            .value(is(DEFAULT_IS_TXN_SUCCESS.booleanValue()))
            .jsonPath("$.createAt")
            .value(is(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.modifyAt")
            .value(is(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getNonExistingOrder() {
        // Get the order
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewOrder() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).block();
        updatedOrder
            .customerName(UPDATED_CUSTOMER_NAME)
            .orderAddress(UPDATED_ORDER_ADDRESS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .omiseTxnId(UPDATED_OMISE_TXN_ID)
            .txnTimeStamp(UPDATED_TXN_TIME_STAMP)
            .isTxnSuccess(UPDATED_IS_TXN_SUCCESS)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedOrder.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testOrder.getOrderAddress()).isEqualTo(UPDATED_ORDER_ADDRESS);
        assertThat(testOrder.getTotalAmount()).isEqualTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testOrder.getOmiseTxnId()).isEqualTo(UPDATED_OMISE_TXN_ID);
        assertThat(testOrder.getTxnTimeStamp()).isEqualTo(UPDATED_TXN_TIME_STAMP);
        assertThat(testOrder.getIsTxnSuccess()).isEqualTo(UPDATED_IS_TXN_SUCCESS);
        assertThat(testOrder.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testOrder.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .customerName(UPDATED_CUSTOMER_NAME)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .omiseTxnId(UPDATED_OMISE_TXN_ID)
            .txnTimeStamp(UPDATED_TXN_TIME_STAMP)
            .isTxnSuccess(UPDATED_IS_TXN_SUCCESS)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testOrder.getOrderAddress()).isEqualTo(DEFAULT_ORDER_ADDRESS);
        assertThat(testOrder.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testOrder.getOmiseTxnId()).isEqualTo(UPDATED_OMISE_TXN_ID);
        assertThat(testOrder.getTxnTimeStamp()).isEqualTo(UPDATED_TXN_TIME_STAMP);
        assertThat(testOrder.getIsTxnSuccess()).isEqualTo(UPDATED_IS_TXN_SUCCESS);
        assertThat(testOrder.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testOrder.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .customerName(UPDATED_CUSTOMER_NAME)
            .orderAddress(UPDATED_ORDER_ADDRESS)
            .totalAmount(UPDATED_TOTAL_AMOUNT)
            .omiseTxnId(UPDATED_OMISE_TXN_ID)
            .txnTimeStamp(UPDATED_TXN_TIME_STAMP)
            .isTxnSuccess(UPDATED_IS_TXN_SUCCESS)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getCustomerName()).isEqualTo(UPDATED_CUSTOMER_NAME);
        assertThat(testOrder.getOrderAddress()).isEqualTo(UPDATED_ORDER_ADDRESS);
        assertThat(testOrder.getTotalAmount()).isEqualByComparingTo(UPDATED_TOTAL_AMOUNT);
        assertThat(testOrder.getOmiseTxnId()).isEqualTo(UPDATED_OMISE_TXN_ID);
        assertThat(testOrder.getTxnTimeStamp()).isEqualTo(UPDATED_TXN_TIME_STAMP);
        assertThat(testOrder.getIsTxnSuccess()).isEqualTo(UPDATED_IS_TXN_SUCCESS);
        assertThat(testOrder.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testOrder.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, order.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().collectList().block().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(order))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteOrder() {
        // Initialize the database
        orderRepository.save(order).block();

        int databaseSizeBeforeDelete = orderRepository.findAll().collectList().block().size();

        // Delete the order
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, order.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll().collectList().block();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
