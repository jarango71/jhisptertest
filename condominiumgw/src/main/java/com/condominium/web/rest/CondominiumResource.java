package com.condominium.web.rest;

import com.condominium.repository.CondominiumRepository;
import com.condominium.service.CondominiumService;
import com.condominium.service.dto.CondominiumDTO;
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
 * REST controller for managing {@link com.condominium.domain.Condominium}.
 */
@RestController
@RequestMapping("/api")
public class CondominiumResource {

    private final Logger log = LoggerFactory.getLogger(CondominiumResource.class);

    private static final String ENTITY_NAME = "condominium";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CondominiumService condominiumService;

    private final CondominiumRepository condominiumRepository;

    public CondominiumResource(CondominiumService condominiumService, CondominiumRepository condominiumRepository) {
        this.condominiumService = condominiumService;
        this.condominiumRepository = condominiumRepository;
    }

    /**
     * {@code POST  /condominiums} : Create a new condominium.
     *
     * @param condominiumDTO the condominiumDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new condominiumDTO, or with status {@code 400 (Bad Request)} if the condominium has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/condominiums")
    public Mono<ResponseEntity<CondominiumDTO>> createCondominium(@Valid @RequestBody CondominiumDTO condominiumDTO)
        throws URISyntaxException {
        log.debug("REST request to save Condominium : {}", condominiumDTO);
        if (condominiumDTO.getId() != null) {
            throw new BadRequestAlertException("A new condominium cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return condominiumService
            .save(condominiumDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/condominiums/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /condominiums/:id} : Updates an existing condominium.
     *
     * @param id the id of the condominiumDTO to save.
     * @param condominiumDTO the condominiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated condominiumDTO,
     * or with status {@code 400 (Bad Request)} if the condominiumDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the condominiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/condominiums/{id}")
    public Mono<ResponseEntity<CondominiumDTO>> updateCondominium(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CondominiumDTO condominiumDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Condominium : {}, {}", id, condominiumDTO);
        if (condominiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, condominiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return condominiumRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return condominiumService
                    .update(condominiumDTO)
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
     * {@code PATCH  /condominiums/:id} : Partial updates given fields of an existing condominium, field will ignore if it is null
     *
     * @param id the id of the condominiumDTO to save.
     * @param condominiumDTO the condominiumDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated condominiumDTO,
     * or with status {@code 400 (Bad Request)} if the condominiumDTO is not valid,
     * or with status {@code 404 (Not Found)} if the condominiumDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the condominiumDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/condominiums/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CondominiumDTO>> partialUpdateCondominium(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CondominiumDTO condominiumDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Condominium partially : {}, {}", id, condominiumDTO);
        if (condominiumDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, condominiumDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return condominiumRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CondominiumDTO> result = condominiumService.partialUpdate(condominiumDTO);

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
     * {@code GET  /condominiums} : get all the condominiums.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of condominiums in body.
     */
    @GetMapping("/condominiums")
    public Mono<ResponseEntity<List<CondominiumDTO>>> getAllCondominiums(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        log.debug("REST request to get a page of Condominiums");
        return condominiumService
            .countAll()
            .zipWith(condominiumService.findAll(pageable).collectList())
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
     * {@code GET  /condominiums/:id} : get the "id" condominium.
     *
     * @param id the id of the condominiumDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the condominiumDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/condominiums/{id}")
    public Mono<ResponseEntity<CondominiumDTO>> getCondominium(@PathVariable Long id) {
        log.debug("REST request to get Condominium : {}", id);
        Mono<CondominiumDTO> condominiumDTO = condominiumService.findOne(id);
        return ResponseUtil.wrapOrNotFound(condominiumDTO);
    }

    /**
     * {@code DELETE  /condominiums/:id} : delete the "id" condominium.
     *
     * @param id the id of the condominiumDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/condominiums/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteCondominium(@PathVariable Long id) {
        log.debug("REST request to delete Condominium : {}", id);
        return condominiumService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
