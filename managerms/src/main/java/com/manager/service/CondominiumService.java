package com.manager.service;

import com.manager.service.dto.CondominiumDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.manager.domain.Condominium}.
 */
public interface CondominiumService {
    /**
     * Save a condominium.
     *
     * @param condominiumDTO the entity to save.
     * @return the persisted entity.
     */
    CondominiumDTO save(CondominiumDTO condominiumDTO);

    /**
     * Updates a condominium.
     *
     * @param condominiumDTO the entity to update.
     * @return the persisted entity.
     */
    CondominiumDTO update(CondominiumDTO condominiumDTO);

    /**
     * Partially updates a condominium.
     *
     * @param condominiumDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CondominiumDTO> partialUpdate(CondominiumDTO condominiumDTO);

    /**
     * Get all the condominiums.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CondominiumDTO> findAll(Pageable pageable);

    /**
     * Get the "id" condominium.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CondominiumDTO> findOne(Long id);

    /**
     * Delete the "id" condominium.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
