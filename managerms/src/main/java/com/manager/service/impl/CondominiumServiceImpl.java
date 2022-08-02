package com.manager.service.impl;

import com.manager.domain.Condominium;
import com.manager.repository.CondominiumRepository;
import com.manager.service.CondominiumService;
import com.manager.service.dto.CondominiumDTO;
import com.manager.service.mapper.CondominiumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Condominium}.
 */
@Service
@Transactional
public class CondominiumServiceImpl implements CondominiumService {

    private final Logger log = LoggerFactory.getLogger(CondominiumServiceImpl.class);

    private final CondominiumRepository condominiumRepository;

    private final CondominiumMapper condominiumMapper;

    public CondominiumServiceImpl(CondominiumRepository condominiumRepository, CondominiumMapper condominiumMapper) {
        this.condominiumRepository = condominiumRepository;
        this.condominiumMapper = condominiumMapper;
    }

    @Override
    public CondominiumDTO save(CondominiumDTO condominiumDTO) {
        log.debug("Request to save Condominium : {}", condominiumDTO);
        Condominium condominium = condominiumMapper.toEntity(condominiumDTO);
        condominium = condominiumRepository.save(condominium);
        return condominiumMapper.toDto(condominium);
    }

    @Override
    public CondominiumDTO update(CondominiumDTO condominiumDTO) {
        log.debug("Request to save Condominium : {}", condominiumDTO);
        Condominium condominium = condominiumMapper.toEntity(condominiumDTO);
        condominium = condominiumRepository.save(condominium);
        return condominiumMapper.toDto(condominium);
    }

    @Override
    public Optional<CondominiumDTO> partialUpdate(CondominiumDTO condominiumDTO) {
        log.debug("Request to partially update Condominium : {}", condominiumDTO);

        return condominiumRepository
            .findById(condominiumDTO.getId())
            .map(existingCondominium -> {
                condominiumMapper.partialUpdate(existingCondominium, condominiumDTO);

                return existingCondominium;
            })
            .map(condominiumRepository::save)
            .map(condominiumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CondominiumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Condominiums");
        return condominiumRepository.findAll(pageable).map(condominiumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CondominiumDTO> findOne(Long id) {
        log.debug("Request to get Condominium : {}", id);
        return condominiumRepository.findById(id).map(condominiumMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Condominium : {}", id);
        condominiumRepository.deleteById(id);
    }
}
