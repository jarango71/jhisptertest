package com.condominium.service;

import com.condominium.service.dto.PagoDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.condominium.domain.Pago}.
 */
public interface PagoService {
    /**
     * Save a pago.
     *
     * @param pagoDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PagoDTO> save(PagoDTO pagoDTO);

    /**
     * Updates a pago.
     *
     * @param pagoDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PagoDTO> update(PagoDTO pagoDTO);

    /**
     * Partially updates a pago.
     *
     * @param pagoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PagoDTO> partialUpdate(PagoDTO pagoDTO);

    /**
     * Get all the pagos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PagoDTO> findAll(Pageable pageable);

    /**
     * Get all the pagos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PagoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of pagos available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" pago.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PagoDTO> findOne(Long id);

    /**
     * Delete the "id" pago.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
