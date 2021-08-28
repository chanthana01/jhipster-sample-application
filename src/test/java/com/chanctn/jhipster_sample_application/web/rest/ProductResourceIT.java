package com.chanctn.jhipster_sample_application.web.rest;

import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameInstant;
import static com.chanctn.jhipster_sample_application.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.chanctn.jhipster_sample_application.IntegrationTest;
import com.chanctn.jhipster_sample_application.domain.Product;
import com.chanctn.jhipster_sample_application.repository.ProductRepository;
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
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FAMILY = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL_1 = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL_2 = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL_2 = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_CREATE_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFY_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFY_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .code(DEFAULT_CODE)
            .category(DEFAULT_CATEGORY)
            .name(DEFAULT_NAME)
            .family(DEFAULT_FAMILY)
            .detail1(DEFAULT_DETAIL_1)
            .detail2(DEFAULT_DETAIL_2)
            .price(DEFAULT_PRICE)
            .createAt(DEFAULT_CREATE_AT)
            .modifyAt(DEFAULT_MODIFY_AT);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .code(UPDATED_CODE)
            .category(UPDATED_CATEGORY)
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .detail1(UPDATED_DETAIL_1)
            .detail2(UPDATED_DETAIL_2)
            .price(UPDATED_PRICE)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);
        return product;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Product.class).block();
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
        product = createEntity(em);
    }

    @Test
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().collectList().block().size();
        // Create the Product
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testProduct.getDetail1()).isEqualTo(DEFAULT_DETAIL_1);
        assertThat(testProduct.getDetail2()).isEqualTo(DEFAULT_DETAIL_2);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testProduct.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testProduct.getModifyAt()).isEqualTo(DEFAULT_MODIFY_AT);
    }

    @Test
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);

        int databaseSizeBeforeCreate = productRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setCode(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setCategory(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFamilyIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setFamily(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCreateAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setCreateAt(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkModifyAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().collectList().block().size();
        // set the field null
        product.setModifyAt(null);

        // Create the Product, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProducts() {
        // Initialize the database
        productRepository.save(product).block();

        // Get all the productList
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
            .value(hasItem(product.getId().intValue()))
            .jsonPath("$.[*].code")
            .value(hasItem(DEFAULT_CODE))
            .jsonPath("$.[*].category")
            .value(hasItem(DEFAULT_CATEGORY))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].family")
            .value(hasItem(DEFAULT_FAMILY))
            .jsonPath("$.[*].detail1")
            .value(hasItem(DEFAULT_DETAIL_1))
            .jsonPath("$.[*].detail2")
            .value(hasItem(DEFAULT_DETAIL_2))
            .jsonPath("$.[*].price")
            .value(hasItem(sameNumber(DEFAULT_PRICE)))
            .jsonPath("$.[*].createAt")
            .value(hasItem(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.[*].modifyAt")
            .value(hasItem(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getProduct() {
        // Initialize the database
        productRepository.save(product).block();

        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(product.getId().intValue()))
            .jsonPath("$.code")
            .value(is(DEFAULT_CODE))
            .jsonPath("$.category")
            .value(is(DEFAULT_CATEGORY))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.family")
            .value(is(DEFAULT_FAMILY))
            .jsonPath("$.detail1")
            .value(is(DEFAULT_DETAIL_1))
            .jsonPath("$.detail2")
            .value(is(DEFAULT_DETAIL_2))
            .jsonPath("$.price")
            .value(is(sameNumber(DEFAULT_PRICE)))
            .jsonPath("$.createAt")
            .value(is(sameInstant(DEFAULT_CREATE_AT)))
            .jsonPath("$.modifyAt")
            .value(is(sameInstant(DEFAULT_MODIFY_AT)));
    }

    @Test
    void getNonExistingProduct() {
        // Get the product
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).block();
        updatedProduct
            .code(UPDATED_CODE)
            .category(UPDATED_CATEGORY)
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .detail1(UPDATED_DETAIL_1)
            .detail2(UPDATED_DETAIL_2)
            .price(UPDATED_PRICE)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedProduct.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testProduct.getDetail1()).isEqualTo(UPDATED_DETAIL_1);
        assertThat(testProduct.getDetail2()).isEqualTo(UPDATED_DETAIL_2);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testProduct.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, product.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.detail1(UPDATED_DETAIL_1).detail2(UPDATED_DETAIL_2);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testProduct.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testProduct.getDetail1()).isEqualTo(UPDATED_DETAIL_1);
        assertThat(testProduct.getDetail2()).isEqualTo(UPDATED_DETAIL_2);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testProduct.getCreateAt()).isEqualTo(DEFAULT_CREATE_AT);
        assertThat(testProduct.getModifyAt()).isEqualTo(DEFAULT_MODIFY_AT);
    }

    @Test
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .code(UPDATED_CODE)
            .category(UPDATED_CATEGORY)
            .name(UPDATED_NAME)
            .family(UPDATED_FAMILY)
            .detail1(UPDATED_DETAIL_1)
            .detail2(UPDATED_DETAIL_2)
            .price(UPDATED_PRICE)
            .createAt(UPDATED_CREATE_AT)
            .modifyAt(UPDATED_MODIFY_AT);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testProduct.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testProduct.getDetail1()).isEqualTo(UPDATED_DETAIL_1);
        assertThat(testProduct.getDetail2()).isEqualTo(UPDATED_DETAIL_2);
        assertThat(testProduct.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testProduct.getCreateAt()).isEqualTo(UPDATED_CREATE_AT);
        assertThat(testProduct.getModifyAt()).isEqualTo(UPDATED_MODIFY_AT);
    }

    @Test
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, product.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().collectList().block().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(product))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProduct() {
        // Initialize the database
        productRepository.save(product).block();

        int databaseSizeBeforeDelete = productRepository.findAll().collectList().block().size();

        // Delete the product
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, product.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll().collectList().block();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
