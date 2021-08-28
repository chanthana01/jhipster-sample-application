package com.chanctn.jhipster_sample_application.repository.rowmapper;

import com.chanctn.jhipster_sample_application.domain.OrderInfo;
import com.chanctn.jhipster_sample_application.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link OrderInfo}, with proper type conversions.
 */
@Service
public class OrderInfoRowMapper implements BiFunction<Row, String, OrderInfo> {

    private final ColumnConverter converter;

    public OrderInfoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link OrderInfo} stored in the database.
     */
    @Override
    public OrderInfo apply(Row row, String prefix) {
        OrderInfo entity = new OrderInfo();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setQuantity(converter.fromRow(row, prefix + "_quantity", Integer.class));
        entity.setPricePerUnit(converter.fromRow(row, prefix + "_price_per_unit", BigDecimal.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setModifyAt(converter.fromRow(row, prefix + "_modify_at", ZonedDateTime.class));
        entity.setPromotionInfoId(converter.fromRow(row, prefix + "_promotion_info_id", Long.class));
        entity.setProductId(converter.fromRow(row, prefix + "_product_id", Long.class));
        entity.setOrderId(converter.fromRow(row, prefix + "_order_id", Long.class));
        return entity;
    }
}
