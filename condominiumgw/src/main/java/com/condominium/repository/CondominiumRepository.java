package com.condominium.repository;

import com.condominium.domain.Condominium;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data SQL reactive repository for the Condominium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CondominiumRepository extends ReactiveCrudRepository<Condominium, Long>, CondominiumRepositoryInternal {
    Flux<Condominium> findAllBy(Pageable pageable);

    @Override
    <S extends Condominium> Mono<S> save(S entity);

    @Override
    Flux<Condominium> findAll();

    @Override
    Mono<Condominium> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CondominiumRepositoryInternal {
    <S extends Condominium> Mono<S> save(S entity);

    Flux<Condominium> findAllBy(Pageable pageable);

    Flux<Condominium> findAll();

    Mono<Condominium> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Condominium> findAllBy(Pageable pageable, Criteria criteria);

}
