package com.chanctn.jhipster_sample_application.repository.rowmapper;

import com.chanctn.jhipster_sample_application.domain.Product;
import com.chanctn.jhipster_sample_application.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Product}, with proper type conversions.
 */
@Service
public class ProductRowMapper implements BiFunction<Row, String, Product> {

    private final ColumnConverter converter;

    public ProductRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Product} stored in the database.
     */
    @Override
    public Product apply(Row row, String prefix) {
        Product entity = new Product();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCode(converter.fromRow(row, prefix + "_code", String.class));
        entity.setCategory(converter.fromRow(row, prefix + "_category", String.class));
        entity.setName(converter.fromRow(row, prefix + "_name", String.class));
        entity.setFamily(converter.fromRow(row, prefix + "_family", String.class));
        entity.setDetail1(converter.fromRow(row, prefix + "_detail_1", String.class));
        entity.setDetail2(converter.fromRow(row, prefix + "_detail_2", String.class));
        entity.setPrice(converter.fromRow(row, prefix + "_price", BigDecimal.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setModifyAt(converter.fromRow(row, prefix + "_modify_at", ZonedDateTime.class));
        entity.setInventoryId(converter.fromRow(row, prefix + "_inventory_id", Long.class));
        return entity;
    }
}
