package com.condominium.repository;

import static org.springframework.data.relational.core.query.Criteria.where;

import com.condominium.domain.Property;
import com.condominium.domain.enumeration.RegisterState;
import com.condominium.repository.rowmapper.CondominiumRowMapper;
import com.condominium.repository.rowmapper.PropertyRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
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
 * Spring Data SQL reactive custom repository implementation for the Property entity.
 */
@SuppressWarnings("unused")
class PropertyRepositoryInternalImpl extends SimpleR2dbcRepository<Property, Long> implements PropertyRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CondominiumRowMapper condominiumMapper;
    private final PropertyRowMapper propertyMapper;

    private static final Table entityTable = Table.aliased("property", EntityManager.ENTITY_ALIAS);
    private static final Table condominiumTable = Table.aliased("condominium", "condominium");

    public PropertyRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CondominiumRowMapper condominiumMapper,
        PropertyRowMapper propertyMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Property.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.condominiumMapper = condominiumMapper;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public Flux<Property> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Property> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = PropertySqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CondominiumSqlHelper.getColumns(condominiumTable, "condominium"));
        SelectFromAndJoinCondition selectFrom = Select
            .builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(condominiumTable)
            .on(Column.create("condominium_id", entityTable))
            .equals(Column.create("id", condominiumTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Property.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Property> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Property> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<Property> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<Property> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<Property> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private Property process(Row row, RowMetadata metadata) {
        Property entity = propertyMapper.apply(row, "e");
        entity.setCondominium(condominiumMapper.apply(row, "condominium"));
        return entity;
    }

    @Override
    public <S extends Property> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
