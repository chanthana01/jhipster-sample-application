package com.chanctn.jhipster_sample_application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Product.
 */
@Table("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("code")
    private String code;

    @NotNull(message = "must not be null")
    @Column("category")
    private String category;

    @NotNull(message = "must not be null")
    @Column("name")
    private String name;

    @NotNull(message = "must not be null")
    @Column("family")
    private String family;

    @Column("detail_1")
    private String detail1;

    @Column("detail_2")
    private String detail2;

    @NotNull(message = "must not be null")
    @Column("price")
    private BigDecimal price;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("modify_at")
    private ZonedDateTime modifyAt;

    private Long inventoryId;

    @Transient
    private Inventory inventory;

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

    public Product id(Long id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return this.code;
    }

    public Product code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return this.category;
    }

    public Product category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return this.family;
    }

    public Product family(String family) {
        this.family = family;
        return this;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getDetail1() {
        return this.detail1;
    }

    public Product detail1(String detail1) {
        this.detail1 = detail1;
        return this;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public String getDetail2() {
        return this.detail2;
    }

    public Product detail2(String detail2) {
        this.detail2 = detail2;
        return this;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Product price(BigDecimal price) {
        this.price = price != null ? price.stripTrailingZeros() : null;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price != null ? price.stripTrailingZeros() : null;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public Product createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getModifyAt() {
        return this.modifyAt;
    }

    public Product modifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
        return this;
    }

    public void setModifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public Product inventory(Inventory inventory) {
        this.setInventory(inventory);
        this.inventoryId = inventory != null ? inventory.getId() : null;
        return this;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.inventoryId = inventory != null ? inventory.getId() : null;
    }

    public Long getInventoryId() {
        return this.inventoryId;
    }

    public void setInventoryId(Long inventory) {
        this.inventoryId = inventory;
    }

    public Set<PromotionInfo> getPromotionInfos() {
        return this.promotionInfos;
    }

    public Product promotionInfos(Set<PromotionInfo> promotionInfos) {
        this.setPromotionInfos(promotionInfos);
        return this;
    }

    public Product addPromotionInfo(PromotionInfo promotionInfo) {
        this.promotionInfos.add(promotionInfo);
        promotionInfo.setProduct(this);
        return this;
    }

    public Product removePromotionInfo(PromotionInfo promotionInfo) {
        this.promotionInfos.remove(promotionInfo);
        promotionInfo.setProduct(null);
        return this;
    }

    public void setPromotionInfos(Set<PromotionInfo> promotionInfos) {
        if (this.promotionInfos != null) {
            this.promotionInfos.forEach(i -> i.setProduct(null));
        }
        if (promotionInfos != null) {
            promotionInfos.forEach(i -> i.setProduct(this));
        }
        this.promotionInfos = promotionInfos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", category='" + getCategory() + "'" +
            ", name='" + getName() + "'" +
            ", family='" + getFamily() + "'" +
            ", detail1='" + getDetail1() + "'" +
            ", detail2='" + getDetail2() + "'" +
            ", price=" + getPrice() +
            ", createAt='" + getCreateAt() + "'" +
            ", modifyAt='" + getModifyAt() + "'" +
            "}";
    }
}
