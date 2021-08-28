package com.chanctn.jhipster_sample_application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.PromotionInfo;
import com.chanctn.jhipster_sample_application.repository.PromotionInfoRepository;
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
 * Integration tests for the {@link PromotionInfoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class PromotionInfoResourceIT {

    private static final String ENTITY_API_URL = "/api/promotion-infos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PromotionInfoRepository promotionInfoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PromotionInfo promotionInfo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromotionInfo createEntity(EntityManager em) {
        PromotionInfo promotionInfo = new PromotionInfo();
        return promotionInfo;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PromotionInfo createUpdatedEntity(EntityManager em) {
        PromotionInfo promotionInfo = new PromotionInfo();
        return promotionInfo;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PromotionInfo.class).block();
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
        promotionInfo = createEntity(em);
    }

    @Test
    void createPromotionInfo() throws Exception {
        int databaseSizeBeforeCreate = promotionInfoRepository.findAll().collectList().block().size();
        // Create the PromotionInfo
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeCreate + 1);
        PromotionInfo testPromotionInfo = promotionInfoList.get(promotionInfoList.size() - 1);
    }

    @Test
    void createPromotionInfoWithExistingId() throws Exception {
        // Create the PromotionInfo with an existing ID
        promotionInfo.setId(1L);

        int databaseSizeBeforeCreate = promotionInfoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPromotionInfos() {
        // Initialize the database
        promotionInfoRepository.save(promotionInfo).block();

        // Get all the promotionInfoList
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
            .value(hasItem(promotionInfo.getId().intValue()));
    }

    @Test
    void getPromotionInfo() {
        // Initialize the database
        promotionInfoRepository.save(promotionInfo).block();

        // Get the promotionInfo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, promotionInfo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(promotionInfo.getId().intValue()));
    }

    @Test
    void getNonExistingPromotionInfo() {
        // Get the promotionInfo
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPromotionInfo() throws Exception {
        // Initialize the database
        promotionInfoRepository.save(promotionInfo).block();

        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();

        // Update the promotionInfo
        PromotionInfo updatedPromotionInfo = promotionInfoRepository.findById(promotionInfo.getId()).block();

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPromotionInfo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPromotionInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
        PromotionInfo testPromotionInfo = promotionInfoList.get(promotionInfoList.size() - 1);
    }

    @Test
    void putNonExistingPromotionInfo() throws Exception {
        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();
        promotionInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, promotionInfo.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPromotionInfo() throws Exception {
        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();
        promotionInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPromotionInfo() throws Exception {
        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();
        promotionInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePromotionInfoWithPatch() throws Exception {
        // Initialize the database
        promotionInfoRepository.save(promotionInfo).block();

        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();

        // Update the promotionInfo using partial update
        PromotionInfo partialUpdatedPromotionInfo = new PromotionInfo();
        partialUpdatedPromotionInfo.setId(promotionInfo.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPromotionInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotionInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
        PromotionInfo testPromotionInfo = promotionInfoList.get(promotionInfoList.size() - 1);
    }

    @Test
    void fullUpdatePromotionInfoWithPatch() throws Exception {
        // Initialize the database
        promotionInfoRepository.save(promotionInfo).block();

        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();

        // Update the promotionInfo using partial update
        PromotionInfo partialUpdatedPromotionInfo = new PromotionInfo();
        partialUpdatedPromotionInfo.setId(promotionInfo.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPromotionInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPromotionInfo))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
        PromotionInfo testPromotionInfo = promotionInfoList.get(promotionInfoList.size() - 1);
    }

    @Test
    void patchNonExistingPromotionInfo() throws Exception {
        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();
        promotionInfo.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, promotionInfo.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPromotionInfo() throws Exception {
        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();
        promotionInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPromotionInfo() throws Exception {
        int databaseSizeBeforeUpdate = promotionInfoRepository.findAll().collectList().block().size();
        promotionInfo.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(promotionInfo))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PromotionInfo in the database
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePromotionInfo() {
        // Initialize the database
        promotionInfoRepository.save(promotionInfo).block();

        int databaseSizeBeforeDelete = promotionInfoRepository.findAll().collectList().block().size();

        // Delete the promotionInfo
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, promotionInfo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PromotionInfo> promotionInfoList = promotionInfoRepository.findAll().collectList().block();
        assertThat(promotionInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
