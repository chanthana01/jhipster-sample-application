package com.chanctn.jhipster_sample_application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
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
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Table("jhi_order")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("customer_name")
    private String customerName;

    @NotNull(message = "must not be null")
    @Column("order_address")
    private String orderAddress;

    @NotNull(message = "must not be null")
    @Column("total_amount")
    private BigDecimal totalAmount;

    @NotNull(message = "must not be null")
    @Column("omise_txn_id")
    private String omiseTxnId;

    @NotNull(message = "must not be null")
    @Column("txn_time_stamp")
    private ZonedDateTime txnTimeStamp;

    @NotNull(message = "must not be null")
    @Column("is_txn_success")
    private Boolean isTxnSuccess;

    @NotNull(message = "must not be null")
    @Column("create_at")
    private ZonedDateTime createAt;

    @NotNull(message = "must not be null")
    @Column("modify_at")
    private ZonedDateTime modifyAt;

    @Transient
    @JsonIgnoreProperties(value = { "promotionInfo", "product", "order" }, allowSetters = true)
    private Set<OrderInfo> orderInfos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order id(Long id) {
        this.id = id;
        return this;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public Order customerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderAddress() {
        return this.orderAddress;
    }

    public Order orderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
        return this;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public Order totalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount != null ? totalAmount.stripTrailingZeros() : null;
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount != null ? totalAmount.stripTrailingZeros() : null;
    }

    public String getOmiseTxnId() {
        return this.omiseTxnId;
    }

    public Order omiseTxnId(String omiseTxnId) {
        this.omiseTxnId = omiseTxnId;
        return this;
    }

    public void setOmiseTxnId(String omiseTxnId) {
        this.omiseTxnId = omiseTxnId;
    }

    public ZonedDateTime getTxnTimeStamp() {
        return this.txnTimeStamp;
    }

    public Order txnTimeStamp(ZonedDateTime txnTimeStamp) {
        this.txnTimeStamp = txnTimeStamp;
        return this;
    }

    public void setTxnTimeStamp(ZonedDateTime txnTimeStamp) {
        this.txnTimeStamp = txnTimeStamp;
    }

    public Boolean getIsTxnSuccess() {
        return this.isTxnSuccess;
    }

    public Order isTxnSuccess(Boolean isTxnSuccess) {
        this.isTxnSuccess = isTxnSuccess;
        return this;
    }

    public void setIsTxnSuccess(Boolean isTxnSuccess) {
        this.isTxnSuccess = isTxnSuccess;
    }

    public ZonedDateTime getCreateAt() {
        return this.createAt;
    }

    public Order createAt(ZonedDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public void setCreateAt(ZonedDateTime createAt) {
        this.createAt = createAt;
    }

    public ZonedDateTime getModifyAt() {
        return this.modifyAt;
    }

    public Order modifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
        return this;
    }

    public void setModifyAt(ZonedDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }

    public Set<OrderInfo> getOrderInfos() {
        return this.orderInfos;
    }

    public Order orderInfos(Set<OrderInfo> orderInfos) {
        this.setOrderInfos(orderInfos);
        return this;
    }

    public Order addOrderInfo(OrderInfo orderInfo) {
        this.orderInfos.add(orderInfo);
        orderInfo.setOrder(this);
        return this;
    }

    public Order removeOrderInfo(OrderInfo orderInfo) {
        this.orderInfos.remove(orderInfo);
        orderInfo.setOrder(null);
        return this;
    }

    public void setOrderInfos(Set<OrderInfo> orderInfos) {
        if (this.orderInfos != null) {
            this.orderInfos.forEach(i -> i.setOrder(null));
        }
        if (orderInfos != null) {
            orderInfos.forEach(i -> i.setOrder(this));
        }
        this.orderInfos = orderInfos;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", customerName='" + getCustomerName() + "'" +
            ", orderAddress='" + getOrderAddress() + "'" +
            ", totalAmount=" + getTotalAmount() +
            ", omiseTxnId='" + getOmiseTxnId() + "'" +
            ", txnTimeStamp='" + getTxnTimeStamp() + "'" +
            ", isTxnSuccess='" + getIsTxnSuccess() + "'" +
            ", createAt='" + getCreateAt() + "'" +
            ", modifyAt='" + getModifyAt() + "'" +
            "}";
    }
}
