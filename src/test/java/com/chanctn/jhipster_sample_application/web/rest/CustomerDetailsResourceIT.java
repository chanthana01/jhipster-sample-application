package com.chanctn.jhipster_sample_application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.CustomerDetails;
import com.chanctn.jhipster_sample_application.domain.User;
import com.chanctn.jhipster_sample_application.repository.CustomerDetailsRepository;
import com.chanctn.jhipster_sample_application.service.EntityManager;
import java.time.Duration;
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
 * Integration tests for the {@link CustomerDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class CustomerDetailsResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/customer-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CustomerDetailsRepository customerDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CustomerDetails customerDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerDetails createEntity(EntityManager em) {
        CustomerDetails customerDetails = new CustomerDetails()
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        customerDetails.setUser(user);
        return customerDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerDetails createUpdatedEntity(EntityManager em) {
        CustomerDetails customerDetails = new CustomerDetails()
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);
        // Add required entity
        User user = em.insert(UserResourceIT.createEntity(em)).block();
        customerDetails.setUser(user);
        return customerDetails;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CustomerDetails.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        UserResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        customerDetails = createEntity(em);
    }

    @Test
    void createCustomerDetails() throws Exception {
        int databaseSizeBeforeCreate = customerDetailsRepository.findAll().collectList().block().size();
        // Create the CustomerDetails
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerDetails testCustomerDetails = customerDetailsList.get(customerDetailsList.size() - 1);
        assertThat(testCustomerDetails.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomerDetails.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testCustomerDetails.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testCustomerDetails.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomerDetails.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    void createCustomerDetailsWithExistingId() throws Exception {
        // Create the CustomerDetails with an existing ID
        customerDetails.setId(1L);

        int databaseSizeBeforeCreate = customerDetailsRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerDetailsRepository.findAll().collectList().block().size();
        // set the field null
        customerDetails.setPhone(null);

        // Create the CustomerDetails, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAddressLine1IsRequired() throws Exception {
        int databaseSizeBeforeTest = customerDetailsRepository.findAll().collectList().block().size();
        // set the field null
        customerDetails.setAddressLine1(null);

        // Create the CustomerDetails, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCityIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerDetailsRepository.findAll().collectList().block().size();
        // set the field null
        customerDetails.setCity(null);

        // Create the CustomerDetails, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerDetailsRepository.findAll().collectList().block().size();
        // set the field null
        customerDetails.setCountry(null);

        // Create the CustomerDetails, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCustomerDetailsAsStream() {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        List<CustomerDetails> customerDetailsList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(CustomerDetails.class)
            .getResponseBody()
            .filter(customerDetails::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(customerDetailsList).isNotNull();
        assertThat(customerDetailsList).hasSize(1);
        CustomerDetails testCustomerDetails = customerDetailsList.get(0);
        assertThat(testCustomerDetails.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomerDetails.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testCustomerDetails.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testCustomerDetails.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomerDetails.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    void getAllCustomerDetails() {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        // Get all the customerDetailsList
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
            .value(hasItem(customerDetails.getId().intValue()))
            .jsonPath("$.[*].phone")
            .value(hasItem(DEFAULT_PHONE))
            .jsonPath("$.[*].addressLine1")
            .value(hasItem(DEFAULT_ADDRESS_LINE_1))
            .jsonPath("$.[*].addressLine2")
            .value(hasItem(DEFAULT_ADDRESS_LINE_2))
            .jsonPath("$.[*].city")
            .value(hasItem(DEFAULT_CITY))
            .jsonPath("$.[*].country")
            .value(hasItem(DEFAULT_COUNTRY));
    }

    @Test
    void getCustomerDetails() {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        // Get the customerDetails
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, customerDetails.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(customerDetails.getId().intValue()))
            .jsonPath("$.phone")
            .value(is(DEFAULT_PHONE))
            .jsonPath("$.addressLine1")
            .value(is(DEFAULT_ADDRESS_LINE_1))
            .jsonPath("$.addressLine2")
            .value(is(DEFAULT_ADDRESS_LINE_2))
            .jsonPath("$.city")
            .value(is(DEFAULT_CITY))
            .jsonPath("$.country")
            .value(is(DEFAULT_COUNTRY));
    }

    @Test
    void getNonExistingCustomerDetails() {
        // Get the customerDetails
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCustomerDetails() throws Exception {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();

        // Update the customerDetails
        CustomerDetails updatedCustomerDetails = customerDetailsRepository.findById(customerDetails.getId()).block();
        updatedCustomerDetails
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCustomerDetails.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedCustomerDetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
        CustomerDetails testCustomerDetails = customerDetailsList.get(customerDetailsList.size() - 1);
        assertThat(testCustomerDetails.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomerDetails.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testCustomerDetails.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testCustomerDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomerDetails.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    void putNonExistingCustomerDetails() throws Exception {
        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();
        customerDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, customerDetails.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCustomerDetails() throws Exception {
        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();
        customerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCustomerDetails() throws Exception {
        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();
        customerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCustomerDetailsWithPatch() throws Exception {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();

        // Update the customerDetails using partial update
        CustomerDetails partialUpdatedCustomerDetails = new CustomerDetails();
        partialUpdatedCustomerDetails.setId(customerDetails.getId());

        partialUpdatedCustomerDetails.addressLine1(UPDATED_ADDRESS_LINE_1).addressLine2(UPDATED_ADDRESS_LINE_2).city(UPDATED_CITY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomerDetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerDetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
        CustomerDetails testCustomerDetails = customerDetailsList.get(customerDetailsList.size() - 1);
        assertThat(testCustomerDetails.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomerDetails.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testCustomerDetails.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testCustomerDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomerDetails.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    void fullUpdateCustomerDetailsWithPatch() throws Exception {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();

        // Update the customerDetails using partial update
        CustomerDetails partialUpdatedCustomerDetails = new CustomerDetails();
        partialUpdatedCustomerDetails.setId(customerDetails.getId());

        partialUpdatedCustomerDetails
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCustomerDetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCustomerDetails))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
        CustomerDetails testCustomerDetails = customerDetailsList.get(customerDetailsList.size() - 1);
        assertThat(testCustomerDetails.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomerDetails.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testCustomerDetails.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testCustomerDetails.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomerDetails.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    void patchNonExistingCustomerDetails() throws Exception {
        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();
        customerDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, customerDetails.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCustomerDetails() throws Exception {
        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();
        customerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCustomerDetails() throws Exception {
        int databaseSizeBeforeUpdate = customerDetailsRepository.findAll().collectList().block().size();
        customerDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(customerDetails))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CustomerDetails in the database
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCustomerDetails() {
        // Initialize the database
        customerDetailsRepository.save(customerDetails).block();

        int databaseSizeBeforeDelete = customerDetailsRepository.findAll().collectList().block().size();

        // Delete the customerDetails
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, customerDetails.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<CustomerDetails> customerDetailsList = customerDetailsRepository.findAll().collectList().block();
        assertThat(customerDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
