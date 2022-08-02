package com.payment.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payment.IntegrationTest;
import com.payment.domain.Cuota;
import com.payment.domain.Pago;
import com.payment.domain.enumeration.RegisterState;
import com.payment.repository.PagoRepository;
import com.payment.service.PagoService;
import com.payment.service.dto.PagoDTO;
import com.payment.service.mapper.PagoMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PagoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
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
    private MockMvc restPagoMockMvc;

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
        if (TestUtil.findAll(em, Cuota.class).isEmpty()) {
            cuota = CuotaResourceIT.createEntity(em);
            em.persist(cuota);
            em.flush();
        } else {
            cuota = TestUtil.findAll(em, Cuota.class).get(0);
        }
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
        if (TestUtil.findAll(em, Cuota.class).isEmpty()) {
            cuota = CuotaResourceIT.createUpdatedEntity(em);
            em.persist(cuota);
            em.flush();
        } else {
            cuota = TestUtil.findAll(em, Cuota.class).get(0);
        }
        pago.setCuota(cuota);
        return pago;
    }

    @BeforeEach
    public void initTest() {
        pago = createEntity(em);
    }

    @Test
    @Transactional
    void createPago() throws Exception {
        int databaseSizeBeforeCreate = pagoRepository.findAll().size();
        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isCreated());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
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
    @Transactional
    void createPagoWithExistingId() throws Exception {
        // Create the Pago with an existing ID
        pago.setId(1L);
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        int databaseSizeBeforeCreate = pagoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnioIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setAnio(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMesIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setMes(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setValor(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setEstado(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaGeneracionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setFechaGeneracion(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaPagoIsRequired() throws Exception {
        int databaseSizeBeforeTest = pagoRepository.findAll().size();
        // set the field null
        pago.setFechaPago(null);

        // Create the Pago, which fails.
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        restPagoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isBadRequest());

        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPagos() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get all the pagoList
        restPagoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pago.getId().intValue())))
            .andExpect(jsonPath("$.[*].anio").value(hasItem(DEFAULT_ANIO)))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].fechaGeneracion").value(hasItem(DEFAULT_FECHA_GENERACION.toString())))
            .andExpect(jsonPath("$.[*].fechaPago").value(hasItem(DEFAULT_FECHA_PAGO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPagosWithEagerRelationshipsIsEnabled() throws Exception {
        when(pagoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPagoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pagoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPagosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pagoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPagoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pagoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        // Get the pago
        restPagoMockMvc
            .perform(get(ENTITY_API_URL_ID, pago.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pago.getId().intValue()))
            .andExpect(jsonPath("$.anio").value(DEFAULT_ANIO))
            .andExpect(jsonPath("$.mes").value(DEFAULT_MES))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.fechaGeneracion").value(DEFAULT_FECHA_GENERACION.toString()))
            .andExpect(jsonPath("$.fechaPago").value(DEFAULT_FECHA_PAGO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPago() throws Exception {
        // Get the pago
        restPagoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago
        Pago updatedPago = pagoRepository.findById(pago.getId()).get();
        // Disconnect from session so that the updates on updatedPago are not directly saved in db
        em.detach(updatedPago);
        updatedPago
            .anio(UPDATED_ANIO)
            .mes(UPDATED_MES)
            .valor(UPDATED_VALOR)
            .estado(UPDATED_ESTADO)
            .fechaGeneracion(UPDATED_FECHA_GENERACION)
            .fechaPago(UPDATED_FECHA_PAGO);
        PagoDTO pagoDTO = pagoMapper.toDto(updatedPago);

        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
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
    @Transactional
    void putNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pagoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

        // Update the pago using partial update
        Pago partialUpdatedPago = new Pago();
        partialUpdatedPago.setId(pago.getId());

        partialUpdatedPago.anio(UPDATED_ANIO).mes(UPDATED_MES).fechaPago(UPDATED_FECHA_PAGO);

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
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
    @Transactional
    void fullUpdatePagoWithPatch() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();

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

        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPago.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPago))
            )
            .andExpect(status().isOk());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
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
    @Transactional
    void patchNonExistingPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pagoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pagoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPago() throws Exception {
        int databaseSizeBeforeUpdate = pagoRepository.findAll().size();
        pago.setId(count.incrementAndGet());

        // Create the Pago
        PagoDTO pagoDTO = pagoMapper.toDto(pago);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPagoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pagoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pago in the database
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePago() throws Exception {
        // Initialize the database
        pagoRepository.saveAndFlush(pago);

        int databaseSizeBeforeDelete = pagoRepository.findAll().size();

        // Delete the pago
        restPagoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pago.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pago> pagoList = pagoRepository.findAll();
        assertThat(pagoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
