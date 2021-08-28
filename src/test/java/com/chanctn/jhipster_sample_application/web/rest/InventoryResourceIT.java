package com.chanctn.jhipster_sample_application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.Inventory;
import com.chanctn.jhipster_sample_application.repository.InventoryRepository;
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
 * Integration tests for the {@link InventoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class InventoryResourceIT {

    private static final Integer DEFAULT_UNIT = 1;
    private static final Integer UPDATED_UNIT = 2;

    private static final String ENTITY_API_URL = "/api/inventories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Inventory inventory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createEntity(EntityManager em) {
        Inventory inventory = new Inventory().unit(DEFAULT_UNIT);
        return inventory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Inventory createUpdatedEntity(EntityManager em) {
        Inventory inventory = new Inventory().unit(UPDATED_UNIT);
        return inventory;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Inventory.class).block();
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
        inventory = createEntity(em);
    }

    @Test
    void createInventory() throws Exception {
        int databaseSizeBeforeCreate = inventoryRepository.findAll().collectList().block().size();
        // Create the Inventory
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate + 1);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    void createInventoryWithExistingId() throws Exception {
        // Create the Inventory with an existing ID
        inventory.setId(1L);

        int databaseSizeBeforeCreate = inventoryRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = inventoryRepository.findAll().collectList().block().size();
        // set the field null
        inventory.setUnit(null);

        // Create the Inventory, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllInventoriesAsStream() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        List<Inventory> inventoryList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(Inventory.class)
            .getResponseBody()
            .filter(inventory::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(inventoryList).isNotNull();
        assertThat(inventoryList).hasSize(1);
        Inventory testInventory = inventoryList.get(0);
        assertThat(testInventory.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    void getAllInventories() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        // Get all the inventoryList
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
            .value(hasItem(inventory.getId().intValue()))
            .jsonPath("$.[*].unit")
            .value(hasItem(DEFAULT_UNIT));
    }

    @Test
    void getInventory() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        // Get the inventory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(inventory.getId().intValue()))
            .jsonPath("$.unit")
            .value(is(DEFAULT_UNIT));
    }

    @Test
    void getNonExistingInventory() {
        // Get the inventory
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewInventory() throws Exception {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();

        // Update the inventory
        Inventory updatedInventory = inventoryRepository.findById(inventory.getId()).block();
        updatedInventory.unit(UPDATED_UNIT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedInventory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedInventory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    void putNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    void fullUpdateInventoryWithPatch() throws Exception {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();

        // Update the inventory using partial update
        Inventory partialUpdatedInventory = new Inventory();
        partialUpdatedInventory.setId(inventory.getId());

        partialUpdatedInventory.unit(UPDATED_UNIT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedInventory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedInventory))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
        Inventory testInventory = inventoryList.get(inventoryList.size() - 1);
        assertThat(testInventory.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    void patchNonExistingInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInventory() throws Exception {
        int databaseSizeBeforeUpdate = inventoryRepository.findAll().collectList().block().size();
        inventory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(inventory))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Inventory in the database
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInventory() {
        // Initialize the database
        inventoryRepository.save(inventory).block();

        int databaseSizeBeforeDelete = inventoryRepository.findAll().collectList().block().size();

        // Delete the inventory
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, inventory.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Inventory> inventoryList = inventoryRepository.findAll().collectList().block();
        assertThat(inventoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
