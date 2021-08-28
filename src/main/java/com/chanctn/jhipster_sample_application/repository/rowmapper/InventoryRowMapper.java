package com.chanctn.jhipster_sample_application.repository.rowmapper;

import com.chanctn.jhipster_sample_application.domain.Inventory;
import com.chanctn.jhipster_sample_application.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Inventory}, with proper type conversions.
 */
@Service
public class InventoryRowMapper implements BiFunction<Row, String, Inventory> {

    private final ColumnConverter converter;

    public InventoryRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Inventory} stored in the database.
     */
    @Override
    public Inventory apply(Row row, String prefix) {
        Inventory entity = new Inventory();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setUnit(converter.fromRow(row, prefix + "_unit", Integer.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}
