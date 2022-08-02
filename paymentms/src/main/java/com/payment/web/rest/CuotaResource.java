package com.payment.web.rest;

import com.payment.repository.CuotaRepository;
import com.payment.service.CuotaService;
import com.payment.service.dto.CuotaDTO;
import com.payment.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.payment.domain.Cuota}.
 */
@RestController
@RequestMapping("/api")
public class CuotaResource {

    private final Logger log = LoggerFactory.getLogger(CuotaResource.class);

    private static final String ENTITY_NAME = "paymentmsCuota";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CuotaService cuotaService;

    private final CuotaRepository cuotaRepository;

    public CuotaResource(CuotaService cuotaService, CuotaRepository cuotaRepository) {
        this.cuotaService = cuotaService;
        this.cuotaRepository = cuotaRepository;
    }

    /**
     * {@code POST  /cuotas} : Create a new cuota.
     *
     * @param cuotaDTO the cuotaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cuotaDTO, or with status {@code 400 (Bad Request)} if the cuota has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cuotas")
    public ResponseEntity<CuotaDTO> createCuota(@Valid @RequestBody CuotaDTO cuotaDTO) throws URISyntaxException {
        log.debug("REST request to save Cuota : {}", cuotaDTO);
        if (cuotaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cuota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CuotaDTO result = cuotaService.save(cuotaDTO);
        return ResponseEntity
            .created(new URI("/api/cuotas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cuotas/:id} : Updates an existing cuota.
     *
     * @param id the id of the cuotaDTO to save.
     * @param cuotaDTO the cuotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuotaDTO,
     * or with status {@code 400 (Bad Request)} if the cuotaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cuotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cuotas/{id}")
    public ResponseEntity<CuotaDTO> updateCuota(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CuotaDTO cuotaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Cuota : {}, {}", id, cuotaDTO);
        if (cuotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CuotaDTO result = cuotaService.update(cuotaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuotaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cuotas/:id} : Partial updates given fields of an existing cuota, field will ignore if it is null
     *
     * @param id the id of the cuotaDTO to save.
     * @param cuotaDTO the cuotaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cuotaDTO,
     * or with status {@code 400 (Bad Request)} if the cuotaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cuotaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cuotaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cuotas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CuotaDTO> partialUpdateCuota(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CuotaDTO cuotaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Cuota partially : {}, {}", id, cuotaDTO);
        if (cuotaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cuotaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cuotaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CuotaDTO> result = cuotaService.partialUpdate(cuotaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cuotaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cuotas} : get all the cuotas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuotas in body.
     */
    @GetMapping("/cuotas")
    public ResponseEntity<List<CuotaDTO>> getAllCuotas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Cuotas");
        Page<CuotaDTO> page = cuotaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cuotas/:id} : get the "id" cuota.
     *
     * @param id the id of the cuotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cuotas/{id}")
    public ResponseEntity<CuotaDTO> getCuota(@PathVariable Long id) {
        log.debug("REST request to get Cuota : {}", id);
        Optional<CuotaDTO> cuotaDTO = cuotaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuotaDTO);
    }

    /**
     * {@code DELETE  /cuotas/:id} : delete the "id" cuota.
     *
     * @param id the id of the cuotaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cuotas/{id}")
    public ResponseEntity<Void> deleteCuota(@PathVariable Long id) {
        log.debug("REST request to delete Cuota : {}", id);
        cuotaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
