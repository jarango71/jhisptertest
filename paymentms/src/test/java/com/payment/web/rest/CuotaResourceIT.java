package com.payment.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.payment.IntegrationTest;
import com.payment.domain.Cuota;
import com.payment.domain.enumeration.CuotaType;
import com.payment.domain.enumeration.RegisterState;
import com.payment.repository.CuotaRepository;
import com.payment.service.dto.CuotaDTO;
import com.payment.service.mapper.CuotaMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CuotaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restCuotaMockMvc;

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

    @BeforeEach
    public void initTest() {
        cuota = createEntity(em);
    }

    @Test
    @Transactional
    void createCuota() throws Exception {
        int databaseSizeBeforeCreate = cuotaRepository.findAll().size();
        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);
        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isCreated());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
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
    @Transactional
    void createCuotaWithExistingId() throws Exception {
        // Create the Cuota with an existing ID
        cuota.setId(1L);
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        int databaseSizeBeforeCreate = cuotaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setNombre(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setTipo(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPeriodicidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setPeriodicidad(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAplicaIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setAplica(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMontoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setMonto(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDiponibilidadIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setDiponibilidad(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkObservacionIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setObservacion(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cuotaRepository.findAll().size();
        // set the field null
        cuota.setEstado(null);

        // Create the Cuota, which fails.
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        restCuotaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isBadRequest());

        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCuotas() throws Exception {
        // Initialize the database
        cuotaRepository.saveAndFlush(cuota);

        // Get all the cuotaList
        restCuotaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cuota.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].periodicidad").value(hasItem(DEFAULT_PERIODICIDAD)))
            .andExpect(jsonPath("$.[*].aplica").value(hasItem(DEFAULT_APLICA)))
            .andExpect(jsonPath("$.[*].monto").value(hasItem(DEFAULT_MONTO.doubleValue())))
            .andExpect(jsonPath("$.[*].diponibilidad").value(hasItem(DEFAULT_DIPONIBILIDAD)))
            .andExpect(jsonPath("$.[*].observacion").value(hasItem(DEFAULT_OBSERVACION)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getCuota() throws Exception {
        // Initialize the database
        cuotaRepository.saveAndFlush(cuota);

        // Get the cuota
        restCuotaMockMvc
            .perform(get(ENTITY_API_URL_ID, cuota.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cuota.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.periodicidad").value(DEFAULT_PERIODICIDAD))
            .andExpect(jsonPath("$.aplica").value(DEFAULT_APLICA))
            .andExpect(jsonPath("$.monto").value(DEFAULT_MONTO.doubleValue()))
            .andExpect(jsonPath("$.diponibilidad").value(DEFAULT_DIPONIBILIDAD))
            .andExpect(jsonPath("$.observacion").value(DEFAULT_OBSERVACION))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCuota() throws Exception {
        // Get the cuota
        restCuotaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCuota() throws Exception {
        // Initialize the database
        cuotaRepository.saveAndFlush(cuota);

        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();

        // Update the cuota
        Cuota updatedCuota = cuotaRepository.findById(cuota.getId()).get();
        // Disconnect from session so that the updates on updatedCuota are not directly saved in db
        em.detach(updatedCuota);
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

        restCuotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuotaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
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
    @Transactional
    void putNonExistingCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cuotaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuotaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuotaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCuotaWithPatch() throws Exception {
        // Initialize the database
        cuotaRepository.saveAndFlush(cuota);

        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();

        // Update the cuota using partial update
        Cuota partialUpdatedCuota = new Cuota();
        partialUpdatedCuota.setId(cuota.getId());

        partialUpdatedCuota.nombre(UPDATED_NOMBRE).tipo(UPDATED_TIPO).diponibilidad(UPDATED_DIPONIBILIDAD).estado(UPDATED_ESTADO);

        restCuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuota.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuota))
            )
            .andExpect(status().isOk());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
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
    @Transactional
    void fullUpdateCuotaWithPatch() throws Exception {
        // Initialize the database
        cuotaRepository.saveAndFlush(cuota);

        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();

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

        restCuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCuota.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCuota))
            )
            .andExpect(status().isOk());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
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
    @Transactional
    void patchNonExistingCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cuotaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuotaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cuotaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCuota() throws Exception {
        int databaseSizeBeforeUpdate = cuotaRepository.findAll().size();
        cuota.setId(count.incrementAndGet());

        // Create the Cuota
        CuotaDTO cuotaDTO = cuotaMapper.toDto(cuota);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCuotaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cuotaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cuota in the database
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCuota() throws Exception {
        // Initialize the database
        cuotaRepository.saveAndFlush(cuota);

        int databaseSizeBeforeDelete = cuotaRepository.findAll().size();

        // Delete the cuota
        restCuotaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cuota.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cuota> cuotaList = cuotaRepository.findAll();
        assertThat(cuotaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
