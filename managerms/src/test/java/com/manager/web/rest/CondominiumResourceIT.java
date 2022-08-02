package com.manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.manager.IntegrationTest;
import com.manager.domain.Condominium;
import com.manager.repository.CondominiumRepository;
import com.manager.service.dto.CondominiumDTO;
import com.manager.service.mapper.CondominiumMapper;
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
 * Integration tests for the {@link CondominiumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CondominiumResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECCION = "AAAAAAAAAA";
    private static final String UPDATED_DIRECCION = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO = "AAAAAAAAAA";
    private static final String UPDATED_LOGO = "BBBBBBBBBB";

    private static final Double DEFAULT_LATITUD = 1D;
    private static final Double UPDATED_LATITUD = 2D;

    private static final Double DEFAULT_LONGITUD = 1D;
    private static final Double UPDATED_LONGITUD = 2D;

    private static final Boolean DEFAULT_ESTADO = false;
    private static final Boolean UPDATED_ESTADO = true;

    private static final String ENTITY_API_URL = "/api/condominiums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CondominiumRepository condominiumRepository;

    @Autowired
    private CondominiumMapper condominiumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCondominiumMockMvc;

    private Condominium condominium;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Condominium createEntity(EntityManager em) {
        Condominium condominium = new Condominium()
            .nombre(DEFAULT_NOMBRE)
            .direccion(DEFAULT_DIRECCION)
            .logo(DEFAULT_LOGO)
            .latitud(DEFAULT_LATITUD)
            .longitud(DEFAULT_LONGITUD)
            .estado(DEFAULT_ESTADO);
        return condominium;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Condominium createUpdatedEntity(EntityManager em) {
        Condominium condominium = new Condominium()
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .logo(UPDATED_LOGO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .estado(UPDATED_ESTADO);
        return condominium;
    }

    @BeforeEach
    public void initTest() {
        condominium = createEntity(em);
    }

    @Test
    @Transactional
    void createCondominium() throws Exception {
        int databaseSizeBeforeCreate = condominiumRepository.findAll().size();
        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);
        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeCreate + 1);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testCondominium.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testCondominium.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCondominium.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testCondominium.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testCondominium.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createCondominiumWithExistingId() throws Exception {
        // Create the Condominium with an existing ID
        condominium.setId(1L);
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        int databaseSizeBeforeCreate = condominiumRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().size();
        // set the field null
        condominium.setNombre(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().size();
        // set the field null
        condominium.setDireccion(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().size();
        // set the field null
        condominium.setEstado(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        restCondominiumMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCondominiums() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        // Get all the condominiumList
        restCondominiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(condominium.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].direccion").value(hasItem(DEFAULT_DIRECCION)))
            .andExpect(jsonPath("$.[*].logo").value(hasItem(DEFAULT_LOGO)))
            .andExpect(jsonPath("$.[*].latitud").value(hasItem(DEFAULT_LATITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].longitud").value(hasItem(DEFAULT_LONGITUD.doubleValue())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.booleanValue())));
    }

    @Test
    @Transactional
    void getCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        // Get the condominium
        restCondominiumMockMvc
            .perform(get(ENTITY_API_URL_ID, condominium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(condominium.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.direccion").value(DEFAULT_DIRECCION))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO))
            .andExpect(jsonPath("$.latitud").value(DEFAULT_LATITUD.doubleValue()))
            .andExpect(jsonPath("$.longitud").value(DEFAULT_LONGITUD.doubleValue()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCondominium() throws Exception {
        // Get the condominium
        restCondominiumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();

        // Update the condominium
        Condominium updatedCondominium = condominiumRepository.findById(condominium.getId()).get();
        // Disconnect from session so that the updates on updatedCondominium are not directly saved in db
        em.detach(updatedCondominium);
        updatedCondominium
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .logo(UPDATED_LOGO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .estado(UPDATED_ESTADO);
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(updatedCondominium);

        restCondominiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, condominiumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCondominium.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testCondominium.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCondominium.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testCondominium.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testCondominium.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, condominiumDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(condominiumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCondominiumWithPatch() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();

        // Update the condominium using partial update
        Condominium partialUpdatedCondominium = new Condominium();
        partialUpdatedCondominium.setId(condominium.getId());

        partialUpdatedCondominium.nombre(UPDATED_NOMBRE);

        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCondominium.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCondominium))
            )
            .andExpect(status().isOk());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCondominium.getDireccion()).isEqualTo(DEFAULT_DIRECCION);
        assertThat(testCondominium.getLogo()).isEqualTo(DEFAULT_LOGO);
        assertThat(testCondominium.getLatitud()).isEqualTo(DEFAULT_LATITUD);
        assertThat(testCondominium.getLongitud()).isEqualTo(DEFAULT_LONGITUD);
        assertThat(testCondominium.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateCondominiumWithPatch() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();

        // Update the condominium using partial update
        Condominium partialUpdatedCondominium = new Condominium();
        partialUpdatedCondominium.setId(condominium.getId());

        partialUpdatedCondominium
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .logo(UPDATED_LOGO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .estado(UPDATED_ESTADO);

        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCondominium.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCondominium))
            )
            .andExpect(status().isOk());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
        Condominium testCondominium = condominiumList.get(condominiumList.size() - 1);
        assertThat(testCondominium.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testCondominium.getDireccion()).isEqualTo(UPDATED_DIRECCION);
        assertThat(testCondominium.getLogo()).isEqualTo(UPDATED_LOGO);
        assertThat(testCondominium.getLatitud()).isEqualTo(UPDATED_LATITUD);
        assertThat(testCondominium.getLongitud()).isEqualTo(UPDATED_LONGITUD);
        assertThat(testCondominium.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, condominiumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCondominiumMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.saveAndFlush(condominium);

        int databaseSizeBeforeDelete = condominiumRepository.findAll().size();

        // Delete the condominium
        restCondominiumMockMvc
            .perform(delete(ENTITY_API_URL_ID, condominium.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Condominium> condominiumList = condominiumRepository.findAll();
        assertThat(condominiumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
