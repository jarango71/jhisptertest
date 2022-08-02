package com.payment.service.impl;

import com.payment.domain.Pago;
import com.payment.repository.PagoRepository;
import com.payment.service.PagoService;
import com.payment.service.dto.PagoDTO;
import com.payment.service.mapper.PagoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PagoDTO save(PagoDTO pagoDTO) {
        log.debug("Request to save Pago : {}", pagoDTO);
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago = pagoRepository.save(pago);
        return pagoMapper.toDto(pago);
    }

    @Override
    public PagoDTO update(PagoDTO pagoDTO) {
        log.debug("Request to save Pago : {}", pagoDTO);
        Pago pago = pagoMapper.toEntity(pagoDTO);
        pago = pagoRepository.save(pago);
        return pagoMapper.toDto(pago);
    }

    @Override
    public Optional<PagoDTO> partialUpdate(PagoDTO pagoDTO) {
        log.debug("Request to partially update Pago : {}", pagoDTO);

        return pagoRepository
            .findById(pagoDTO.getId())
            .map(existingPago -> {
                pagoMapper.partialUpdate(existingPago, pagoDTO);

                return existingPago;
            })
            .map(pagoRepository::save)
            .map(pagoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pagos");
        return pagoRepository.findAll(pageable).map(pagoMapper::toDto);
    }

    public Page<PagoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pagoRepository.findAllWithEagerRelationships(pageable).map(pagoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PagoDTO> findOne(Long id) {
        log.debug("Request to get Pago : {}", id);
        return pagoRepository.findOneWithEagerRelationships(id).map(pagoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pago : {}", id);
        pagoRepository.deleteById(id);
    }
}
