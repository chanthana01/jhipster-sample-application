package com.chanctn.jhipster_sample_application.repository.rowmapper;

import com.chanctn.jhipster_sample_application.domain.Order;
import com.chanctn.jhipster_sample_application.service.ColumnConverter;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Order}, with proper type conversions.
 */
@Service
public class OrderRowMapper implements BiFunction<Row, String, Order> {

    private final ColumnConverter converter;

    public OrderRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Order} stored in the database.
     */
    @Override
    public Order apply(Row row, String prefix) {
        Order entity = new Order();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setCustomerName(converter.fromRow(row, prefix + "_customer_name", String.class));
        entity.setOrderAddress(converter.fromRow(row, prefix + "_order_address", String.class));
        entity.setTotalAmount(converter.fromRow(row, prefix + "_total_amount", BigDecimal.class));
        entity.setOmiseTxnId(converter.fromRow(row, prefix + "_omise_txn_id", String.class));
        entity.setTxnTimeStamp(converter.fromRow(row, prefix + "_txn_time_stamp", ZonedDateTime.class));
        entity.setIsTxnSuccess(converter.fromRow(row, prefix + "_is_txn_success", Boolean.class));
        entity.setCreateAt(converter.fromRow(row, prefix + "_create_at", ZonedDateTime.class));
        entity.setModifyAt(converter.fromRow(row, prefix + "_modify_at", ZonedDateTime.class));
        return entity;
    }
}
