package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.SkillType;
import com.drumbitious.backend.repository.SkillTypeRepository;
import com.drumbitious.backend.service.SkillTypeService;
import com.drumbitious.backend.service.dto.SkillTypeDTO;
import com.drumbitious.backend.service.mapper.SkillTypeMapper;
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
 * Integration tests for the {@link SkillTypeResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class SkillTypeResourceIT {

    private static final String DEFAULT_SKILL_TEXT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SKILL_TEXT_KEY = "BBBBBBBBBB";

    @Autowired
    private SkillTypeRepository skillTypeRepository;

    @Autowired
    private SkillTypeMapper skillTypeMapper;

    @Autowired
    private SkillTypeService skillTypeService;

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

    private MockMvc restSkillTypeMockMvc;

    private SkillType skillType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SkillTypeResource skillTypeResource = new SkillTypeResource(skillTypeService);
        this.restSkillTypeMockMvc = MockMvcBuilders.standaloneSetup(skillTypeResource)
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
    public static SkillType createEntity(EntityManager em) {
        SkillType skillType = new SkillType()
            .skillTextKey(DEFAULT_SKILL_TEXT_KEY);
        return skillType;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SkillType createUpdatedEntity(EntityManager em) {
        SkillType skillType = new SkillType()
            .skillTextKey(UPDATED_SKILL_TEXT_KEY);
        return skillType;
    }

    @BeforeEach
    public void initTest() {
        skillType = createEntity(em);
    }

    @Test
    @Transactional
    public void createSkillType() throws Exception {
        int databaseSizeBeforeCreate = skillTypeRepository.findAll().size();

        // Create the SkillType
        SkillTypeDTO skillTypeDTO = skillTypeMapper.toDto(skillType);
        restSkillTypeMockMvc.perform(post("/api/skill-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the SkillType in the database
        List<SkillType> skillTypeList = skillTypeRepository.findAll();
        assertThat(skillTypeList).hasSize(databaseSizeBeforeCreate + 1);
        SkillType testSkillType = skillTypeList.get(skillTypeList.size() - 1);
        assertThat(testSkillType.getSkillTextKey()).isEqualTo(DEFAULT_SKILL_TEXT_KEY);
    }

    @Test
    @Transactional
    public void createSkillTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = skillTypeRepository.findAll().size();

        // Create the SkillType with an existing ID
        skillType.setId(1L);
        SkillTypeDTO skillTypeDTO = skillTypeMapper.toDto(skillType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSkillTypeMockMvc.perform(post("/api/skill-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SkillType in the database
        List<SkillType> skillTypeList = skillTypeRepository.findAll();
        assertThat(skillTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSkillTextKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = skillTypeRepository.findAll().size();
        // set the field null
        skillType.setSkillTextKey(null);

        // Create the SkillType, which fails.
        SkillTypeDTO skillTypeDTO = skillTypeMapper.toDto(skillType);

        restSkillTypeMockMvc.perform(post("/api/skill-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillTypeDTO)))
            .andExpect(status().isBadRequest());

        List<SkillType> skillTypeList = skillTypeRepository.findAll();
        assertThat(skillTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSkillTypes() throws Exception {
        // Initialize the database
        skillTypeRepository.saveAndFlush(skillType);

        // Get all the skillTypeList
        restSkillTypeMockMvc.perform(get("/api/skill-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(skillType.getId().intValue())))
            .andExpect(jsonPath("$.[*].skillTextKey").value(hasItem(DEFAULT_SKILL_TEXT_KEY)));
    }
    
    @Test
    @Transactional
    public void getSkillType() throws Exception {
        // Initialize the database
        skillTypeRepository.saveAndFlush(skillType);

        // Get the skillType
        restSkillTypeMockMvc.perform(get("/api/skill-types/{id}", skillType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(skillType.getId().intValue()))
            .andExpect(jsonPath("$.skillTextKey").value(DEFAULT_SKILL_TEXT_KEY));
    }

    @Test
    @Transactional
    public void getNonExistingSkillType() throws Exception {
        // Get the skillType
        restSkillTypeMockMvc.perform(get("/api/skill-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSkillType() throws Exception {
        // Initialize the database
        skillTypeRepository.saveAndFlush(skillType);

        int databaseSizeBeforeUpdate = skillTypeRepository.findAll().size();

        // Update the skillType
        SkillType updatedSkillType = skillTypeRepository.findById(skillType.getId()).get();
        // Disconnect from session so that the updates on updatedSkillType are not directly saved in db
        em.detach(updatedSkillType);
        updatedSkillType
            .skillTextKey(UPDATED_SKILL_TEXT_KEY);
        SkillTypeDTO skillTypeDTO = skillTypeMapper.toDto(updatedSkillType);

        restSkillTypeMockMvc.perform(put("/api/skill-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillTypeDTO)))
            .andExpect(status().isOk());

        // Validate the SkillType in the database
        List<SkillType> skillTypeList = skillTypeRepository.findAll();
        assertThat(skillTypeList).hasSize(databaseSizeBeforeUpdate);
        SkillType testSkillType = skillTypeList.get(skillTypeList.size() - 1);
        assertThat(testSkillType.getSkillTextKey()).isEqualTo(UPDATED_SKILL_TEXT_KEY);
    }

    @Test
    @Transactional
    public void updateNonExistingSkillType() throws Exception {
        int databaseSizeBeforeUpdate = skillTypeRepository.findAll().size();

        // Create the SkillType
        SkillTypeDTO skillTypeDTO = skillTypeMapper.toDto(skillType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSkillTypeMockMvc.perform(put("/api/skill-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(skillTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SkillType in the database
        List<SkillType> skillTypeList = skillTypeRepository.findAll();
        assertThat(skillTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSkillType() throws Exception {
        // Initialize the database
        skillTypeRepository.saveAndFlush(skillType);

        int databaseSizeBeforeDelete = skillTypeRepository.findAll().size();

        // Delete the skillType
        restSkillTypeMockMvc.perform(delete("/api/skill-types/{id}", skillType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SkillType> skillTypeList = skillTypeRepository.findAll();
        assertThat(skillTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillType.class);
        SkillType skillType1 = new SkillType();
        skillType1.setId(1L);
        SkillType skillType2 = new SkillType();
        skillType2.setId(skillType1.getId());
        assertThat(skillType1).isEqualTo(skillType2);
        skillType2.setId(2L);
        assertThat(skillType1).isNotEqualTo(skillType2);
        skillType1.setId(null);
        assertThat(skillType1).isNotEqualTo(skillType2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SkillTypeDTO.class);
        SkillTypeDTO skillTypeDTO1 = new SkillTypeDTO();
        skillTypeDTO1.setId(1L);
        SkillTypeDTO skillTypeDTO2 = new SkillTypeDTO();
        assertThat(skillTypeDTO1).isNotEqualTo(skillTypeDTO2);
        skillTypeDTO2.setId(skillTypeDTO1.getId());
        assertThat(skillTypeDTO1).isEqualTo(skillTypeDTO2);
        skillTypeDTO2.setId(2L);
        assertThat(skillTypeDTO1).isNotEqualTo(skillTypeDTO2);
        skillTypeDTO1.setId(null);
        assertThat(skillTypeDTO1).isNotEqualTo(skillTypeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(skillTypeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(skillTypeMapper.fromId(null)).isNull();
    }
}
