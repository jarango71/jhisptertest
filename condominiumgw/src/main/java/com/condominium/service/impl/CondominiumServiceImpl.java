package com.condominium.service.impl;

import com.condominium.domain.Condominium;
import com.condominium.repository.CondominiumRepository;
import com.condominium.service.CondominiumService;
import com.condominium.service.dto.CondominiumDTO;
import com.condominium.service.mapper.CondominiumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<CondominiumDTO> save(CondominiumDTO condominiumDTO) {
        log.debug("Request to save Condominium : {}", condominiumDTO);
        return condominiumRepository.save(condominiumMapper.toEntity(condominiumDTO)).map(condominiumMapper::toDto);
    }

    @Override
    public Mono<CondominiumDTO> update(CondominiumDTO condominiumDTO) {
        log.debug("Request to save Condominium : {}", condominiumDTO);
        return condominiumRepository.save(condominiumMapper.toEntity(condominiumDTO)).map(condominiumMapper::toDto);
    }

    @Override
    public Mono<CondominiumDTO> partialUpdate(CondominiumDTO condominiumDTO) {
        log.debug("Request to partially update Condominium : {}", condominiumDTO);

        return condominiumRepository
            .findById(condominiumDTO.getId())
            .map(existingCondominium -> {
                condominiumMapper.partialUpdate(existingCondominium, condominiumDTO);

                return existingCondominium;
            })
            .flatMap(condominiumRepository::save)
            .map(condominiumMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CondominiumDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Condominiums");
        return condominiumRepository.findAllBy(pageable).map(condominiumMapper::toDto);
    }

    public Mono<Long> countAll() {
        return condominiumRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CondominiumDTO> findOne(Long id) {
        log.debug("Request to get Condominium : {}", id);
        return condominiumRepository.findById(id).map(condominiumMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Condominium : {}", id);
        return condominiumRepository.deleteById(id);
    }
}
