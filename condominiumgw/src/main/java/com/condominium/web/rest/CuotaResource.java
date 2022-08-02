package com.condominium.web.rest;

import com.condominium.repository.CuotaRepository;
import com.condominium.service.CuotaService;
import com.condominium.service.dto.CuotaDTO;
import com.condominium.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.condominium.domain.Cuota}.
 */
@RestController
@RequestMapping("/api")
public class CuotaResource {

    private final Logger log = LoggerFactory.getLogger(CuotaResource.class);

    private static final String ENTITY_NAME = "cuota";

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
    public Mono<ResponseEntity<CuotaDTO>> createCuota(@Valid @RequestBody CuotaDTO cuotaDTO) throws URISyntaxException {
        log.debug("REST request to save Cuota : {}", cuotaDTO);
        if (cuotaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cuota cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cuotaService
            .save(cuotaDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/cuotas/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<CuotaDTO>> updateCuota(
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

        return cuotaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cuotaService
                    .update(cuotaDTO)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<CuotaDTO>> partialUpdateCuota(
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

        return cuotaRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CuotaDTO> result = cuotaService.partialUpdate(cuotaDTO);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /cuotas} : get all the cuotas.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cuotas in body.
     */
    @GetMapping("/cuotas")
    public Mono<ResponseEntity<List<CuotaDTO>>> getAllCuotas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Cuotas");
        return cuotaService
            .countAll()
            .zipWith(cuotaService.findAll(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /cuotas/:id} : get the "id" cuota.
     *
     * @param id the id of the cuotaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cuotaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cuotas/{id}")
    public Mono<ResponseEntity<CuotaDTO>> getCuota(@PathVariable Long id) {
        log.debug("REST request to get Cuota : {}", id);
        Mono<CuotaDTO> cuotaDTO = cuotaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cuotaDTO);
    }

    /**
     * {@code DELETE  /cuotas/:id} : delete the "id" cuota.
     *
     * @param id the id of the cuotaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cuotas/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCuota(@PathVariable Long id) {
        log.debug("REST request to delete Cuota : {}", id);
        return cuotaService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
