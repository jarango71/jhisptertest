package com.condominium.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.condominium.domain.Pago;
import com.condominium.domain.enumeration.RegisterState;
import com.condominium.repository.rowmapper.CuotaRowMapper;
import com.condominium.repository.rowmapper.PagoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive custom repository implementation for the Pago entity.
 */
@SuppressWarnings("unused")
class PagoRepositoryInternalImpl extends SimpleR2dbcRepository<Pago, Long> implements PagoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CuotaRowMapper cuotaMapper;
    private final PagoRowMapper pagoMapper;

    private static final Table entityTable = Table.aliased("pago", EntityManager.ENTITY_ALIAS);
    private static final Table cuotaTable = Table.aliased("cuota", "cuota");

    public PagoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CuotaRowMapper cuotaMapper,
        PagoRowMapper pagoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Pago.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.cuotaMapper = cuotaMapper;
        this.pagoMapper = pagoMapper;
    }

    @Override
    public Flux<Pago> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Pago> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PagoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CuotaSqlHelper.getColumns(cuotaTable, "cuota"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(cuotaTable)
            .on(Column.create("cuota_id", entityTable))
            .equals(Column.create("id", cuotaTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Pago.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Pago> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Pago> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Pago> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Pago> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Pago> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Pago process(Row row, RowMetadata metadata) {
        Pago entity = pagoMapper.apply(row, "e");
        entity.setCuota(cuotaMapper.apply(row, "cuota"));
        return entity;
    }

    @Override
    public <S extends Pago> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
