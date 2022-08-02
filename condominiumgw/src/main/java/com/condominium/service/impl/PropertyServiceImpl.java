package com.condominium.service.impl;

import com.condominium.domain.Property;
import com.condominium.repository.PropertyRepository;
import com.condominium.service.PropertyService;
import com.condominium.service.dto.PropertyDTO;
import com.condominium.service.mapper.PropertyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Property}.
 */
@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);

    private final PropertyRepository propertyRepository;

    private final PropertyMapper propertyMapper;

    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    @Override
    public Mono<PropertyDTO> save(PropertyDTO propertyDTO) {
        log.debug("Request to save Property : {}", propertyDTO);
        return propertyRepository.save(propertyMapper.toEntity(propertyDTO)).map(propertyMapper::toDto);
    }

    @Override
    public Mono<PropertyDTO> update(PropertyDTO propertyDTO) {
        log.debug("Request to save Property : {}", propertyDTO);
        return propertyRepository.save(propertyMapper.toEntity(propertyDTO)).map(propertyMapper::toDto);
    }

    @Override
    public Mono<PropertyDTO> partialUpdate(PropertyDTO propertyDTO) {
        log.debug("Request to partially update Property : {}", propertyDTO);

        return propertyRepository
            .findById(propertyDTO.getId())
            .map(existingProperty -> {
                propertyMapper.partialUpdate(existingProperty, propertyDTO);

                return existingProperty;
            })
            .flatMap(propertyRepository::save)
            .map(propertyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PropertyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Properties");
        return propertyRepository.findAllBy(pageable).map(propertyMapper::toDto);
    }

    public Flux<PropertyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return propertyRepository.findAllWithEagerRelationships(pageable).map(propertyMapper::toDto);
    }

    public Mono<Long> countAll() {
        return propertyRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PropertyDTO> findOne(Long id) {
        log.debug("Request to get Property : {}", id);
        return propertyRepository.findOneWithEagerRelationships(id).map(propertyMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Property : {}", id);
        return propertyRepository.deleteById(id);
    }
}
