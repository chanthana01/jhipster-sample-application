package com.chanctn.jhipster_sample_application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A OrderInfo.
 */
@Table("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("quantity")
    private Integer quantity;

    @NotNull(message = "must not be null")
    @Column("price_per_unit")
    private BigDecimal pricePerUnit;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("modify_at")
    private ZonedDateTime modifyAt;

    private Long promotionInfoId;

    @Transient
    private PromotionInfo promotionInfo;

    private Long productId;

    @Transient
    private Product product;

    @JsonIgnoreProperties(value = { "orderInfos" }, allowSetters = true)
    @Transient
    private Order order;

    @Column("order_id")
    private Long orderId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderInfo id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public OrderInfo quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerUnit() {
        return this.pricePerUnit;
    }

    public OrderInfo pricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit != null ? pricePerUnit.stripTrailingZeros() : null;
        return this;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit != null ? pricePerUnit.stripTrailingZeros() : null;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public OrderInfo createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getModifyAt() {
        return this.modifyAt;
    }

    public OrderInfo modifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
        return this;
    }

    public void setModifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

    public PromotionInfo getPromotionInfo() {
        return this.promotionInfo;
    }

    public OrderInfo promotionInfo(PromotionInfo promotionInfo) {
        this.setPromotionInfo(promotionInfo);
        this.promotionInfoId = promotionInfo != null ? promotionInfo.getId() : null;
        return this;
    }

    public void setPromotionInfo(PromotionInfo promotionInfo) {
        this.promotionInfo = promotionInfo;
        this.promotionInfoId = promotionInfo != null ? promotionInfo.getId() : null;
    }

    public Long getPromotionInfoId() {
        return this.promotionInfoId;
    }

    public void setPromotionInfoId(Long promotionInfo) {
        this.promotionInfoId = promotionInfo;
    }

    public Product getProduct() {
        return this.product;
    }

    public OrderInfo product(Product product) {
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

    public Order getOrder() {
        return this.order;
    }

    public OrderInfo order(Order order) {
        this.setOrder(order);
        this.orderId = order != null ? order.getId() : null;
        return this;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.orderId = order != null ? order.getId() : null;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Long order) {
        this.orderId = order;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderInfo)) {
            return false;
        }
        return id != null && id.equals(((OrderInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderInfo{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", pricePerUnit=" + getPricePerUnit() +
            ", createAt='" + getCreateAt() + "'" +
            ", modifyAt='" + getModifyAt() + "'" +
            "}";
    }
}
