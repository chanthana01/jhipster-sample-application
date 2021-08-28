package com.chanctn.jhipster_sample_application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A PromotionInfo.
 */
@Table("promotion_info")
public class PromotionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @JsonIgnoreProperties(value = { "promotionInfos" }, allowSetters = true)
    @Transient
    private Promotion promotion;

    @Column("promotion_id")
    private Long promotionId;

    @JsonIgnoreProperties(value = { "inventory", "promotionInfos" }, allowSetters = true)
    @Transient
    private Product product;

    @Column("product_id")
    private Long productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PromotionInfo id(Long id) {
        this.id = id;
        return this;
    }

    public Promotion getPromotion() {
        return this.promotion;
    }

    public PromotionInfo promotion(Promotion promotion) {
        this.setPromotion(promotion);
        this.promotionId = promotion != null ? promotion.getId() : null;
        return this;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
        this.promotionId = promotion != null ? promotion.getId() : null;
    }

    public Long getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(Long promotion) {
        this.promotionId = promotion;
    }

    public Product getProduct() {
        return this.product;
    }

    public PromotionInfo product(Product product) {
        this.setProduct(product);
        this.productId = product != null ? product.getId() : null;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productId = product != null ? product.getId() : null;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long product) {
        this.productId = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromotionInfo)) {
            return false;
        }
        return id != null && id.equals(((PromotionInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PromotionInfo{" +
            "id=" + getId() +
            "}";
    }
}
