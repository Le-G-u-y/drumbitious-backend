package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.ExerciseConfig;
import com.drumbitious.backend.domain.Plan;
import com.drumbitious.backend.domain.Exercise;
import com.drumbitious.backend.repository.ExerciseConfigRepository;
import com.drumbitious.backend.service.ExerciseConfigService;
import com.drumbitious.backend.service.dto.ExerciseConfigDTO;
import com.drumbitious.backend.service.mapper.ExerciseConfigMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.ExerciseConfigCriteria;
import com.drumbitious.backend.service.ExerciseConfigQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.drumbitious.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExerciseConfigResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class ExerciseConfigResourceIT {

    private static final Integer DEFAULT_PRACTICE_BPM = 1;
    private static final Integer UPDATED_PRACTICE_BPM = 2;
    private static final Integer SMALLER_PRACTICE_BPM = 1 - 1;

    private static final Integer DEFAULT_TARGET_BPM = 1;
    private static final Integer UPDATED_TARGET_BPM = 2;
    private static final Integer SMALLER_TARGET_BPM = 1 - 1;

    private static final Integer DEFAULT_MINUTES = 1;
    private static final Integer UPDATED_MINUTES = 2;
    private static final Integer SMALLER_MINUTES = 1 - 1;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ExerciseConfigRepository exerciseConfigRepository;

    @Autowired
    private ExerciseConfigMapper exerciseConfigMapper;

    @Autowired
    private ExerciseConfigService exerciseConfigService;

    @Autowired
    private ExerciseConfigQueryService exerciseConfigQueryService;

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

    private MockMvc restExerciseConfigMockMvc;

    private ExerciseConfig exerciseConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExerciseConfigResource exerciseConfigResource = new ExerciseConfigResource(exerciseConfigService, exerciseConfigQueryService);
        this.restExerciseConfigMockMvc = MockMvcBuilders.standaloneSetup(exerciseConfigResource)
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
    public static ExerciseConfig createEntity(EntityManager em) {
        ExerciseConfig exerciseConfig = new ExerciseConfig()
            .practiceBpm(DEFAULT_PRACTICE_BPM)
            .targetBpm(DEFAULT_TARGET_BPM)
            .minutes(DEFAULT_MINUTES)
            .note(DEFAULT_NOTE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return exerciseConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExerciseConfig createUpdatedEntity(EntityManager em) {
        ExerciseConfig exerciseConfig = new ExerciseConfig()
            .practiceBpm(UPDATED_PRACTICE_BPM)
            .targetBpm(UPDATED_TARGET_BPM)
            .minutes(UPDATED_MINUTES)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return exerciseConfig;
    }

    @BeforeEach
    public void initTest() {
        exerciseConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createExerciseConfig() throws Exception {
        int databaseSizeBeforeCreate = exerciseConfigRepository.findAll().size();

        // Create the ExerciseConfig
        ExerciseConfigDTO exerciseConfigDTO = exerciseConfigMapper.toDto(exerciseConfig);
        restExerciseConfigMockMvc.perform(post("/api/exercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the ExerciseConfig in the database
        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeCreate + 1);
        ExerciseConfig testExerciseConfig = exerciseConfigList.get(exerciseConfigList.size() - 1);
        assertThat(testExerciseConfig.getPracticeBpm()).isEqualTo(DEFAULT_PRACTICE_BPM);
        assertThat(testExerciseConfig.getTargetBpm()).isEqualTo(DEFAULT_TARGET_BPM);
        assertThat(testExerciseConfig.getMinutes()).isEqualTo(DEFAULT_MINUTES);
        assertThat(testExerciseConfig.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testExerciseConfig.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testExerciseConfig.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createExerciseConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exerciseConfigRepository.findAll().size();

        // Create the ExerciseConfig with an existing ID
        exerciseConfig.setId(1L);
        ExerciseConfigDTO exerciseConfigDTO = exerciseConfigMapper.toDto(exerciseConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseConfigMockMvc.perform(post("/api/exercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExerciseConfig in the database
        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseConfigRepository.findAll().size();
        // set the field null
        exerciseConfig.setCreateDate(null);

        // Create the ExerciseConfig, which fails.
        ExerciseConfigDTO exerciseConfigDTO = exerciseConfigMapper.toDto(exerciseConfig);

        restExerciseConfigMockMvc.perform(post("/api/exercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseConfigRepository.findAll().size();
        // set the field null
        exerciseConfig.setModifyDate(null);

        // Create the ExerciseConfig, which fails.
        ExerciseConfigDTO exerciseConfigDTO = exerciseConfigMapper.toDto(exerciseConfig);

        restExerciseConfigMockMvc.perform(post("/api/exercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigs() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].practiceBpm").value(hasItem(DEFAULT_PRACTICE_BPM)))
            .andExpect(jsonPath("$.[*].targetBpm").value(hasItem(DEFAULT_TARGET_BPM)))
            .andExpect(jsonPath("$.[*].minutes").value(hasItem(DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getExerciseConfig() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get the exerciseConfig
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs/{id}", exerciseConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exerciseConfig.getId().intValue()))
            .andExpect(jsonPath("$.practiceBpm").value(DEFAULT_PRACTICE_BPM))
            .andExpect(jsonPath("$.targetBpm").value(DEFAULT_TARGET_BPM))
            .andExpect(jsonPath("$.minutes").value(DEFAULT_MINUTES))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm equals to DEFAULT_PRACTICE_BPM
        defaultExerciseConfigShouldBeFound("practiceBpm.equals=" + DEFAULT_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm equals to UPDATED_PRACTICE_BPM
        defaultExerciseConfigShouldNotBeFound("practiceBpm.equals=" + UPDATED_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm not equals to DEFAULT_PRACTICE_BPM
        defaultExerciseConfigShouldNotBeFound("practiceBpm.notEquals=" + DEFAULT_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm not equals to UPDATED_PRACTICE_BPM
        defaultExerciseConfigShouldBeFound("practiceBpm.notEquals=" + UPDATED_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm in DEFAULT_PRACTICE_BPM or UPDATED_PRACTICE_BPM
        defaultExerciseConfigShouldBeFound("practiceBpm.in=" + DEFAULT_PRACTICE_BPM + "," + UPDATED_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm equals to UPDATED_PRACTICE_BPM
        defaultExerciseConfigShouldNotBeFound("practiceBpm.in=" + UPDATED_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm is not null
        defaultExerciseConfigShouldBeFound("practiceBpm.specified=true");

        // Get all the exerciseConfigList where practiceBpm is null
        defaultExerciseConfigShouldNotBeFound("practiceBpm.specified=false");
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm is greater than or equal to DEFAULT_PRACTICE_BPM
        defaultExerciseConfigShouldBeFound("practiceBpm.greaterThanOrEqual=" + DEFAULT_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm is greater than or equal to (DEFAULT_PRACTICE_BPM + 1)
        defaultExerciseConfigShouldNotBeFound("practiceBpm.greaterThanOrEqual=" + (DEFAULT_PRACTICE_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm is less than or equal to DEFAULT_PRACTICE_BPM
        defaultExerciseConfigShouldBeFound("practiceBpm.lessThanOrEqual=" + DEFAULT_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm is less than or equal to SMALLER_PRACTICE_BPM
        defaultExerciseConfigShouldNotBeFound("practiceBpm.lessThanOrEqual=" + SMALLER_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm is less than DEFAULT_PRACTICE_BPM
        defaultExerciseConfigShouldNotBeFound("practiceBpm.lessThan=" + DEFAULT_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm is less than (DEFAULT_PRACTICE_BPM + 1)
        defaultExerciseConfigShouldBeFound("practiceBpm.lessThan=" + (DEFAULT_PRACTICE_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPracticeBpmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where practiceBpm is greater than DEFAULT_PRACTICE_BPM
        defaultExerciseConfigShouldNotBeFound("practiceBpm.greaterThan=" + DEFAULT_PRACTICE_BPM);

        // Get all the exerciseConfigList where practiceBpm is greater than SMALLER_PRACTICE_BPM
        defaultExerciseConfigShouldBeFound("practiceBpm.greaterThan=" + SMALLER_PRACTICE_BPM);
    }


    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm equals to DEFAULT_TARGET_BPM
        defaultExerciseConfigShouldBeFound("targetBpm.equals=" + DEFAULT_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm equals to UPDATED_TARGET_BPM
        defaultExerciseConfigShouldNotBeFound("targetBpm.equals=" + UPDATED_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm not equals to DEFAULT_TARGET_BPM
        defaultExerciseConfigShouldNotBeFound("targetBpm.notEquals=" + DEFAULT_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm not equals to UPDATED_TARGET_BPM
        defaultExerciseConfigShouldBeFound("targetBpm.notEquals=" + UPDATED_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm in DEFAULT_TARGET_BPM or UPDATED_TARGET_BPM
        defaultExerciseConfigShouldBeFound("targetBpm.in=" + DEFAULT_TARGET_BPM + "," + UPDATED_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm equals to UPDATED_TARGET_BPM
        defaultExerciseConfigShouldNotBeFound("targetBpm.in=" + UPDATED_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm is not null
        defaultExerciseConfigShouldBeFound("targetBpm.specified=true");

        // Get all the exerciseConfigList where targetBpm is null
        defaultExerciseConfigShouldNotBeFound("targetBpm.specified=false");
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm is greater than or equal to DEFAULT_TARGET_BPM
        defaultExerciseConfigShouldBeFound("targetBpm.greaterThanOrEqual=" + DEFAULT_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm is greater than or equal to (DEFAULT_TARGET_BPM + 1)
        defaultExerciseConfigShouldNotBeFound("targetBpm.greaterThanOrEqual=" + (DEFAULT_TARGET_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm is less than or equal to DEFAULT_TARGET_BPM
        defaultExerciseConfigShouldBeFound("targetBpm.lessThanOrEqual=" + DEFAULT_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm is less than or equal to SMALLER_TARGET_BPM
        defaultExerciseConfigShouldNotBeFound("targetBpm.lessThanOrEqual=" + SMALLER_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm is less than DEFAULT_TARGET_BPM
        defaultExerciseConfigShouldNotBeFound("targetBpm.lessThan=" + DEFAULT_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm is less than (DEFAULT_TARGET_BPM + 1)
        defaultExerciseConfigShouldBeFound("targetBpm.lessThan=" + (DEFAULT_TARGET_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByTargetBpmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where targetBpm is greater than DEFAULT_TARGET_BPM
        defaultExerciseConfigShouldNotBeFound("targetBpm.greaterThan=" + DEFAULT_TARGET_BPM);

        // Get all the exerciseConfigList where targetBpm is greater than SMALLER_TARGET_BPM
        defaultExerciseConfigShouldBeFound("targetBpm.greaterThan=" + SMALLER_TARGET_BPM);
    }


    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes equals to DEFAULT_MINUTES
        defaultExerciseConfigShouldBeFound("minutes.equals=" + DEFAULT_MINUTES);

        // Get all the exerciseConfigList where minutes equals to UPDATED_MINUTES
        defaultExerciseConfigShouldNotBeFound("minutes.equals=" + UPDATED_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes not equals to DEFAULT_MINUTES
        defaultExerciseConfigShouldNotBeFound("minutes.notEquals=" + DEFAULT_MINUTES);

        // Get all the exerciseConfigList where minutes not equals to UPDATED_MINUTES
        defaultExerciseConfigShouldBeFound("minutes.notEquals=" + UPDATED_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes in DEFAULT_MINUTES or UPDATED_MINUTES
        defaultExerciseConfigShouldBeFound("minutes.in=" + DEFAULT_MINUTES + "," + UPDATED_MINUTES);

        // Get all the exerciseConfigList where minutes equals to UPDATED_MINUTES
        defaultExerciseConfigShouldNotBeFound("minutes.in=" + UPDATED_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes is not null
        defaultExerciseConfigShouldBeFound("minutes.specified=true");

        // Get all the exerciseConfigList where minutes is null
        defaultExerciseConfigShouldNotBeFound("minutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes is greater than or equal to DEFAULT_MINUTES
        defaultExerciseConfigShouldBeFound("minutes.greaterThanOrEqual=" + DEFAULT_MINUTES);

        // Get all the exerciseConfigList where minutes is greater than or equal to (DEFAULT_MINUTES + 1)
        defaultExerciseConfigShouldNotBeFound("minutes.greaterThanOrEqual=" + (DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes is less than or equal to DEFAULT_MINUTES
        defaultExerciseConfigShouldBeFound("minutes.lessThanOrEqual=" + DEFAULT_MINUTES);

        // Get all the exerciseConfigList where minutes is less than or equal to SMALLER_MINUTES
        defaultExerciseConfigShouldNotBeFound("minutes.lessThanOrEqual=" + SMALLER_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes is less than DEFAULT_MINUTES
        defaultExerciseConfigShouldNotBeFound("minutes.lessThan=" + DEFAULT_MINUTES);

        // Get all the exerciseConfigList where minutes is less than (DEFAULT_MINUTES + 1)
        defaultExerciseConfigShouldBeFound("minutes.lessThan=" + (DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where minutes is greater than DEFAULT_MINUTES
        defaultExerciseConfigShouldNotBeFound("minutes.greaterThan=" + DEFAULT_MINUTES);

        // Get all the exerciseConfigList where minutes is greater than SMALLER_MINUTES
        defaultExerciseConfigShouldBeFound("minutes.greaterThan=" + SMALLER_MINUTES);
    }


    @Test
    @Transactional
    public void getAllExerciseConfigsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where note equals to DEFAULT_NOTE
        defaultExerciseConfigShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the exerciseConfigList where note equals to UPDATED_NOTE
        defaultExerciseConfigShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where note not equals to DEFAULT_NOTE
        defaultExerciseConfigShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the exerciseConfigList where note not equals to UPDATED_NOTE
        defaultExerciseConfigShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultExerciseConfigShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the exerciseConfigList where note equals to UPDATED_NOTE
        defaultExerciseConfigShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where note is not null
        defaultExerciseConfigShouldBeFound("note.specified=true");

        // Get all the exerciseConfigList where note is null
        defaultExerciseConfigShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllExerciseConfigsByNoteContainsSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where note contains DEFAULT_NOTE
        defaultExerciseConfigShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the exerciseConfigList where note contains UPDATED_NOTE
        defaultExerciseConfigShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where note does not contain DEFAULT_NOTE
        defaultExerciseConfigShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the exerciseConfigList where note does not contain UPDATED_NOTE
        defaultExerciseConfigShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllExerciseConfigsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where createDate equals to DEFAULT_CREATE_DATE
        defaultExerciseConfigShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the exerciseConfigList where createDate equals to UPDATED_CREATE_DATE
        defaultExerciseConfigShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where createDate not equals to DEFAULT_CREATE_DATE
        defaultExerciseConfigShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the exerciseConfigList where createDate not equals to UPDATED_CREATE_DATE
        defaultExerciseConfigShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultExerciseConfigShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the exerciseConfigList where createDate equals to UPDATED_CREATE_DATE
        defaultExerciseConfigShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where createDate is not null
        defaultExerciseConfigShouldBeFound("createDate.specified=true");

        // Get all the exerciseConfigList where createDate is null
        defaultExerciseConfigShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultExerciseConfigShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the exerciseConfigList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExerciseConfigShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultExerciseConfigShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the exerciseConfigList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultExerciseConfigShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultExerciseConfigShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the exerciseConfigList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExerciseConfigShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        // Get all the exerciseConfigList where modifyDate is not null
        defaultExerciseConfigShouldBeFound("modifyDate.specified=true");

        // Get all the exerciseConfigList where modifyDate is null
        defaultExerciseConfigShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExerciseConfigsByPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);
        Plan plan = PlanResourceIT.createEntity(em);
        em.persist(plan);
        em.flush();
        exerciseConfig.addPlan(plan);
        exerciseConfigRepository.saveAndFlush(exerciseConfig);
        Long planId = plan.getId();

        // Get all the exerciseConfigList where plan equals to planId
        defaultExerciseConfigShouldBeFound("planId.equals=" + planId);

        // Get all the exerciseConfigList where plan equals to planId + 1
        defaultExerciseConfigShouldNotBeFound("planId.equals=" + (planId + 1));
    }


    @Test
    @Transactional
    public void getAllExerciseConfigsByExerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);
        Exercise exercise = ExerciseResourceIT.createEntity(em);
        em.persist(exercise);
        em.flush();
        exerciseConfig.setExercise(exercise);
        exerciseConfigRepository.saveAndFlush(exerciseConfig);
        Long exerciseId = exercise.getId();

        // Get all the exerciseConfigList where exercise equals to exerciseId
        defaultExerciseConfigShouldBeFound("exerciseId.equals=" + exerciseId);

        // Get all the exerciseConfigList where exercise equals to exerciseId + 1
        defaultExerciseConfigShouldNotBeFound("exerciseId.equals=" + (exerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseConfigShouldBeFound(String filter) throws Exception {
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exerciseConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].practiceBpm").value(hasItem(DEFAULT_PRACTICE_BPM)))
            .andExpect(jsonPath("$.[*].targetBpm").value(hasItem(DEFAULT_TARGET_BPM)))
            .andExpect(jsonPath("$.[*].minutes").value(hasItem(DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseConfigShouldNotBeFound(String filter) throws Exception {
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExerciseConfig() throws Exception {
        // Get the exerciseConfig
        restExerciseConfigMockMvc.perform(get("/api/exercise-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExerciseConfig() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        int databaseSizeBeforeUpdate = exerciseConfigRepository.findAll().size();

        // Update the exerciseConfig
        ExerciseConfig updatedExerciseConfig = exerciseConfigRepository.findById(exerciseConfig.getId()).get();
        // Disconnect from session so that the updates on updatedExerciseConfig are not directly saved in db
        em.detach(updatedExerciseConfig);
        updatedExerciseConfig
            .practiceBpm(UPDATED_PRACTICE_BPM)
            .targetBpm(UPDATED_TARGET_BPM)
            .minutes(UPDATED_MINUTES)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        ExerciseConfigDTO exerciseConfigDTO = exerciseConfigMapper.toDto(updatedExerciseConfig);

        restExerciseConfigMockMvc.perform(put("/api/exercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseConfigDTO)))
            .andExpect(status().isOk());

        // Validate the ExerciseConfig in the database
        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeUpdate);
        ExerciseConfig testExerciseConfig = exerciseConfigList.get(exerciseConfigList.size() - 1);
        assertThat(testExerciseConfig.getPracticeBpm()).isEqualTo(UPDATED_PRACTICE_BPM);
        assertThat(testExerciseConfig.getTargetBpm()).isEqualTo(UPDATED_TARGET_BPM);
        assertThat(testExerciseConfig.getMinutes()).isEqualTo(UPDATED_MINUTES);
        assertThat(testExerciseConfig.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testExerciseConfig.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testExerciseConfig.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingExerciseConfig() throws Exception {
        int databaseSizeBeforeUpdate = exerciseConfigRepository.findAll().size();

        // Create the ExerciseConfig
        ExerciseConfigDTO exerciseConfigDTO = exerciseConfigMapper.toDto(exerciseConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseConfigMockMvc.perform(put("/api/exercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExerciseConfig in the database
        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExerciseConfig() throws Exception {
        // Initialize the database
        exerciseConfigRepository.saveAndFlush(exerciseConfig);

        int databaseSizeBeforeDelete = exerciseConfigRepository.findAll().size();

        // Delete the exerciseConfig
        restExerciseConfigMockMvc.perform(delete("/api/exercise-configs/{id}", exerciseConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExerciseConfig> exerciseConfigList = exerciseConfigRepository.findAll();
        assertThat(exerciseConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseConfig.class);
        ExerciseConfig exerciseConfig1 = new ExerciseConfig();
        exerciseConfig1.setId(1L);
        ExerciseConfig exerciseConfig2 = new ExerciseConfig();
        exerciseConfig2.setId(exerciseConfig1.getId());
        assertThat(exerciseConfig1).isEqualTo(exerciseConfig2);
        exerciseConfig2.setId(2L);
        assertThat(exerciseConfig1).isNotEqualTo(exerciseConfig2);
        exerciseConfig1.setId(null);
        assertThat(exerciseConfig1).isNotEqualTo(exerciseConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseConfigDTO.class);
        ExerciseConfigDTO exerciseConfigDTO1 = new ExerciseConfigDTO();
        exerciseConfigDTO1.setId(1L);
        ExerciseConfigDTO exerciseConfigDTO2 = new ExerciseConfigDTO();
        assertThat(exerciseConfigDTO1).isNotEqualTo(exerciseConfigDTO2);
        exerciseConfigDTO2.setId(exerciseConfigDTO1.getId());
        assertThat(exerciseConfigDTO1).isEqualTo(exerciseConfigDTO2);
        exerciseConfigDTO2.setId(2L);
        assertThat(exerciseConfigDTO1).isNotEqualTo(exerciseConfigDTO2);
        exerciseConfigDTO1.setId(null);
        assertThat(exerciseConfigDTO1).isNotEqualTo(exerciseConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(exerciseConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(exerciseConfigMapper.fromId(null)).isNull();
    }
}
