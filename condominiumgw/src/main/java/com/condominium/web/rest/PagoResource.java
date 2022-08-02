package com.condominium.web.rest;

import com.condominium.repository.PagoRepository;
import com.condominium.service.PagoService;
import com.condominium.service.dto.PagoDTO;
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
 * REST controller for managing {@link com.condominium.domain.Pago}.
 */
@RestController
@RequestMapping("/api")
public class PagoResource {

    private final Logger log = LoggerFactory.getLogger(PagoResource.class);

    private static final String ENTITY_NAME = "pago";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PagoService pagoService;

    private final PagoRepository pagoRepository;

    public PagoResource(PagoService pagoService, PagoRepository pagoRepository) {
        this.pagoService = pagoService;
        this.pagoRepository = pagoRepository;
    }

    /**
     * {@code POST  /pagos} : Create a new pago.
     *
     * @param pagoDTO the pagoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pagoDTO, or with status {@code 400 (Bad Request)} if the pago has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pagos")
    public Mono<ResponseEntity<PagoDTO>> createPago(@Valid @RequestBody PagoDTO pagoDTO) throws URISyntaxException {
        log.debug("REST request to save Pago : {}", pagoDTO);
        if (pagoDTO.getId() != null) {
            throw new BadRequestAlertException("A new pago cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return pagoService
            .save(pagoDTO)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/pagos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /pagos/:id} : Updates an existing pago.
     *
     * @param id the id of the pagoDTO to save.
     * @param pagoDTO the pagoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagoDTO,
     * or with status {@code 400 (Bad Request)} if the pagoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pagoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pagos/{id}")
    public Mono<ResponseEntity<PagoDTO>> updatePago(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PagoDTO pagoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Pago : {}, {}", id, pagoDTO);
        if (pagoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pagoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return pagoService
                    .update(pagoDTO)
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
     * {@code PATCH  /pagos/:id} : Partial updates given fields of an existing pago, field will ignore if it is null
     *
     * @param id the id of the pagoDTO to save.
     * @param pagoDTO the pagoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pagoDTO,
     * or with status {@code 400 (Bad Request)} if the pagoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pagoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pagoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pagos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<PagoDTO>> partialUpdatePago(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PagoDTO pagoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Pago partially : {}, {}", id, pagoDTO);
        if (pagoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pagoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return pagoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<PagoDTO> result = pagoService.partialUpdate(pagoDTO);

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
     * {@code GET  /pagos} : get all the pagos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pagos in body.
     */
    @GetMapping("/pagos")
    public Mono<ResponseEntity<List<PagoDTO>>> getAllPagos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Pagos");
        return pagoService
            .countAll()
            .zipWith(pagoService.findAll(pageable).collectList())
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
     * {@code GET  /pagos/:id} : get the "id" pago.
     *
     * @param id the id of the pagoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pagoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pagos/{id}")
    public Mono<ResponseEntity<PagoDTO>> getPago(@PathVariable Long id) {
        log.debug("REST request to get Pago : {}", id);
        Mono<PagoDTO> pagoDTO = pagoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pagoDTO);
    }

    /**
     * {@code DELETE  /pagos/:id} : delete the "id" pago.
     *
     * @param id the id of the pagoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pagos/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deletePago(@PathVariable Long id) {
        log.debug("REST request to delete Pago : {}", id);
        return pagoService
            .delete(id)
            .map(result ->
                ResponseEntity
                    .noContent()
                    .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                    .build()
            );
    }
}
