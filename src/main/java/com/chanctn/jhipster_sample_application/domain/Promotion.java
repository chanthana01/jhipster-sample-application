package com.chanctn.jhipster_sample_application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Promotion.
 */
@Table("promotion")
public class Promotion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("promotion_description")
    private String promotionDescription;

    @NotNull(message = "must not be null")
    @Column("promorion_formular")
    private String promorionFormular;

    @Column("expire_at")
    private ZonedDateTime expireAt;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("modify_at")
    private ZonedDateTime modifyAt;

    @Transient
    @JsonIgnoreProperties(value = { "promotion", "product" }, allowSetters = true)
    private Set<PromotionInfo> promotionInfos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Promotion id(Long id) {
        this.id = id;
        return this;
    }

    public String getPromotionDescription() {
        return this.promotionDescription;
    }

    public Promotion promotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
        return this;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }

    public String getPromorionFormular() {
        return this.promorionFormular;
    }

    public Promotion promorionFormular(String promorionFormular) {
        this.promorionFormular = promorionFormular;
        return this;
    }

    public void setPromorionFormular(String promorionFormular) {
        this.promorionFormular = promorionFormular;
    }

    public ZonedDateTime getExpireAt() {
        return this.expireAt;
    }

    public Promotion expireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
        return this;
    }

    public void setExpireAt(ZonedDateTime expireAt) {
        this.expireAt = expireAt;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public Promotion createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getModifyAt() {
        return this.modifyAt;
    }

    public Promotion modifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
        return this;
    }

    public void setModifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Set<PromotionInfo> getPromotionInfos() {
        return this.promotionInfos;
    }

    public Promotion promotionInfos(Set<PromotionInfo> promotionInfos) {
        this.setPromotionInfos(promotionInfos);
        return this;
    }

    public Promotion addPromotionInfo(PromotionInfo promotionInfo) {
        this.promotionInfos.add(promotionInfo);
        promotionInfo.setPromotion(this);
        return this;
    }

    public Promotion removePromotionInfo(PromotionInfo promotionInfo) {
        this.promotionInfos.remove(promotionInfo);
        promotionInfo.setPromotion(null);
        return this;
    }

    public void setPromotionInfos(Set<PromotionInfo> promotionInfos) {
        if (this.promotionInfos != null) {
            this.promotionInfos.forEach(i -> i.setPromotion(null));
        }
        if (promotionInfos != null) {
            promotionInfos.forEach(i -> i.setPromotion(this));
        }
        this.promotionInfos = promotionInfos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promotion)) {
            return false;
        }
        return id != null && id.equals(((Promotion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promotion{" +
            "id=" + getId() +
            ", promotionDescription='" + getPromotionDescription() + "'" +
            ", promorionFormular='" + getPromorionFormular() + "'" +
            ", expireAt='" + getExpireAt() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", modifyAt='" + getModifyAt() + "'" +
            "}";
    }
}
