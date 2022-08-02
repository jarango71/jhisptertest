package com.payment.service.impl;

import com.payment.domain.Cuota;
import com.payment.repository.CuotaRepository;
import com.payment.service.CuotaService;
import com.payment.service.dto.CuotaDTO;
import com.payment.service.mapper.CuotaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CuotaDTO save(CuotaDTO cuotaDTO) {
        log.debug("Request to save Cuota : {}", cuotaDTO);
        Cuota cuota = cuotaMapper.toEntity(cuotaDTO);
        cuota = cuotaRepository.save(cuota);
        return cuotaMapper.toDto(cuota);
    }

    @Override
    public CuotaDTO update(CuotaDTO cuotaDTO) {
        log.debug("Request to save Cuota : {}", cuotaDTO);
        Cuota cuota = cuotaMapper.toEntity(cuotaDTO);
        cuota = cuotaRepository.save(cuota);
        return cuotaMapper.toDto(cuota);
    }

    @Override
    public Optional<CuotaDTO> partialUpdate(CuotaDTO cuotaDTO) {
        log.debug("Request to partially update Cuota : {}", cuotaDTO);

        return cuotaRepository
            .findById(cuotaDTO.getId())
            .map(existingCuota -> {
                cuotaMapper.partialUpdate(existingCuota, cuotaDTO);

                return existingCuota;
            })
            .map(cuotaRepository::save)
            .map(cuotaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CuotaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cuotas");
        return cuotaRepository.findAll(pageable).map(cuotaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CuotaDTO> findOne(Long id) {
        log.debug("Request to get Cuota : {}", id);
        return cuotaRepository.findById(id).map(cuotaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Cuota : {}", id);
        cuotaRepository.deleteById(id);
    }
}
