package com.chanctn.jhipster_sample_application.repository.rowmapper;

import com.chanctn.jhipster_sample_application.domain.Promotion;
import com.chanctn.jhipster_sample_application.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Promotion}, with proper type conversions.
 */
@Service
public class PromotionRowMapper implements BiFunction<Row, String, Promotion> {

    private final ColumnConverter converter;

    public PromotionRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Promotion} stored in the database.
     */
    @Override
    public Promotion apply(Row row, String prefix) {
        Promotion entity = new Promotion();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setPromotionDescription(converter.fromRow(row, prefix + "_promotion_description", String.class));
        entity.setPromorionFormular(converter.fromRow(row, prefix + "_promorion_formular", String.class));
        entity.setExpireAt(converter.fromRow(row, prefix + "_expire_at", ZonedDateTime.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setModifyAt(converter.fromRow(row, prefix + "_modify_at", ZonedDateTime.class));
        return entity;
    }
}
