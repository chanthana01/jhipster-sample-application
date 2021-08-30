package com.chanctn.jhipster_sample_application.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Inventory.
 */
@Table("inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull(message = "must not be null")
    @Column("unit")
    private Integer unit;

    @Transient
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inventory id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getUnit() {
        return this.unit;
    }

    public Inventory unit(Integer unit) {
        this.unit = unit;
        return this;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }

    public Product getProduct() {
        return this.product;
    }

    public Inventory product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        if (this.product != null) {
            this.product.setInventory(null);
        }
        if (product != null) {
            product.setInventory(this);
        }
        this.product = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inventory)) {
            return false;
        }
        return id != null && id.equals(((Inventory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            ", unit=" + getUnit() +
            "}";
    }
}
