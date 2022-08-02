package com.payment.service;

import com.payment.service.dto.CuotaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.payment.domain.Cuota}.
 */
public interface CuotaService {
    /**
     * Save a cuota.
     *
     * @param cuotaDTO the entity to save.
     * @return the persisted entity.
     */
    CuotaDTO save(CuotaDTO cuotaDTO);

    /**
     * Updates a cuota.
     *
     * @param cuotaDTO the entity to update.
     * @return the persisted entity.
     */
    CuotaDTO update(CuotaDTO cuotaDTO);

    /**
     * Partially updates a cuota.
     *
     * @param cuotaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CuotaDTO> partialUpdate(CuotaDTO cuotaDTO);

    /**
     * Get all the cuotas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CuotaDTO> findAll(Pageable pageable);

    /**
     * Get the "id" cuota.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CuotaDTO> findOne(Long id);

    /**
     * Delete the "id" cuota.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
