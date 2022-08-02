package com.condominium.service;

import com.condominium.service.dto.CondominiumDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.condominium.domain.Condominium}.
 */
public interface CondominiumService {
    /**
     * Save a condominium.
     *
     * @param condominiumDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CondominiumDTO> save(CondominiumDTO condominiumDTO);

    /**
     * Updates a condominium.
     *
     * @param condominiumDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CondominiumDTO> update(CondominiumDTO condominiumDTO);

    /**
     * Partially updates a condominium.
     *
     * @param condominiumDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CondominiumDTO> partialUpdate(CondominiumDTO condominiumDTO);

    /**
     * Get all the condominiums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CondominiumDTO> findAll(Pageable pageable);

    /**
     * Returns the number of condominiums available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" condominium.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CondominiumDTO> findOne(Long id);

    /**
     * Delete the "id" condominium.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
