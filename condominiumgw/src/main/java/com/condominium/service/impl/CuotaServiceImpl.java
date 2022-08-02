package com.condominium.service.impl;

import com.condominium.domain.Cuota;
import com.condominium.repository.CuotaRepository;
import com.condominium.service.CuotaService;
import com.condominium.service.dto.CuotaDTO;
import com.condominium.service.mapper.CuotaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Cuota}.
 */
@Service
@Transactional
public class CuotaServiceImpl implements CuotaService {

    private final Logger log = LoggerFactory.getLogger(CuotaServiceImpl.class);

    private final CuotaRepository cuotaRepository;

    private final CuotaMapper cuotaMapper;

    public CuotaServiceImpl(CuotaRepository cuotaRepository, CuotaMapper cuotaMapper) {
        this.cuotaRepository = cuotaRepository;
        this.cuotaMapper = cuotaMapper;
    }

    @Override
    public Mono<CuotaDTO> save(CuotaDTO cuotaDTO) {
        log.debug("Request to save Cuota : {}", cuotaDTO);
        return cuotaRepository.save(cuotaMapper.toEntity(cuotaDTO)).map(cuotaMapper::toDto);
    }

    @Override
    public Mono<CuotaDTO> update(CuotaDTO cuotaDTO) {
        log.debug("Request to save Cuota : {}", cuotaDTO);
        return cuotaRepository.save(cuotaMapper.toEntity(cuotaDTO)).map(cuotaMapper::toDto);
    }

    @Override
    public Mono<CuotaDTO> partialUpdate(CuotaDTO cuotaDTO) {
        log.debug("Request to partially update Cuota : {}", cuotaDTO);

        return cuotaRepository
            .findById(cuotaDTO.getId())
            .map(existingCuota -> {
                cuotaMapper.partialUpdate(existingCuota, cuotaDTO);

                return existingCuota;
            })
            .flatMap(cuotaRepository::save)
            .map(cuotaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<CuotaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cuotas");
        return cuotaRepository.findAllBy(pageable).map(cuotaMapper::toDto);
    }

    public Mono<Long> countAll() {
        return cuotaRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<CuotaDTO> findOne(Long id) {
        log.debug("Request to get Cuota : {}", id);
        return cuotaRepository.findById(id).map(cuotaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Cuota : {}", id);
        return cuotaRepository.deleteById(id);
    }
}
