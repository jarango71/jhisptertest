package com.manager.web.rest;

import com.manager.repository.PropertyRepository;
import com.manager.service.PropertyService;
import com.manager.service.dto.PropertyDTO;
import com.manager.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.manager.domain.Property}.
 */
@RestController
@RequestMapping("/api")
public class PropertyResource {

    private final Logger log = LoggerFactory.getLogger(PropertyResource.class);

    private static final String ENTITY_NAME = "managermsProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropertyService propertyService;

    private final PropertyRepository propertyRepository;

    public PropertyResource(PropertyService propertyService, PropertyRepository propertyRepository) {
        this.propertyService = propertyService;
        this.propertyRepository = propertyRepository;
    }

    /**
     * {@code POST  /properties} : Create a new property.
     *
     * @param propertyDTO the propertyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new propertyDTO, or with status {@code 400 (Bad Request)} if the property has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/properties")
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyDTO propertyDTO) throws URISyntaxException {
        log.debug("REST request to save Property : {}", propertyDTO);
        if (propertyDTO.getId() != null) {
            throw new BadRequestAlertException("A new property cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PropertyDTO result = propertyService.save(propertyDTO);
        return ResponseEntity
            .created(new URI("/api/properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /properties/:id} : Updates an existing property.
     *
     * @param id the id of the propertyDTO to save.
     * @param propertyDTO the propertyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyDTO,
     * or with status {@code 400 (Bad Request)} if the propertyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the propertyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/properties/{id}")
    public ResponseEntity<PropertyDTO> updateProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PropertyDTO propertyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Property : {}, {}", id, propertyDTO);
        if (propertyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PropertyDTO result = propertyService.update(propertyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /properties/:id} : Partial updates given fields of an existing property, field will ignore if it is null
     *
     * @param id the id of the propertyDTO to save.
     * @param propertyDTO the propertyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated propertyDTO,
     * or with status {@code 400 (Bad Request)} if the propertyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the propertyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the propertyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/properties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PropertyDTO> partialUpdateProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PropertyDTO propertyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Property partially : {}, {}", id, propertyDTO);
        if (propertyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, propertyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PropertyDTO> result = propertyService.partialUpdate(propertyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, propertyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /properties} : get all the properties.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of properties in body.
     */
    @GetMapping("/properties")
    public ResponseEntity<List<PropertyDTO>> getAllProperties(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Properties");
        Page<PropertyDTO> page;
        if (eagerload) {
            page = propertyService.findAllWithEagerRelationships(pageable);
        } else {
            page = propertyService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /properties/:id} : get the "id" property.
     *
     * @param id the id of the propertyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the propertyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/properties/{id}")
    public ResponseEntity<PropertyDTO> getProperty(@PathVariable Long id) {
        log.debug("REST request to get Property : {}", id);
        Optional<PropertyDTO> propertyDTO = propertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(propertyDTO);
    }

    /**
     * {@code DELETE  /properties/:id} : delete the "id" property.
     *
     * @param id the id of the propertyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/properties/{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable Long id) {
        log.debug("REST request to delete Property : {}", id);
        propertyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
