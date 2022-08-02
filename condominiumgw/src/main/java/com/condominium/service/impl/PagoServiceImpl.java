package com.condominium.service.impl;

import com.condominium.domain.Pago;
import com.condominium.repository.PagoRepository;
import com.condominium.service.PagoService;
import com.condominium.service.dto.PagoDTO;
import com.condominium.service.mapper.PagoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Pago}.
 */
@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);

    private final PagoRepository pagoRepository;

    private final PagoMapper pagoMapper;

    public PagoServiceImpl(PagoRepository pagoRepository, PagoMapper pagoMapper) {
        this.pagoRepository = pagoRepository;
        this.pagoMapper = pagoMapper;
    }

    @Override
    public Mono<PagoDTO> save(PagoDTO pagoDTO) {
        log.debug("Request to save Pago : {}", pagoDTO);
        return pagoRepository.save(pagoMapper.toEntity(pagoDTO)).map(pagoMapper::toDto);
    }

    @Override
    public Mono<PagoDTO> update(PagoDTO pagoDTO) {
        log.debug("Request to save Pago : {}", pagoDTO);
        return pagoRepository.save(pagoMapper.toEntity(pagoDTO)).map(pagoMapper::toDto);
    }

    @Override
    public Mono<PagoDTO> partialUpdate(PagoDTO pagoDTO) {
        log.debug("Request to partially update Pago : {}", pagoDTO);

        return pagoRepository
            .findById(pagoDTO.getId())
            .map(existingPago -> {
                pagoMapper.partialUpdate(existingPago, pagoDTO);

                return existingPago;
            })
            .flatMap(pagoRepository::save)
            .map(pagoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<PagoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pagos");
        return pagoRepository.findAllBy(pageable).map(pagoMapper::toDto);
    }

    public Flux<PagoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pagoRepository.findAllWithEagerRelationships(pageable).map(pagoMapper::toDto);
    }

    public Mono<Long> countAll() {
        return pagoRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<PagoDTO> findOne(Long id) {
        log.debug("Request to get Pago : {}", id);
        return pagoRepository.findOneWithEagerRelationships(id).map(pagoMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Pago : {}", id);
        return pagoRepository.deleteById(id);
    }
}
