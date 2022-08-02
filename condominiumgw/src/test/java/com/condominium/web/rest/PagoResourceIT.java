package com.condominium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.condominium.IntegrationTest;
import com.condominium.domain.Cuota;
import com.condominium.domain.Pago;
import com.condominium.domain.enumeration.RegisterState;
import com.condominium.repository.EntityManager;
import com.condominium.repository.PagoRepository;
import com.condominium.service.PagoService;
import com.condominium.service.dto.PagoDTO;
import com.condominium.service.mapper.PagoMapper;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PagoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PagoResourceIT {

    private static final String DEFAULT_ANIO = "AAAA";
    private static final String UPDATED_ANIO = "BBBB";

    private static final String DEFAULT_MES = "AAAAAAAAAA";
    private static final String UPDATED_MES = "BBBBBBBBBB";

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;

    private static final RegisterState DEFAULT_ESTADO = RegisterState.ACTIVO;
    private static final RegisterState UPDATED_ESTADO = RegisterState.DESACTIVO;

    private static final LocalDate DEFAULT_FECHA_GENERACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_GENERACION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_FECHA_PAGO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_PAGO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/pagos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PagoRepository pagoRepository;

    @Mock
    private PagoRepository pagoRepositoryMock;

    @Autowired
    private PagoMapper pagoMapper;

    @Mock
    private PagoService pagoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Pago pago;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createEntity(EntityManager em) {
        Pago pago = new Pago()
            .anio(DEFAULT_ANIO)
            .mes(DEFAULT_MES)
            .valor(DEFAULT_VALOR)
            .estado(DEFAULT_ESTADO)
            .fechaGeneracion(DEFAULT_FECHA_GENERACION)
            .fechaPago(DEFAULT_FECHA_PAGO);
        // Add required entity
        Cuota cuota;
        cuota = em.insert(CuotaResourceIT.createEntity(em)).block();
        pago.setCuota(cuota);
        return pago;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pago createUpdatedEntity(EntityManager em) {
        Pago pago = new Pago()
            .anio(UPDATED_ANIO)
            .mes(UPDATED_MES)
            .valor(UPDATED_VALOR)
            .estado(UPDATED_ESTADO)
            .fechaGeneracion(UPDATED_FECHA_GENERACION)
            .fechaPago(UPDATED_FECHA_PAGO);
        // Add required entity
        Cuota cuota;
        cuota = em.insert(CuotaResourceIT.createUpdatedEntity(em)).block();
        pago.setCuota(cuota);
        return pago;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Pago.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
        CuotaResourceIT.deleteEntities(em);
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        pago = createEntity(em);
    }

    @Test
    void createPago() throws Exception {
        int databaseSizeBeforeCreate = pagoRepository.findAll().collectList().block().size();
        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate + 1);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getAnio()).isEqualTo(DEFAULT_ANIO);
        assertThat(testPago.getMes()).isEqualTo(DEFAULT_MES);
        assertThat(testPago.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testPago.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPago.getFechaGeneracion()).isEqualTo(DEFAULT_FECHA_GENERACION);
        assertThat(testPago.getFechaPago()).isEqualTo(DEFAULT_FECHA_PAGO);
    }

    @Test
    void createPagoWithExistingId() throws Exception {
        // Create the Pago with an existing ID
        pago.setId(1L);
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        int databaseSizeBeforeCreate = pagoRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAnioIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().collectList().block().size();
        // set the field null
        pago.setAnio(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkMesIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().collectList().block().size();
        // set the field null
        pago.setMes(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().collectList().block().size();
        // set the field null
        pago.setValor(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().collectList().block().size();
        // set the field null
        pago.setEstado(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaGeneracionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().collectList().block().size();
        // set the field null
        pago.setFechaGeneracion(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkFechaPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().collectList().block().size();
        // set the field null
        pago.setFechaPago(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPagos() {
        // Initialize the database
        pagoRepository.save(pago).block();

        // Get all the pagoList
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
            .value(hasItem(pago.getId().intValue()))
            .jsonPath("$.[*].anio")
            .value(hasItem(DEFAULT_ANIO))
            .jsonPath("$.[*].mes")
            .value(hasItem(DEFAULT_MES))
            .jsonPath("$.[*].valor")
            .value(hasItem(DEFAULT_VALOR.doubleValue()))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.toString()))
            .jsonPath("$.[*].fechaGeneracion")
            .value(hasItem(DEFAULT_FECHA_GENERACION.toString()))
            .jsonPath("$.[*].fechaPago")
            .value(hasItem(DEFAULT_FECHA_PAGO.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPagosWithEagerRelationshipsIsEnabled() {
        when(pagoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pagoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPagosWithEagerRelationshipsIsNotEnabled() {
        when(pagoServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(pagoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getPago() {
        // Initialize the database
        pagoRepository.save(pago).block();

        // Get the pago
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, pago.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(pago.getId().intValue()))
            .jsonPath("$.anio")
            .value(is(DEFAULT_ANIO))
            .jsonPath("$.mes")
            .value(is(DEFAULT_MES))
            .jsonPath("$.valor")
            .value(is(DEFAULT_VALOR.doubleValue()))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.toString()))
            .jsonPath("$.fechaGeneracion")
            .value(is(DEFAULT_FECHA_GENERACION.toString()))
            .jsonPath("$.fechaPago")
            .value(is(DEFAULT_FECHA_PAGO.toString()));
    }

    @Test
    void getNonExistingPago() {
        // Get the pago
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPago() throws Exception {
        // Initialize the database
        pagoRepository.save(pago).block();

        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();

        // Update the pago
        Pago updatedPago = pagoRepository.findById(pago.getId()).block();
        updatedPago
            .anio(UPDATED_ANIO)
            .mes(UPDATED_MES)
            .valor(UPDATED_VALOR)
            .estado(UPDATED_ESTADO)
            .fechaGeneracion(UPDATED_FECHA_GENERACION)
            .fechaPago(UPDATED_FECHA_PAGO);
        PagoDTO pagoDTO = pagoMapper.toDto(updatedPago);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pagoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testPago.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testPago.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testPago.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPago.getFechaGeneracion()).isEqualTo(UPDATED_FECHA_GENERACION);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
    }

    @Test
    void putNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, pagoDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.save(pago).block();

        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago.anio(UPDATED_ANIO).mes(UPDATED_MES).fechaPago(UPDATED_FECHA_PAGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPago.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testPago.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testPago.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testPago.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testPago.getFechaGeneracion()).isEqualTo(DEFAULT_FECHA_GENERACION);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
    }

    @Test
    void fullUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.save(pago).block();

        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago
            .anio(UPDATED_ANIO)
            .mes(UPDATED_MES)
            .valor(UPDATED_VALOR)
            .estado(UPDATED_ESTADO)
            .fechaGeneracion(UPDATED_FECHA_GENERACION)
            .fechaPago(UPDATED_FECHA_PAGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPago.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
        Pago testPago = pagoList.get(pagoList.size() - 1);
        assertThat(testPago.getAnio()).isEqualTo(UPDATED_ANIO);
        assertThat(testPago.getMes()).isEqualTo(UPDATED_MES);
        assertThat(testPago.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testPago.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testPago.getFechaGeneracion()).isEqualTo(UPDATED_FECHA_GENERACION);
        assertThat(testPago.getFechaPago()).isEqualTo(UPDATED_FECHA_PAGO);
    }

    @Test
    void patchNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, pagoDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().collectList().block().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(pagoDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePago() {
        // Initialize the database
        pagoRepository.save(pago).block();

        int databaseSizeBeforeDelete = pagoRepository.findAll().collectList().block().size();

        // Delete the pago
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, pago.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Pago> pagoList = pagoRepository.findAll().collectList().block();
        assertThat(pagoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
