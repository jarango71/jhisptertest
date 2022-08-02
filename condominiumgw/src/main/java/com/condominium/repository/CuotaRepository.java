package com.condominium.repository;

import com.condominium.domain.Cuota;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Cuota entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuotaRepository extends ReactiveCrudRepository<Cuota, Long>, CuotaRepositoryInternal {
    Flux<Cuota> findAllBy(Pageable pageable);

    @Override
    <S extends Cuota> Mono<S> save(S entity);

    @Override
    Flux<Cuota> findAll();

    @Override
    Mono<Cuota> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CuotaRepositoryInternal {
    <S extends Cuota> Mono<S> save(S entity);

    Flux<Cuota> findAllBy(Pageable pageable);

    Flux<Cuota> findAll();

    Mono<Cuota> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Cuota> findAllBy(Pageable pageable, Criteria criteria);

}
