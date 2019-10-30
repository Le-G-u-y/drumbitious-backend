package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.ExcerciseType;
import com.drumbitious.backend.repository.ExcerciseTypeRepository;
import com.drumbitious.backend.service.ExcerciseTypeService;
import com.drumbitious.backend.service.dto.ExcerciseTypeDTO;
import com.drumbitious.backend.service.mapper.ExcerciseTypeMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static com.drumbitious.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExcerciseTypeResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class ExcerciseTypeResourceIT {

    private static final String DEFAULT_EXCERCISE_TYPE_TEXT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_EXCERCISE_TYPE_TEXT_KEY = "BBBBBBBBBB";

    @Autowired
    private ExcerciseTypeRepository excerciseTypeRepository;

    @Autowired
    private ExcerciseTypeMapper excerciseTypeMapper;

    @Autowired
    private ExcerciseTypeService excerciseTypeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restExcerciseTypeMockMvc;

    private ExcerciseType excerciseType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExcerciseTypeResource excerciseTypeResource = new ExcerciseTypeResource(excerciseTypeService);
        this.restExcerciseTypeMockMvc = MockMvcBuilders.standaloneSetup(excerciseTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcerciseType createEntity(EntityManager em) {
        ExcerciseType excerciseType = new ExcerciseType()
            .excerciseTypeTextKey(DEFAULT_EXCERCISE_TYPE_TEXT_KEY);
        return excerciseType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcerciseType createUpdatedEntity(EntityManager em) {
        ExcerciseType excerciseType = new ExcerciseType()
            .excerciseTypeTextKey(UPDATED_EXCERCISE_TYPE_TEXT_KEY);
        return excerciseType;
    }

    @BeforeEach
    public void initTest() {
        excerciseType = createEntity(em);
    }

    @Test
    @Transactional
    public void createExcerciseType() throws Exception {
        int databaseSizeBeforeCreate = excerciseTypeRepository.findAll().size();

        // Create the ExcerciseType
        ExcerciseTypeDTO excerciseTypeDTO = excerciseTypeMapper.toDto(excerciseType);
        restExcerciseTypeMockMvc.perform(post("/api/excercise-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the ExcerciseType in the database
        List<ExcerciseType> excerciseTypeList = excerciseTypeRepository.findAll();
        assertThat(excerciseTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ExcerciseType testExcerciseType = excerciseTypeList.get(excerciseTypeList.size() - 1);
        assertThat(testExcerciseType.getExcerciseTypeTextKey()).isEqualTo(DEFAULT_EXCERCISE_TYPE_TEXT_KEY);
    }

    @Test
    @Transactional
    public void createExcerciseTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = excerciseTypeRepository.findAll().size();

        // Create the ExcerciseType with an existing ID
        excerciseType.setId(1L);
        ExcerciseTypeDTO excerciseTypeDTO = excerciseTypeMapper.toDto(excerciseType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcerciseTypeMockMvc.perform(post("/api/excercise-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExcerciseType in the database
        List<ExcerciseType> excerciseTypeList = excerciseTypeRepository.findAll();
        assertThat(excerciseTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkExcerciseTypeTextKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = excerciseTypeRepository.findAll().size();
        // set the field null
        excerciseType.setExcerciseTypeTextKey(null);

        // Create the ExcerciseType, which fails.
        ExcerciseTypeDTO excerciseTypeDTO = excerciseTypeMapper.toDto(excerciseType);

        restExcerciseTypeMockMvc.perform(post("/api/excercise-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseTypeDTO)))
            .andExpect(status().isBadRequest());

        List<ExcerciseType> excerciseTypeList = excerciseTypeRepository.findAll();
        assertThat(excerciseTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExcerciseTypes() throws Exception {
        // Initialize the database
        excerciseTypeRepository.saveAndFlush(excerciseType);

        // Get all the excerciseTypeList
        restExcerciseTypeMockMvc.perform(get("/api/excercise-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excerciseType.getId().intValue())))
            .andExpect(jsonPath("$.[*].excerciseTypeTextKey").value(hasItem(DEFAULT_EXCERCISE_TYPE_TEXT_KEY)));
    }
    
    @Test
    @Transactional
    public void getExcerciseType() throws Exception {
        // Initialize the database
        excerciseTypeRepository.saveAndFlush(excerciseType);

        // Get the excerciseType
        restExcerciseTypeMockMvc.perform(get("/api/excercise-types/{id}", excerciseType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(excerciseType.getId().intValue()))
            .andExpect(jsonPath("$.excerciseTypeTextKey").value(DEFAULT_EXCERCISE_TYPE_TEXT_KEY));
    }

    @Test
    @Transactional
    public void getNonExistingExcerciseType() throws Exception {
        // Get the excerciseType
        restExcerciseTypeMockMvc.perform(get("/api/excercise-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExcerciseType() throws Exception {
        // Initialize the database
        excerciseTypeRepository.saveAndFlush(excerciseType);

        int databaseSizeBeforeUpdate = excerciseTypeRepository.findAll().size();

        // Update the excerciseType
        ExcerciseType updatedExcerciseType = excerciseTypeRepository.findById(excerciseType.getId()).get();
        // Disconnect from session so that the updates on updatedExcerciseType are not directly saved in db
        em.detach(updatedExcerciseType);
        updatedExcerciseType
            .excerciseTypeTextKey(UPDATED_EXCERCISE_TYPE_TEXT_KEY);
        ExcerciseTypeDTO excerciseTypeDTO = excerciseTypeMapper.toDto(updatedExcerciseType);

        restExcerciseTypeMockMvc.perform(put("/api/excercise-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseTypeDTO)))
            .andExpect(status().isOk());

        // Validate the ExcerciseType in the database
        List<ExcerciseType> excerciseTypeList = excerciseTypeRepository.findAll();
        assertThat(excerciseTypeList).hasSize(databaseSizeBeforeUpdate);
        ExcerciseType testExcerciseType = excerciseTypeList.get(excerciseTypeList.size() - 1);
        assertThat(testExcerciseType.getExcerciseTypeTextKey()).isEqualTo(UPDATED_EXCERCISE_TYPE_TEXT_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingExcerciseType() throws Exception {
        int databaseSizeBeforeUpdate = excerciseTypeRepository.findAll().size();

        // Create the ExcerciseType
        ExcerciseTypeDTO excerciseTypeDTO = excerciseTypeMapper.toDto(excerciseType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseTypeMockMvc.perform(put("/api/excercise-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExcerciseType in the database
        List<ExcerciseType> excerciseTypeList = excerciseTypeRepository.findAll();
        assertThat(excerciseTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExcerciseType() throws Exception {
        // Initialize the database
        excerciseTypeRepository.saveAndFlush(excerciseType);

        int databaseSizeBeforeDelete = excerciseTypeRepository.findAll().size();

        // Delete the excerciseType
        restExcerciseTypeMockMvc.perform(delete("/api/excercise-types/{id}", excerciseType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExcerciseType> excerciseTypeList = excerciseTypeRepository.findAll();
        assertThat(excerciseTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcerciseType.class);
        ExcerciseType excerciseType1 = new ExcerciseType();
        excerciseType1.setId(1L);
        ExcerciseType excerciseType2 = new ExcerciseType();
        excerciseType2.setId(excerciseType1.getId());
        assertThat(excerciseType1).isEqualTo(excerciseType2);
        excerciseType2.setId(2L);
        assertThat(excerciseType1).isNotEqualTo(excerciseType2);
        excerciseType1.setId(null);
        assertThat(excerciseType1).isNotEqualTo(excerciseType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcerciseTypeDTO.class);
        ExcerciseTypeDTO excerciseTypeDTO1 = new ExcerciseTypeDTO();
        excerciseTypeDTO1.setId(1L);
        ExcerciseTypeDTO excerciseTypeDTO2 = new ExcerciseTypeDTO();
        assertThat(excerciseTypeDTO1).isNotEqualTo(excerciseTypeDTO2);
        excerciseTypeDTO2.setId(excerciseTypeDTO1.getId());
        assertThat(excerciseTypeDTO1).isEqualTo(excerciseTypeDTO2);
        excerciseTypeDTO2.setId(2L);
        assertThat(excerciseTypeDTO1).isNotEqualTo(excerciseTypeDTO2);
        excerciseTypeDTO1.setId(null);
        assertThat(excerciseTypeDTO1).isNotEqualTo(excerciseTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(excerciseTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(excerciseTypeMapper.fromId(null)).isNull();
    }
}
