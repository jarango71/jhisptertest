package com.condominium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.condominium.IntegrationTest;
import com.condominium.domain.Condominium;
import com.condominium.domain.Property;
import com.condominium.domain.enumeration.RegisterState;
import com.condominium.repository.EntityManager;
import com.condominium.repository.PropertyRepository;
import com.condominium.service.PropertyService;
import com.condominium.service.dto.PropertyDTO;
import com.condominium.service.mapper.PropertyMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link PropertyResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PropertyResourceIT {

    private static final String DEFAULT_MANZANA = "AAAAAAAAAA";
    private static final String UPDATED_MANZANA = "BBBBBBBBBB";

    private static final String DEFAULT_BLOQUE = "AAAAAAAAAA";
    private static final String UPDATED_BLOQUE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_DIPONIBILIDAD = "AAAAAAAAAA";
    private static final String UPDATED_DIPONIBILIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final RegisterState DEFAULT_ESTADO = RegisterState.ACTIVO;
    private static final RegisterState UPDATED_ESTADO = RegisterState.DESACTIVO;

    private static final String ENTITY_API_URL = "/api/properties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PropertyRepository propertyRepository;

    @Mock
    private PropertyRepository propertyRepositoryMock;

    @Autowired
    private PropertyMapper propertyMapper;

    @Mock
    private PropertyService propertyServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Property property;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Property createEntity(EntityManager em) {
        Property property = new Property()
            .manzana(DEFAULT_MANZANA)
            .bloque(DEFAULT_BLOQUE)
            .numero(DEFAULT_NUMERO)
            .ubicacion(DEFAULT_UBICACION)
            .tipo(DEFAULT_TIPO)
            .diponibilidad(DEFAULT_DIPONIBILIDAD)
            .observacion(DEFAULT_OBSERVACION)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        Condominium condominium;
        condominium = em.insert(CondominiumResourceIT.createEntity(em)).block();
        property.setCondominium(condominium);
        return property;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Property createUpdatedEntity(EntityManager em) {
        Property property = new Property()
            .manzana(UPDATED_MANZANA)
            .bloque(UPDATED_BLOQUE)
            .numero(UPDATED_NUMERO)
            .ubicacion(UPDATED_UBICACION)
            .tipo(UPDATED_TIPO)
            .diponibilidad(UPDATED_DIPONIBILIDAD)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);
        // Add required entity
        Condominium condominium;
        condominium = em.insert(CondominiumResourceIT.createUpdatedEntity(em)).block();
        property.setCondominium(condominium);
        return property;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Property.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CondominiumResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        property = createEntity(em);
    }

    @Test
    void createProperty() throws Exception {
        int databaseSizeBeforeCreate = propertyRepository.findAll().collectList().block().size();
        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeCreate + 1);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getManzana()).isEqualTo(DEFAULT_MANZANA);
        assertThat(testProperty.getBloque()).isEqualTo(DEFAULT_BLOQUE);
        assertThat(testProperty.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testProperty.getUbicacion()).isEqualTo(DEFAULT_UBICACION);
        assertThat(testProperty.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testProperty.getDiponibilidad()).isEqualTo(DEFAULT_DIPONIBILIDAD);
        assertThat(testProperty.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testProperty.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void createPropertyWithExistingId() throws Exception {
        // Create the Property with an existing ID
        property.setId(1L);
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        int databaseSizeBeforeCreate = propertyRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkManzanaIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setManzana(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBloqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setBloque(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setNumero(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUbicacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setUbicacion(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setTipo(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDiponibilidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setDiponibilidad(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkObservacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setObservacion(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = propertyRepository.findAll().collectList().block().size();
        // set the field null
        property.setEstado(null);

        // Create the Property, which fails.
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllProperties() {
        // Initialize the database
        propertyRepository.save(property).block();

        // Get all the propertyList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(property.getId().intValue()))
            .jsonPath("$.[*].manzana")
            .value(hasItem(DEFAULT_MANZANA))
            .jsonPath("$.[*].bloque")
            .value(hasItem(DEFAULT_BLOQUE))
            .jsonPath("$.[*].numero")
            .value(hasItem(DEFAULT_NUMERO))
            .jsonPath("$.[*].ubicacion")
            .value(hasItem(DEFAULT_UBICACION))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO))
            .jsonPath("$.[*].diponibilidad")
            .value(hasItem(DEFAULT_DIPONIBILIDAD))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPropertiesWithEagerRelationshipsIsEnabled() {
        when(propertyServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(propertyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPropertiesWithEagerRelationshipsIsNotEnabled() {
        when(propertyServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(propertyServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getProperty() {
        // Initialize the database
        propertyRepository.save(property).block();

        // Get the property
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, property.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(property.getId().intValue()))
            .jsonPath("$.manzana")
            .value(is(DEFAULT_MANZANA))
            .jsonPath("$.bloque")
            .value(is(DEFAULT_BLOQUE))
            .jsonPath("$.numero")
            .value(is(DEFAULT_NUMERO))
            .jsonPath("$.ubicacion")
            .value(is(DEFAULT_UBICACION))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO))
            .jsonPath("$.diponibilidad")
            .value(is(DEFAULT_DIPONIBILIDAD))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.toString()));
    }

    @Test
    void getNonExistingProperty() {
        // Get the property
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewProperty() throws Exception {
        // Initialize the database
        propertyRepository.save(property).block();

        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();

        // Update the property
        Property updatedProperty = propertyRepository.findById(property.getId()).block();
        updatedProperty
            .manzana(UPDATED_MANZANA)
            .bloque(UPDATED_BLOQUE)
            .numero(UPDATED_NUMERO)
            .ubicacion(UPDATED_UBICACION)
            .tipo(UPDATED_TIPO)
            .diponibilidad(UPDATED_DIPONIBILIDAD)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);
        PropertyDTO propertyDTO = propertyMapper.toDto(updatedProperty);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, propertyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getManzana()).isEqualTo(UPDATED_MANZANA);
        assertThat(testProperty.getBloque()).isEqualTo(UPDATED_BLOQUE);
        assertThat(testProperty.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testProperty.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testProperty.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testProperty.getDiponibilidad()).isEqualTo(UPDATED_DIPONIBILIDAD);
        assertThat(testProperty.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testProperty.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void putNonExistingProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();
        property.setId(count.incrementAndGet());

        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, propertyDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();
        property.setId(count.incrementAndGet());

        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();
        property.setId(count.incrementAndGet());

        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePropertyWithPatch() throws Exception {
        // Initialize the database
        propertyRepository.save(property).block();

        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();

        // Update the property using partial update
        Property partialUpdatedProperty = new Property();
        partialUpdatedProperty.setId(property.getId());

        partialUpdatedProperty
            .manzana(UPDATED_MANZANA)
            .ubicacion(UPDATED_UBICACION)
            .tipo(UPDATED_TIPO)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProperty.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProperty))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getManzana()).isEqualTo(UPDATED_MANZANA);
        assertThat(testProperty.getBloque()).isEqualTo(DEFAULT_BLOQUE);
        assertThat(testProperty.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testProperty.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testProperty.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testProperty.getDiponibilidad()).isEqualTo(DEFAULT_DIPONIBILIDAD);
        assertThat(testProperty.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testProperty.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void fullUpdatePropertyWithPatch() throws Exception {
        // Initialize the database
        propertyRepository.save(property).block();

        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();

        // Update the property using partial update
        Property partialUpdatedProperty = new Property();
        partialUpdatedProperty.setId(property.getId());

        partialUpdatedProperty
            .manzana(UPDATED_MANZANA)
            .bloque(UPDATED_BLOQUE)
            .numero(UPDATED_NUMERO)
            .ubicacion(UPDATED_UBICACION)
            .tipo(UPDATED_TIPO)
            .diponibilidad(UPDATED_DIPONIBILIDAD)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedProperty.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedProperty))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
        Property testProperty = propertyList.get(propertyList.size() - 1);
        assertThat(testProperty.getManzana()).isEqualTo(UPDATED_MANZANA);
        assertThat(testProperty.getBloque()).isEqualTo(UPDATED_BLOQUE);
        assertThat(testProperty.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testProperty.getUbicacion()).isEqualTo(UPDATED_UBICACION);
        assertThat(testProperty.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testProperty.getDiponibilidad()).isEqualTo(UPDATED_DIPONIBILIDAD);
        assertThat(testProperty.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testProperty.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void patchNonExistingProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();
        property.setId(count.incrementAndGet());

        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, propertyDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();
        property.setId(count.incrementAndGet());

        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamProperty() throws Exception {
        int databaseSizeBeforeUpdate = propertyRepository.findAll().collectList().block().size();
        property.setId(count.incrementAndGet());

        // Create the Property
        PropertyDTO propertyDTO = propertyMapper.toDto(property);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(propertyDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Property in the database
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteProperty() {
        // Initialize the database
        propertyRepository.save(property).block();

        int databaseSizeBeforeDelete = propertyRepository.findAll().collectList().block().size();

        // Delete the property
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, property.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Property> propertyList = propertyRepository.findAll().collectList().block();
        assertThat(propertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
