package com.chanctn.jhipster_sample_application.web.rest;

import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.Promotion;
import com.chanctn.jhipster_sample_application.repository.PromotionRepository;
import com.chanctn.jhipster_sample_application.service.EntityManager;
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
 * Integration tests for the {@link PromotionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class PromotionResourceIT {

    private static final String DEFAULT_PROMOTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PROMOTION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROMORION_FORMULAR = "AAAAAAAAAA";
    private static final String UPDATED_PROMORION_FORMULAR = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_EXPIRE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/promotions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Promotion promotion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotion createEntity(EntityManager em) {
        Promotion promotion = new Promotion()
            .promotionDescription(DEFAULT_PROMOTION_DESCRIPTION)
            .promorionFormular(DEFAULT_PROMORION_FORMULAR)
            .expireAt(DEFAULT_EXPIRE_AT)
            .createAt(DEFAULT_CREATE_AT)
            .modifyAt(DEFAULT_MODIFY_AT);
        return promotion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Promotion createUpdatedEntity(EntityManager em) {
        Promotion promotion = new Promotion()
            .promotionDescription(UPDATED_PROMOTION_DESCRIPTION)
            .promorionFormular(UPDATED_PROMORION_FORMULAR)
            .expireAt(UPDATED_EXPIRE_AT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);
        return promotion;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Promotion.class).block();
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
        promotion = createEntity(em);
    }

    @Test
    void createPromotion() throws Exception {
        int databaseSizeBeforeCreate = promotionRepository.findAll().collectList().block().size();
        // Create the Promotion
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeCreate + 1);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getPromotionDescription()).isEqualTo(DEFAULT_PROMOTION_DESCRIPTION);
        assertThat(testPromotion.getPromorionFormular()).isEqualTo(DEFAULT_PROMORION_FORMULAR);
        assertThat(testPromotion.getExpireAt()).isEqualTo(DEFAULT_EXPIRE_AT);
        assertThat(testPromotion.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testPromotion.getModifyAt()).isEqualTo(DEFAULT_MODIFY_AT);
    }

    @Test
    void createPromotionWithExistingId() throws Exception {
        // Create the Promotion with an existing ID
        promotion.setId(1L);

        int databaseSizeBeforeCreate = promotionRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkPromotionDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().collectList().block().size();
        // set the field null
        promotion.setPromotionDescription(null);

        // Create the Promotion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPromorionFormularIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().collectList().block().size();
        // set the field null
        promotion.setPromorionFormular(null);

        // Create the Promotion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreateAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().collectList().block().size();
        // set the field null
        promotion.setCreateAt(null);

        // Create the Promotion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifyAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = promotionRepository.findAll().collectList().block().size();
        // set the field null
        promotion.setModifyAt(null);

        // Create the Promotion, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPromotions() {
        // Initialize the database
        promotionRepository.save(promotion).block();

        // Get all the promotionList
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
            .value(hasItem(promotion.getId().intValue()))
            .jsonPath("$.[*].promotionDescription")
            .value(hasItem(DEFAULT_PROMOTION_DESCRIPTION))
            .jsonPath("$.[*].promorionFormular")
            .value(hasItem(DEFAULT_PROMORION_FORMULAR))
            .jsonPath("$.[*].expireAt")
            .value(hasItem(sameInstant(DEFAULT_EXPIRE_AT)))
            .jsonPath("$.[*].createAt")
            .value(hasItem(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.[*].modifyAt")
            .value(hasItem(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getPromotion() {
        // Initialize the database
        promotionRepository.save(promotion).block();

        // Get the promotion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, promotion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(promotion.getId().intValue()))
            .jsonPath("$.promotionDescription")
            .value(is(DEFAULT_PROMOTION_DESCRIPTION))
            .jsonPath("$.promorionFormular")
            .value(is(DEFAULT_PROMORION_FORMULAR))
            .jsonPath("$.expireAt")
            .value(is(sameInstant(DEFAULT_EXPIRE_AT)))
            .jsonPath("$.createAt")
            .value(is(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.modifyAt")
            .value(is(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getNonExistingPromotion() {
        // Get the promotion
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPromotion() throws Exception {
        // Initialize the database
        promotionRepository.save(promotion).block();

        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();

        // Update the promotion
        Promotion updatedPromotion = promotionRepository.findById(promotion.getId()).block();
        updatedPromotion
            .promotionDescription(UPDATED_PROMOTION_DESCRIPTION)
            .promorionFormular(UPDATED_PROMORION_FORMULAR)
            .expireAt(UPDATED_EXPIRE_AT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPromotion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPromotion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getPromotionDescription()).isEqualTo(UPDATED_PROMOTION_DESCRIPTION);
        assertThat(testPromotion.getPromorionFormular()).isEqualTo(UPDATED_PROMORION_FORMULAR);
        assertThat(testPromotion.getExpireAt()).isEqualTo(UPDATED_EXPIRE_AT);
        assertThat(testPromotion.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPromotion.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void putNonExistingPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();
        promotion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, promotion.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();
        promotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();
        promotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePromotionWithPatch() throws Exception {
        // Initialize the database
        promotionRepository.save(promotion).block();

        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();

        // Update the promotion using partial update
        Promotion partialUpdatedPromotion = new Promotion();
        partialUpdatedPromotion.setId(promotion.getId());

        partialUpdatedPromotion
            .promorionFormular(UPDATED_PROMORION_FORMULAR)
            .expireAt(UPDATED_EXPIRE_AT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPromotion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getPromotionDescription()).isEqualTo(DEFAULT_PROMOTION_DESCRIPTION);
        assertThat(testPromotion.getPromorionFormular()).isEqualTo(UPDATED_PROMORION_FORMULAR);
        assertThat(testPromotion.getExpireAt()).isEqualTo(UPDATED_EXPIRE_AT);
        assertThat(testPromotion.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPromotion.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void fullUpdatePromotionWithPatch() throws Exception {
        // Initialize the database
        promotionRepository.save(promotion).block();

        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();

        // Update the promotion using partial update
        Promotion partialUpdatedPromotion = new Promotion();
        partialUpdatedPromotion.setId(promotion.getId());

        partialUpdatedPromotion
            .promotionDescription(UPDATED_PROMOTION_DESCRIPTION)
            .promorionFormular(UPDATED_PROMORION_FORMULAR)
            .expireAt(UPDATED_EXPIRE_AT)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPromotion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotion))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
        Promotion testPromotion = promotionList.get(promotionList.size() - 1);
        assertThat(testPromotion.getPromotionDescription()).isEqualTo(UPDATED_PROMOTION_DESCRIPTION);
        assertThat(testPromotion.getPromorionFormular()).isEqualTo(UPDATED_PROMORION_FORMULAR);
        assertThat(testPromotion.getExpireAt()).isEqualTo(UPDATED_EXPIRE_AT);
        assertThat(testPromotion.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testPromotion.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void patchNonExistingPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();
        promotion.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, promotion.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();
        promotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPromotion() throws Exception {
        int databaseSizeBeforeUpdate = promotionRepository.findAll().collectList().block().size();
        promotion.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotion))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Promotion in the database
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePromotion() {
        // Initialize the database
        promotionRepository.save(promotion).block();

        int databaseSizeBeforeDelete = promotionRepository.findAll().collectList().block().size();

        // Delete the promotion
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, promotion.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Promotion> promotionList = promotionRepository.findAll().collectList().block();
        assertThat(promotionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
