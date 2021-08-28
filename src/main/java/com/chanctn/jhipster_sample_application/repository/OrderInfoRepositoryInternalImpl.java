package com.chanctn.jhipster_sample_application.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.chanctn.jhipster_sample_application.domain.OrderInfo;
import com.chanctn.jhipster_sample_application.repository.rowmapper.OrderInfoRowMapper;
import com.chanctn.jhipster_sample_application.repository.rowmapper.OrderRowMapper;
import com.chanctn.jhipster_sample_application.repository.rowmapper.ProductRowMapper;
import com.chanctn.jhipster_sample_application.repository.rowmapper.PromotionInfoRowMapper;
import com.chanctn.jhipster_sample_application.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the OrderInfo entity.
 */
@SuppressWarnings("unused")
class OrderInfoRepositoryInternalImpl implements OrderInfoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PromotionInfoRowMapper promotioninfoMapper;
    private final ProductRowMapper productMapper;
    private final OrderRowMapper orderMapper;
    private final OrderInfoRowMapper orderinfoMapper;

    private static final Table entityTable = Table.aliased("order_info", EntityManager.ENTITY_ALIAS);
    private static final Table promotionInfoTable = Table.aliased("promotion_info", "promotionInfo");
    private static final Table productTable = Table.aliased("product", "product");
    private static final Table orderTable = Table.aliased("jhi_order", "e_order");

    public OrderInfoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PromotionInfoRowMapper promotioninfoMapper,
        ProductRowMapper productMapper,
        OrderRowMapper orderMapper,
        OrderInfoRowMapper orderinfoMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.promotioninfoMapper = promotioninfoMapper;
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.orderinfoMapper = orderinfoMapper;
    }

    @Override
    public Flux<OrderInfo> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<OrderInfo> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<OrderInfo> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = OrderInfoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PromotionInfoSqlHelper.getColumns(promotionInfoTable, "promotionInfo"));
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        columns.addAll(OrderSqlHelper.getColumns(orderTable, "order"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(promotionInfoTable)
            .on(Column.create("promotion_info_id", entityTable))
            .equals(Column.create("id", promotionInfoTable))
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable))
            .leftOuterJoin(orderTable)
            .on(Column.create("order_id", entityTable))
            .equals(Column.create("id", orderTable));

        String select = entityManager.createSelect(selectFrom, OrderInfo.class, pageable, criteria);
        String alias = entityTable.getReferenceName().getReference();
        String selectWhere = Optional
            .ofNullable(criteria)
            .map(
                crit ->
                    new StringBuilder(select)
                        .append(" ")
                        .append("WHERE")
                        .append(" ")
                        .append(alias)
                        .append(".")
                        .append(crit.toString())
                        .toString()
            )
            .orElse(select); // TODO remove once https://github.com/spring-projects/spring-data-jdbc/issues/907 will be fixed
        return db.sql(selectWhere).map(this::process);
    }

    @Override
    public Flux<OrderInfo> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<OrderInfo> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private OrderInfo process(Row row, RowMetadata metadata) {
        OrderInfo entity = orderinfoMapper.apply(row, "e");
        entity.setPromotionInfo(promotioninfoMapper.apply(row, "promotionInfo"));
        entity.setProduct(productMapper.apply(row, "product"));
        entity.setOrder(orderMapper.apply(row, "order"));
        return entity;
    }

    @Override
    public <S extends OrderInfo> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends OrderInfo> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update OrderInfo with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(OrderInfo entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class OrderInfoSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("quantity", table, columnPrefix + "_quantity"));
        columns.add(Column.aliased("price_per_unit", table, columnPrefix + "_price_per_unit"));
        columns.add(Column.aliased("create_at", table, columnPrefix + "_create_at"));
        columns.add(Column.aliased("modify_at", table, columnPrefix + "_modify_at"));

        columns.add(Column.aliased("promotion_info_id", table, columnPrefix + "_promotion_info_id"));
        columns.add(Column.aliased("product_id", table, columnPrefix + "_product_id"));
        columns.add(Column.aliased("order_id", table, columnPrefix + "_order_id"));
        return columns;
    }
}
