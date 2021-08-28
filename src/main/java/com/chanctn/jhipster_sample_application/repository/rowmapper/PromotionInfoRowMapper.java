package com.chanctn.jhipster_sample_application.repository.rowmapper;

import com.chanctn.jhipster_sample_application.domain.PromotionInfo;
import com.chanctn.jhipster_sample_application.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link PromotionInfo}, with proper type conversions.
 */
@Service
public class PromotionInfoRowMapper implements BiFunction<Row, String, PromotionInfo> {

    private final ColumnConverter converter;

    public PromotionInfoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link PromotionInfo} stored in the database.
     */
    @Override
    public PromotionInfo apply(Row row, String prefix) {
        PromotionInfo entity = new PromotionInfo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPromotionId(converter.fromRow(row, prefix + "_promotion_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        return entity;
    }
}
