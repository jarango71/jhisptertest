package com.condominium.repository;

import com.condominium.domain.Property;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Property entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PropertyRepository extends ReactiveCrudRepository<Property, Long>, PropertyRepositoryInternal {
    Flux<Property> findAllBy(Pageable pageable);

    @Override
    Mono<Property> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Property> findAllWithEagerRelationships();

    @Override
    Flux<Property> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM property entity WHERE entity.condominium_id = :id")
    Flux<Property> findByCondominium(Long id);

    @Query("SELECT * FROM property entity WHERE entity.condominium_id IS NULL")
    Flux<Property> findAllWhereCondominiumIsNull();

    @Override
    <S extends Property> Mono<S> save(S entity);

    @Override
    Flux<Property> findAll();

    @Override
    Mono<Property> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PropertyRepositoryInternal {
    <S extends Property> Mono<S> save(S entity);

    Flux<Property> findAllBy(Pageable pageable);

    Flux<Property> findAll();

    Mono<Property> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Property> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Property> findOneWithEagerRelationships(Long id);

    Flux<Property> findAllWithEagerRelationships();

    Flux<Property> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
