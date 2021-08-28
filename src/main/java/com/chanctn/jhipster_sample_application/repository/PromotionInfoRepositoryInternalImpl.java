package com.chanctn.jhipster_sample_application.repository;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;

import com.chanctn.jhipster_sample_application.domain.PromotionInfo;
import com.chanctn.jhipster_sample_application.repository.rowmapper.ProductRowMapper;
import com.chanctn.jhipster_sample_application.repository.rowmapper.PromotionInfoRowMapper;
import com.chanctn.jhipster_sample_application.repository.rowmapper.PromotionRowMapper;
import com.chanctn.jhipster_sample_application.service.EntityManager;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
 * Spring Data SQL reactive custom repository implementation for the PromotionInfo entity.
 */
@SuppressWarnings("unused")
class PromotionInfoRepositoryInternalImpl implements PromotionInfoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final PromotionRowMapper promotionMapper;
    private final ProductRowMapper productMapper;
    private final PromotionInfoRowMapper promotioninfoMapper;

    private static final Table entityTable = Table.aliased("promotion_info", EntityManager.ENTITY_ALIAS);
    private static final Table promotionTable = Table.aliased("promotion", "promotion");
    private static final Table productTable = Table.aliased("product", "product");

    public PromotionInfoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        PromotionRowMapper promotionMapper,
        ProductRowMapper productMapper,
        PromotionInfoRowMapper promotioninfoMapper
    ) {
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.promotionMapper = promotionMapper;
        this.productMapper = productMapper;
        this.promotioninfoMapper = promotioninfoMapper;
    }

    @Override
    public Flux<PromotionInfo> findAllBy(Pageable pageable) {
        return findAllBy(pageable, null);
    }

    @Override
    public Flux<PromotionInfo> findAllBy(Pageable pageable, Criteria criteria) {
        return createQuery(pageable, criteria).all();
    }

    RowsFetchSpec<PromotionInfo> createQuery(Pageable pageable, Criteria criteria) {
        List<Expression> columns = PromotionInfoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(PromotionSqlHelper.getColumns(promotionTable, "promotion"));
        columns.addAll(ProductSqlHelper.getColumns(productTable, "product"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(promotionTable)
            .on(Column.create("promotion_id", entityTable))
            .equals(Column.create("id", promotionTable))
            .leftOuterJoin(productTable)
            .on(Column.create("product_id", entityTable))
            .equals(Column.create("id", productTable));

        String select = entityManager.createSelect(selectFrom, PromotionInfo.class, pageable, criteria);
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
    public Flux<PromotionInfo> findAll() {
        return findAllBy(null, null);
    }

    @Override
    public Mono<PromotionInfo> findById(Long id) {
        return createQuery(null, where("id").is(id)).one();
    }

    private PromotionInfo process(Row row, RowMetadata metadata) {
        PromotionInfo entity = promotioninfoMapper.apply(row, "e");
        entity.setPromotion(promotionMapper.apply(row, "promotion"));
        entity.setProduct(productMapper.apply(row, "product"));
        return entity;
    }

    @Override
    public <S extends PromotionInfo> Mono<S> insert(S entity) {
        return entityManager.insert(entity);
    }

    @Override
    public <S extends PromotionInfo> Mono<S> save(S entity) {
        if (entity.getId() == null) {
            return insert(entity);
        } else {
            return update(entity)
                .map(
                    numberOfUpdates -> {
                        if (numberOfUpdates.intValue() <= 0) {
                            throw new IllegalStateException("Unable to update PromotionInfo with id = " + entity.getId());
                        }
                        return entity;
                    }
                );
        }
    }

    @Override
    public Mono<Integer> update(PromotionInfo entity) {
        //fixme is this the proper way?
        return r2dbcEntityTemplate.update(entity).thenReturn(1);
    }
}

class PromotionInfoSqlHelper {

    static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));

        columns.add(Column.aliased("promotion_id", table, columnPrefix + "_promotion_id"));
        columns.add(Column.aliased("product_id", table, columnPrefix + "_product_id"));
        return columns;
    }
}
