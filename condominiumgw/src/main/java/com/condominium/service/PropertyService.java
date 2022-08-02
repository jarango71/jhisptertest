package com.condominium.service;

import com.condominium.service.dto.PropertyDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.condominium.domain.Property}.
 */
public interface PropertyService {
    /**
     * Save a property.
     *
     * @param propertyDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<PropertyDTO> save(PropertyDTO propertyDTO);

    /**
     * Updates a property.
     *
     * @param propertyDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<PropertyDTO> update(PropertyDTO propertyDTO);

    /**
     * Partially updates a property.
     *
     * @param propertyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<PropertyDTO> partialUpdate(PropertyDTO propertyDTO);

    /**
     * Get all the properties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PropertyDTO> findAll(Pageable pageable);

    /**
     * Get all the properties with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<PropertyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of properties available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" property.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<PropertyDTO> findOne(Long id);

    /**
     * Delete the "id" property.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
