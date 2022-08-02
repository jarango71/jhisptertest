package com.condominium.service;

import com.condominium.service.dto.CuotaDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.condominium.domain.Cuota}.
 */
public interface CuotaService {
    /**
     * Save a cuota.
     *
     * @param cuotaDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<CuotaDTO> save(CuotaDTO cuotaDTO);

    /**
     * Updates a cuota.
     *
     * @param cuotaDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<CuotaDTO> update(CuotaDTO cuotaDTO);

    /**
     * Partially updates a cuota.
     *
     * @param cuotaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<CuotaDTO> partialUpdate(CuotaDTO cuotaDTO);

    /**
     * Get all the cuotas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<CuotaDTO> findAll(Pageable pageable);

    /**
     * Returns the number of cuotas available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" cuota.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<CuotaDTO> findOne(Long id);

    /**
     * Delete the "id" cuota.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
