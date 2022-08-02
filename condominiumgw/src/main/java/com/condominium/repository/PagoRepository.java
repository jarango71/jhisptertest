package com.condominium.repository;

import com.condominium.domain.Pago;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Pago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagoRepository extends ReactiveCrudRepository<Pago, Long>, PagoRepositoryInternal {
    Flux<Pago> findAllBy(Pageable pageable);

    @Override
    Mono<Pago> findOneWithEagerRelationships(Long id);

    @Override
    Flux<Pago> findAllWithEagerRelationships();

    @Override
    Flux<Pago> findAllWithEagerRelationships(Pageable page);

    @Query("SELECT * FROM pago entity WHERE entity.cuota_id = :id")
    Flux<Pago> findByCuota(Long id);

    @Query("SELECT * FROM pago entity WHERE entity.cuota_id IS NULL")
    Flux<Pago> findAllWhereCuotaIsNull();

    @Override
    <S extends Pago> Mono<S> save(S entity);

    @Override
    Flux<Pago> findAll();

    @Override
    Mono<Pago> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface PagoRepositoryInternal {
    <S extends Pago> Mono<S> save(S entity);

    Flux<Pago> findAllBy(Pageable pageable);

    Flux<Pago> findAll();

    Mono<Pago> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Pago> findAllBy(Pageable pageable, Criteria criteria);

    Mono<Pago> findOneWithEagerRelationships(Long id);

    Flux<Pago> findAllWithEagerRelationships();

    Flux<Pago> findAllWithEagerRelationships(Pageable page);

    Mono<Void> deleteById(Long id);
}
