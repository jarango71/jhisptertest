package com.condominium.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.condominium.IntegrationTest;
import com.condominium.domain.Condominium;
import com.condominium.repository.CondominiumRepository;
import com.condominium.repository.EntityManager;
import com.condominium.service.dto.CondominiumDTO;
import com.condominium.service.mapper.CondominiumMapper;
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
 * Integration tests for the {@link CondominiumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
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
    private WebTestClient webTestClient;

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

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Condominium.class).block();
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
        condominium = createEntity(em);
    }

    @Test
    void createCondominium() throws Exception {
        int databaseSizeBeforeCreate = condominiumRepository.findAll().collectList().block().size();
        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
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
    void createCondominiumWithExistingId() throws Exception {
        // Create the Condominium with an existing ID
        condominium.setId(1L);
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        int databaseSizeBeforeCreate = condominiumRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().collectList().block().size();
        // set the field null
        condominium.setNombre(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDireccionIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().collectList().block().size();
        // set the field null
        condominium.setDireccion(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = condominiumRepository.findAll().collectList().block().size();
        // set the field null
        condominium.setEstado(null);

        // Create the Condominium, which fails.
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllCondominiums() {
        // Initialize the database
        condominiumRepository.save(condominium).block();

        // Get all the condominiumList
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
            .value(hasItem(condominium.getId().intValue()))
            .jsonPath("$.[*].nombre")
            .value(hasItem(DEFAULT_NOMBRE))
            .jsonPath("$.[*].direccion")
            .value(hasItem(DEFAULT_DIRECCION))
            .jsonPath("$.[*].logo")
            .value(hasItem(DEFAULT_LOGO))
            .jsonPath("$.[*].latitud")
            .value(hasItem(DEFAULT_LATITUD.doubleValue()))
            .jsonPath("$.[*].longitud")
            .value(hasItem(DEFAULT_LONGITUD.doubleValue()))
            .jsonPath("$.[*].estado")
            .value(hasItem(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    void getCondominium() {
        // Initialize the database
        condominiumRepository.save(condominium).block();

        // Get the condominium
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, condominium.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(condominium.getId().intValue()))
            .jsonPath("$.nombre")
            .value(is(DEFAULT_NOMBRE))
            .jsonPath("$.direccion")
            .value(is(DEFAULT_DIRECCION))
            .jsonPath("$.logo")
            .value(is(DEFAULT_LOGO))
            .jsonPath("$.latitud")
            .value(is(DEFAULT_LATITUD.doubleValue()))
            .jsonPath("$.longitud")
            .value(is(DEFAULT_LONGITUD.doubleValue()))
            .jsonPath("$.estado")
            .value(is(DEFAULT_ESTADO.booleanValue()));
    }

    @Test
    void getNonExistingCondominium() {
        // Get the condominium
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewCondominium() throws Exception {
        // Initialize the database
        condominiumRepository.save(condominium).block();

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();

        // Update the condominium
        Condominium updatedCondominium = condominiumRepository.findById(condominium.getId()).block();
        updatedCondominium
            .nombre(UPDATED_NOMBRE)
            .direccion(UPDATED_DIRECCION)
            .logo(UPDATED_LOGO)
            .latitud(UPDATED_LATITUD)
            .longitud(UPDATED_LONGITUD)
            .estado(UPDATED_ESTADO);
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(updatedCondominium);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, condominiumDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
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
    void putNonExistingCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, condominiumDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCondominiumWithPatch() throws Exception {
        // Initialize the database
        condominiumRepository.save(condominium).block();

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();

        // Update the condominium using partial update
        Condominium partialUpdatedCondominium = new Condominium();
        partialUpdatedCondominium.setId(condominium.getId());

        partialUpdatedCondominium.nombre(UPDATED_NOMBRE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCondominium.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCondominium))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
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
    void fullUpdateCondominiumWithPatch() throws Exception {
        // Initialize the database
        condominiumRepository.save(condominium).block();

        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();

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

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCondominium.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedCondominium))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
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
    void patchNonExistingCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, condominiumDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCondominium() throws Exception {
        int databaseSizeBeforeUpdate = condominiumRepository.findAll().collectList().block().size();
        condominium.setId(count.incrementAndGet());

        // Create the Condominium
        CondominiumDTO condominiumDTO = condominiumMapper.toDto(condominium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(condominiumDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Condominium in the database
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCondominium() {
        // Initialize the database
        condominiumRepository.save(condominium).block();

        int databaseSizeBeforeDelete = condominiumRepository.findAll().collectList().block().size();

        // Delete the condominium
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, condominium.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Condominium> condominiumList = condominiumRepository.findAll().collectList().block();
        assertThat(condominiumList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
