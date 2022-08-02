package com.condominium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.condominium.IntegrationTest;
import com.condominium.domain.Cuota;
import com.condominium.domain.enumeration.CuotaType;
import com.condominium.domain.enumeration.RegisterState;
import com.condominium.repository.CuotaRepository;
import com.condominium.repository.EntityManager;
import com.condominium.service.dto.CuotaDTO;
import com.condominium.service.mapper.CuotaMapper;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link CuotaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CuotaResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final CuotaType DEFAULT_TIPO = CuotaType.ORDINARIA;
    private static final CuotaType UPDATED_TIPO = CuotaType.EXTRAORDINARIA;

    private static final String DEFAULT_PERIODICIDAD = "AAAAAAAAAA";
    private static final String UPDATED_PERIODICIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_APLICA = "AAAAAAAAAA";
    private static final String UPDATED_APLICA = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTO = 1D;
    private static final Double UPDATED_MONTO = 2D;

    private static final String DEFAULT_DIPONIBILIDAD = "AAAAAAAAAA";
    private static final String UPDATED_DIPONIBILIDAD = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACION = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACION = "BBBBBBBBBB";

    private static final RegisterState DEFAULT_ESTADO = RegisterState.ACTIVO;
    private static final RegisterState UPDATED_ESTADO = RegisterState.DESACTIVO;

    private static final String ENTITY_API_URL = "/api/cuotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private CuotaMapper cuotaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Cuota cuota;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuota createEntity(EntityManager em) {
        Cuota cuota = new Cuota()
            .nombre(DEFAULT_NOMBRE)
            .tipo(DEFAULT_TIPO)
            .periodicidad(DEFAULT_PERIODICIDAD)
            .aplica(DEFAULT_APLICA)
            .monto(DEFAULT_MONTO)
            .diponibilidad(DEFAULT_DIPONIBILIDAD)
            .observacion(DEFAULT_OBSERVACION)
            .estado(DEFAULT_ESTADO);
        return cuota;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cuota createUpdatedEntity(EntityManager em) {
        Cuota cuota = new Cuota()
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .periodicidad(UPDATED_PERIODICIDAD)
            .aplica(UPDATED_APLICA)
            .monto(UPDATED_MONTO)
            .diponibilidad(UPDATED_DIPONIBILIDAD)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);
        return cuota;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Cuota.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        cuota = createEntity(em);
    }

    @Test
    void createCuota() throws Exception {
        int databaseSizeBeforeCreate = cuotaRepository.findAll().collectList().block().size();
        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeCreate + 1);
        Cuota testCuota = cuotaList.get(cuotaList.size() - 1);
        assertThat(testCuota.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCuota.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCuota.getPeriodicidad()).isEqualTo(DEFAULT_PERIODICIDAD);
        assertThat(testCuota.getAplica()).isEqualTo(DEFAULT_APLICA);
        assertThat(testCuota.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testCuota.getDiponibilidad()).isEqualTo(DEFAULT_DIPONIBILIDAD);
        assertThat(testCuota.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testCuota.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    void createCuotaWithExistingId() throws Exception {
        // Create the Cuota with an existing ID
        cuota.setId(1L);
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        int databaseSizeBeforeCreate = cuotaRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setNombre(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setTipo(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPeriodicidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setPeriodicidad(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAplicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setAplica(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setMonto(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDiponibilidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setDiponibilidad(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkObservacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setObservacion(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().collectList().block().size();
        // set the field null
        cuota.setEstado(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCuotas() {
        // Initialize the database
        cuotaRepository.save(cuota).block();

        // Get all the cuotaList
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
            .value(hasItem(cuota.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].tipo")
            .value(hasItem(DEFAULT_TIPO.toString()))
            .jsonPath("$.[*].periodicidad")
            .value(hasItem(DEFAULT_PERIODICIDAD))
            .jsonPath("$.[*].aplica")
            .value(hasItem(DEFAULT_APLICA))
            .jsonPath("$.[*].monto")
            .value(hasItem(DEFAULT_MONTO.doubleValue()))
            .jsonPath("$.[*].diponibilidad")
            .value(hasItem(DEFAULT_DIPONIBILIDAD))
            .jsonPath("$.[*].observacion")
            .value(hasItem(DEFAULT_OBSERVACION))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.toString()));
    }

    @Test
    void getCuota() {
        // Initialize the database
        cuotaRepository.save(cuota).block();

        // Get the cuota
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cuota.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cuota.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.tipo")
            .value(is(DEFAULT_TIPO.toString()))
            .jsonPath("$.periodicidad")
            .value(is(DEFAULT_PERIODICIDAD))
            .jsonPath("$.aplica")
            .value(is(DEFAULT_APLICA))
            .jsonPath("$.monto")
            .value(is(DEFAULT_MONTO.doubleValue()))
            .jsonPath("$.diponibilidad")
            .value(is(DEFAULT_DIPONIBILIDAD))
            .jsonPath("$.observacion")
            .value(is(DEFAULT_OBSERVACION))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.toString()));
    }

    @Test
    void getNonExistingCuota() {
        // Get the cuota
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCuota() throws Exception {
        // Initialize the database
        cuotaRepository.save(cuota).block();

        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();

        // Update the cuota
        Cuota updatedCuota = cuotaRepository.findById(cuota.getId()).block();
        updatedCuota
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .periodicidad(UPDATED_PERIODICIDAD)
            .aplica(UPDATED_APLICA)
            .monto(UPDATED_MONTO)
            .diponibilidad(UPDATED_DIPONIBILIDAD)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);
        CuotaDTO cuotaDTO = cuotaMapper.toDto(updatedCuota);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cuotaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
        Cuota testCuota = cuotaList.get(cuotaList.size() - 1);
        assertThat(testCuota.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCuota.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuota.getPeriodicidad()).isEqualTo(UPDATED_PERIODICIDAD);
        assertThat(testCuota.getAplica()).isEqualTo(UPDATED_APLICA);
        assertThat(testCuota.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testCuota.getDiponibilidad()).isEqualTo(UPDATED_DIPONIBILIDAD);
        assertThat(testCuota.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testCuota.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void putNonExistingCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cuotaDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCuotaWithPatch() throws Exception {
        // Initialize the database
        cuotaRepository.save(cuota).block();

        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();

        // Update the cuota using partial update
        Cuota partialUpdatedCuota = new Cuota();
        partialUpdatedCuota.setId(cuota.getId());

        partialUpdatedCuota.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).diponibilidad(UPDATED_DIPONIBILIDAD).estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCuota.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCuota))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
        Cuota testCuota = cuotaList.get(cuotaList.size() - 1);
        assertThat(testCuota.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCuota.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuota.getPeriodicidad()).isEqualTo(DEFAULT_PERIODICIDAD);
        assertThat(testCuota.getAplica()).isEqualTo(DEFAULT_APLICA);
        assertThat(testCuota.getMonto()).isEqualTo(DEFAULT_MONTO);
        assertThat(testCuota.getDiponibilidad()).isEqualTo(UPDATED_DIPONIBILIDAD);
        assertThat(testCuota.getObservacion()).isEqualTo(DEFAULT_OBSERVACION);
        assertThat(testCuota.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void fullUpdateCuotaWithPatch() throws Exception {
        // Initialize the database
        cuotaRepository.save(cuota).block();

        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();

        // Update the cuota using partial update
        Cuota partialUpdatedCuota = new Cuota();
        partialUpdatedCuota.setId(cuota.getId());

        partialUpdatedCuota
            .nombre(UPDATED_NOMBRE)
            .tipo(UPDATED_TIPO)
            .periodicidad(UPDATED_PERIODICIDAD)
            .aplica(UPDATED_APLICA)
            .monto(UPDATED_MONTO)
            .diponibilidad(UPDATED_DIPONIBILIDAD)
            .observacion(UPDATED_OBSERVACION)
            .estado(UPDATED_ESTADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCuota.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCuota))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
        Cuota testCuota = cuotaList.get(cuotaList.size() - 1);
        assertThat(testCuota.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCuota.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCuota.getPeriodicidad()).isEqualTo(UPDATED_PERIODICIDAD);
        assertThat(testCuota.getAplica()).isEqualTo(UPDATED_APLICA);
        assertThat(testCuota.getMonto()).isEqualTo(UPDATED_MONTO);
        assertThat(testCuota.getDiponibilidad()).isEqualTo(UPDATED_DIPONIBILIDAD);
        assertThat(testCuota.getObservacion()).isEqualTo(UPDATED_OBSERVACION);
        assertThat(testCuota.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    void patchNonExistingCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cuotaDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().collectList().block().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCuota() {
        // Initialize the database
        cuotaRepository.save(cuota).block();

        int databaseSizeBeforeDelete = cuotaRepository.findAll().collectList().block().size();

        // Delete the cuota
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cuota.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Cuota> cuotaList = cuotaRepository.findAll().collectList().block();
        assertThat(cuotaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
