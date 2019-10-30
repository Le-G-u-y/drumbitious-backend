package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.ExcerciseConfig;
import com.drumbitious.backend.domain.Plan;
import com.drumbitious.backend.domain.Excercise;
import com.drumbitious.backend.repository.ExcerciseConfigRepository;
import com.drumbitious.backend.service.ExcerciseConfigService;
import com.drumbitious.backend.service.dto.ExcerciseConfigDTO;
import com.drumbitious.backend.service.mapper.ExcerciseConfigMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.ExcerciseConfigCriteria;
import com.drumbitious.backend.service.ExcerciseConfigQueryService;

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
 * Integration tests for the {@link ExcerciseConfigResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class ExcerciseConfigResourceIT {

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
    private ExcerciseConfigRepository excerciseConfigRepository;

    @Autowired
    private ExcerciseConfigMapper excerciseConfigMapper;

    @Autowired
    private ExcerciseConfigService excerciseConfigService;

    @Autowired
    private ExcerciseConfigQueryService excerciseConfigQueryService;

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

    private MockMvc restExcerciseConfigMockMvc;

    private ExcerciseConfig excerciseConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExcerciseConfigResource excerciseConfigResource = new ExcerciseConfigResource(excerciseConfigService, excerciseConfigQueryService);
        this.restExcerciseConfigMockMvc = MockMvcBuilders.standaloneSetup(excerciseConfigResource)
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
    public static ExcerciseConfig createEntity(EntityManager em) {
        ExcerciseConfig excerciseConfig = new ExcerciseConfig()
            .practiceBpm(DEFAULT_PRACTICE_BPM)
            .targetBpm(DEFAULT_TARGET_BPM)
            .minutes(DEFAULT_MINUTES)
            .note(DEFAULT_NOTE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return excerciseConfig;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExcerciseConfig createUpdatedEntity(EntityManager em) {
        ExcerciseConfig excerciseConfig = new ExcerciseConfig()
            .practiceBpm(UPDATED_PRACTICE_BPM)
            .targetBpm(UPDATED_TARGET_BPM)
            .minutes(UPDATED_MINUTES)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return excerciseConfig;
    }

    @BeforeEach
    public void initTest() {
        excerciseConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createExcerciseConfig() throws Exception {
        int databaseSizeBeforeCreate = excerciseConfigRepository.findAll().size();

        // Create the ExcerciseConfig
        ExcerciseConfigDTO excerciseConfigDTO = excerciseConfigMapper.toDto(excerciseConfig);
        restExcerciseConfigMockMvc.perform(post("/api/excercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseConfigDTO)))
            .andExpect(status().isCreated());

        // Validate the ExcerciseConfig in the database
        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeCreate + 1);
        ExcerciseConfig testExcerciseConfig = excerciseConfigList.get(excerciseConfigList.size() - 1);
        assertThat(testExcerciseConfig.getPracticeBpm()).isEqualTo(DEFAULT_PRACTICE_BPM);
        assertThat(testExcerciseConfig.getTargetBpm()).isEqualTo(DEFAULT_TARGET_BPM);
        assertThat(testExcerciseConfig.getMinutes()).isEqualTo(DEFAULT_MINUTES);
        assertThat(testExcerciseConfig.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testExcerciseConfig.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testExcerciseConfig.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createExcerciseConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = excerciseConfigRepository.findAll().size();

        // Create the ExcerciseConfig with an existing ID
        excerciseConfig.setId(1L);
        ExcerciseConfigDTO excerciseConfigDTO = excerciseConfigMapper.toDto(excerciseConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcerciseConfigMockMvc.perform(post("/api/excercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExcerciseConfig in the database
        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = excerciseConfigRepository.findAll().size();
        // set the field null
        excerciseConfig.setCreateDate(null);

        // Create the ExcerciseConfig, which fails.
        ExcerciseConfigDTO excerciseConfigDTO = excerciseConfigMapper.toDto(excerciseConfig);

        restExcerciseConfigMockMvc.perform(post("/api/excercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = excerciseConfigRepository.findAll().size();
        // set the field null
        excerciseConfig.setModifyDate(null);

        // Create the ExcerciseConfig, which fails.
        ExcerciseConfigDTO excerciseConfigDTO = excerciseConfigMapper.toDto(excerciseConfig);

        restExcerciseConfigMockMvc.perform(post("/api/excercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigs() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excerciseConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].practiceBpm").value(hasItem(DEFAULT_PRACTICE_BPM)))
            .andExpect(jsonPath("$.[*].targetBpm").value(hasItem(DEFAULT_TARGET_BPM)))
            .andExpect(jsonPath("$.[*].minutes").value(hasItem(DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getExcerciseConfig() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get the excerciseConfig
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs/{id}", excerciseConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(excerciseConfig.getId().intValue()))
            .andExpect(jsonPath("$.practiceBpm").value(DEFAULT_PRACTICE_BPM))
            .andExpect(jsonPath("$.targetBpm").value(DEFAULT_TARGET_BPM))
            .andExpect(jsonPath("$.minutes").value(DEFAULT_MINUTES))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm equals to DEFAULT_PRACTICE_BPM
        defaultExcerciseConfigShouldBeFound("practiceBpm.equals=" + DEFAULT_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm equals to UPDATED_PRACTICE_BPM
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.equals=" + UPDATED_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm not equals to DEFAULT_PRACTICE_BPM
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.notEquals=" + DEFAULT_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm not equals to UPDATED_PRACTICE_BPM
        defaultExcerciseConfigShouldBeFound("practiceBpm.notEquals=" + UPDATED_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm in DEFAULT_PRACTICE_BPM or UPDATED_PRACTICE_BPM
        defaultExcerciseConfigShouldBeFound("practiceBpm.in=" + DEFAULT_PRACTICE_BPM + "," + UPDATED_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm equals to UPDATED_PRACTICE_BPM
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.in=" + UPDATED_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm is not null
        defaultExcerciseConfigShouldBeFound("practiceBpm.specified=true");

        // Get all the excerciseConfigList where practiceBpm is null
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm is greater than or equal to DEFAULT_PRACTICE_BPM
        defaultExcerciseConfigShouldBeFound("practiceBpm.greaterThanOrEqual=" + DEFAULT_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm is greater than or equal to (DEFAULT_PRACTICE_BPM + 1)
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.greaterThanOrEqual=" + (DEFAULT_PRACTICE_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm is less than or equal to DEFAULT_PRACTICE_BPM
        defaultExcerciseConfigShouldBeFound("practiceBpm.lessThanOrEqual=" + DEFAULT_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm is less than or equal to SMALLER_PRACTICE_BPM
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.lessThanOrEqual=" + SMALLER_PRACTICE_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsLessThanSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm is less than DEFAULT_PRACTICE_BPM
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.lessThan=" + DEFAULT_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm is less than (DEFAULT_PRACTICE_BPM + 1)
        defaultExcerciseConfigShouldBeFound("practiceBpm.lessThan=" + (DEFAULT_PRACTICE_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPracticeBpmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where practiceBpm is greater than DEFAULT_PRACTICE_BPM
        defaultExcerciseConfigShouldNotBeFound("practiceBpm.greaterThan=" + DEFAULT_PRACTICE_BPM);

        // Get all the excerciseConfigList where practiceBpm is greater than SMALLER_PRACTICE_BPM
        defaultExcerciseConfigShouldBeFound("practiceBpm.greaterThan=" + SMALLER_PRACTICE_BPM);
    }


    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm equals to DEFAULT_TARGET_BPM
        defaultExcerciseConfigShouldBeFound("targetBpm.equals=" + DEFAULT_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm equals to UPDATED_TARGET_BPM
        defaultExcerciseConfigShouldNotBeFound("targetBpm.equals=" + UPDATED_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm not equals to DEFAULT_TARGET_BPM
        defaultExcerciseConfigShouldNotBeFound("targetBpm.notEquals=" + DEFAULT_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm not equals to UPDATED_TARGET_BPM
        defaultExcerciseConfigShouldBeFound("targetBpm.notEquals=" + UPDATED_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm in DEFAULT_TARGET_BPM or UPDATED_TARGET_BPM
        defaultExcerciseConfigShouldBeFound("targetBpm.in=" + DEFAULT_TARGET_BPM + "," + UPDATED_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm equals to UPDATED_TARGET_BPM
        defaultExcerciseConfigShouldNotBeFound("targetBpm.in=" + UPDATED_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm is not null
        defaultExcerciseConfigShouldBeFound("targetBpm.specified=true");

        // Get all the excerciseConfigList where targetBpm is null
        defaultExcerciseConfigShouldNotBeFound("targetBpm.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm is greater than or equal to DEFAULT_TARGET_BPM
        defaultExcerciseConfigShouldBeFound("targetBpm.greaterThanOrEqual=" + DEFAULT_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm is greater than or equal to (DEFAULT_TARGET_BPM + 1)
        defaultExcerciseConfigShouldNotBeFound("targetBpm.greaterThanOrEqual=" + (DEFAULT_TARGET_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm is less than or equal to DEFAULT_TARGET_BPM
        defaultExcerciseConfigShouldBeFound("targetBpm.lessThanOrEqual=" + DEFAULT_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm is less than or equal to SMALLER_TARGET_BPM
        defaultExcerciseConfigShouldNotBeFound("targetBpm.lessThanOrEqual=" + SMALLER_TARGET_BPM);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsLessThanSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm is less than DEFAULT_TARGET_BPM
        defaultExcerciseConfigShouldNotBeFound("targetBpm.lessThan=" + DEFAULT_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm is less than (DEFAULT_TARGET_BPM + 1)
        defaultExcerciseConfigShouldBeFound("targetBpm.lessThan=" + (DEFAULT_TARGET_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByTargetBpmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where targetBpm is greater than DEFAULT_TARGET_BPM
        defaultExcerciseConfigShouldNotBeFound("targetBpm.greaterThan=" + DEFAULT_TARGET_BPM);

        // Get all the excerciseConfigList where targetBpm is greater than SMALLER_TARGET_BPM
        defaultExcerciseConfigShouldBeFound("targetBpm.greaterThan=" + SMALLER_TARGET_BPM);
    }


    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes equals to DEFAULT_MINUTES
        defaultExcerciseConfigShouldBeFound("minutes.equals=" + DEFAULT_MINUTES);

        // Get all the excerciseConfigList where minutes equals to UPDATED_MINUTES
        defaultExcerciseConfigShouldNotBeFound("minutes.equals=" + UPDATED_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes not equals to DEFAULT_MINUTES
        defaultExcerciseConfigShouldNotBeFound("minutes.notEquals=" + DEFAULT_MINUTES);

        // Get all the excerciseConfigList where minutes not equals to UPDATED_MINUTES
        defaultExcerciseConfigShouldBeFound("minutes.notEquals=" + UPDATED_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes in DEFAULT_MINUTES or UPDATED_MINUTES
        defaultExcerciseConfigShouldBeFound("minutes.in=" + DEFAULT_MINUTES + "," + UPDATED_MINUTES);

        // Get all the excerciseConfigList where minutes equals to UPDATED_MINUTES
        defaultExcerciseConfigShouldNotBeFound("minutes.in=" + UPDATED_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes is not null
        defaultExcerciseConfigShouldBeFound("minutes.specified=true");

        // Get all the excerciseConfigList where minutes is null
        defaultExcerciseConfigShouldNotBeFound("minutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes is greater than or equal to DEFAULT_MINUTES
        defaultExcerciseConfigShouldBeFound("minutes.greaterThanOrEqual=" + DEFAULT_MINUTES);

        // Get all the excerciseConfigList where minutes is greater than or equal to (DEFAULT_MINUTES + 1)
        defaultExcerciseConfigShouldNotBeFound("minutes.greaterThanOrEqual=" + (DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes is less than or equal to DEFAULT_MINUTES
        defaultExcerciseConfigShouldBeFound("minutes.lessThanOrEqual=" + DEFAULT_MINUTES);

        // Get all the excerciseConfigList where minutes is less than or equal to SMALLER_MINUTES
        defaultExcerciseConfigShouldNotBeFound("minutes.lessThanOrEqual=" + SMALLER_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes is less than DEFAULT_MINUTES
        defaultExcerciseConfigShouldNotBeFound("minutes.lessThan=" + DEFAULT_MINUTES);

        // Get all the excerciseConfigList where minutes is less than (DEFAULT_MINUTES + 1)
        defaultExcerciseConfigShouldBeFound("minutes.lessThan=" + (DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where minutes is greater than DEFAULT_MINUTES
        defaultExcerciseConfigShouldNotBeFound("minutes.greaterThan=" + DEFAULT_MINUTES);

        // Get all the excerciseConfigList where minutes is greater than SMALLER_MINUTES
        defaultExcerciseConfigShouldBeFound("minutes.greaterThan=" + SMALLER_MINUTES);
    }


    @Test
    @Transactional
    public void getAllExcerciseConfigsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where note equals to DEFAULT_NOTE
        defaultExcerciseConfigShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the excerciseConfigList where note equals to UPDATED_NOTE
        defaultExcerciseConfigShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where note not equals to DEFAULT_NOTE
        defaultExcerciseConfigShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the excerciseConfigList where note not equals to UPDATED_NOTE
        defaultExcerciseConfigShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultExcerciseConfigShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the excerciseConfigList where note equals to UPDATED_NOTE
        defaultExcerciseConfigShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where note is not null
        defaultExcerciseConfigShouldBeFound("note.specified=true");

        // Get all the excerciseConfigList where note is null
        defaultExcerciseConfigShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllExcerciseConfigsByNoteContainsSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where note contains DEFAULT_NOTE
        defaultExcerciseConfigShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the excerciseConfigList where note contains UPDATED_NOTE
        defaultExcerciseConfigShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where note does not contain DEFAULT_NOTE
        defaultExcerciseConfigShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the excerciseConfigList where note does not contain UPDATED_NOTE
        defaultExcerciseConfigShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllExcerciseConfigsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where createDate equals to DEFAULT_CREATE_DATE
        defaultExcerciseConfigShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the excerciseConfigList where createDate equals to UPDATED_CREATE_DATE
        defaultExcerciseConfigShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where createDate not equals to DEFAULT_CREATE_DATE
        defaultExcerciseConfigShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the excerciseConfigList where createDate not equals to UPDATED_CREATE_DATE
        defaultExcerciseConfigShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultExcerciseConfigShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the excerciseConfigList where createDate equals to UPDATED_CREATE_DATE
        defaultExcerciseConfigShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where createDate is not null
        defaultExcerciseConfigShouldBeFound("createDate.specified=true");

        // Get all the excerciseConfigList where createDate is null
        defaultExcerciseConfigShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultExcerciseConfigShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the excerciseConfigList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExcerciseConfigShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultExcerciseConfigShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the excerciseConfigList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultExcerciseConfigShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultExcerciseConfigShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the excerciseConfigList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExcerciseConfigShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        // Get all the excerciseConfigList where modifyDate is not null
        defaultExcerciseConfigShouldBeFound("modifyDate.specified=true");

        // Get all the excerciseConfigList where modifyDate is null
        defaultExcerciseConfigShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcerciseConfigsByPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);
        Plan plan = PlanResourceIT.createEntity(em);
        em.persist(plan);
        em.flush();
        excerciseConfig.addPlan(plan);
        excerciseConfigRepository.saveAndFlush(excerciseConfig);
        Long planId = plan.getId();

        // Get all the excerciseConfigList where plan equals to planId
        defaultExcerciseConfigShouldBeFound("planId.equals=" + planId);

        // Get all the excerciseConfigList where plan equals to planId + 1
        defaultExcerciseConfigShouldNotBeFound("planId.equals=" + (planId + 1));
    }


    @Test
    @Transactional
    public void getAllExcerciseConfigsByExcerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);
        Excercise excercise = ExcerciseResourceIT.createEntity(em);
        em.persist(excercise);
        em.flush();
        excerciseConfig.setExcercise(excercise);
        excerciseConfigRepository.saveAndFlush(excerciseConfig);
        Long excerciseId = excercise.getId();

        // Get all the excerciseConfigList where excercise equals to excerciseId
        defaultExcerciseConfigShouldBeFound("excerciseId.equals=" + excerciseId);

        // Get all the excerciseConfigList where excercise equals to excerciseId + 1
        defaultExcerciseConfigShouldNotBeFound("excerciseId.equals=" + (excerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExcerciseConfigShouldBeFound(String filter) throws Exception {
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excerciseConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].practiceBpm").value(hasItem(DEFAULT_PRACTICE_BPM)))
            .andExpect(jsonPath("$.[*].targetBpm").value(hasItem(DEFAULT_TARGET_BPM)))
            .andExpect(jsonPath("$.[*].minutes").value(hasItem(DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExcerciseConfigShouldNotBeFound(String filter) throws Exception {
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExcerciseConfig() throws Exception {
        // Get the excerciseConfig
        restExcerciseConfigMockMvc.perform(get("/api/excercise-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExcerciseConfig() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        int databaseSizeBeforeUpdate = excerciseConfigRepository.findAll().size();

        // Update the excerciseConfig
        ExcerciseConfig updatedExcerciseConfig = excerciseConfigRepository.findById(excerciseConfig.getId()).get();
        // Disconnect from session so that the updates on updatedExcerciseConfig are not directly saved in db
        em.detach(updatedExcerciseConfig);
        updatedExcerciseConfig
            .practiceBpm(UPDATED_PRACTICE_BPM)
            .targetBpm(UPDATED_TARGET_BPM)
            .minutes(UPDATED_MINUTES)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        ExcerciseConfigDTO excerciseConfigDTO = excerciseConfigMapper.toDto(updatedExcerciseConfig);

        restExcerciseConfigMockMvc.perform(put("/api/excercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseConfigDTO)))
            .andExpect(status().isOk());

        // Validate the ExcerciseConfig in the database
        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeUpdate);
        ExcerciseConfig testExcerciseConfig = excerciseConfigList.get(excerciseConfigList.size() - 1);
        assertThat(testExcerciseConfig.getPracticeBpm()).isEqualTo(UPDATED_PRACTICE_BPM);
        assertThat(testExcerciseConfig.getTargetBpm()).isEqualTo(UPDATED_TARGET_BPM);
        assertThat(testExcerciseConfig.getMinutes()).isEqualTo(UPDATED_MINUTES);
        assertThat(testExcerciseConfig.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testExcerciseConfig.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testExcerciseConfig.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingExcerciseConfig() throws Exception {
        int databaseSizeBeforeUpdate = excerciseConfigRepository.findAll().size();

        // Create the ExcerciseConfig
        ExcerciseConfigDTO excerciseConfigDTO = excerciseConfigMapper.toDto(excerciseConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseConfigMockMvc.perform(put("/api/excercise-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExcerciseConfig in the database
        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExcerciseConfig() throws Exception {
        // Initialize the database
        excerciseConfigRepository.saveAndFlush(excerciseConfig);

        int databaseSizeBeforeDelete = excerciseConfigRepository.findAll().size();

        // Delete the excerciseConfig
        restExcerciseConfigMockMvc.perform(delete("/api/excercise-configs/{id}", excerciseConfig.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ExcerciseConfig> excerciseConfigList = excerciseConfigRepository.findAll();
        assertThat(excerciseConfigList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcerciseConfig.class);
        ExcerciseConfig excerciseConfig1 = new ExcerciseConfig();
        excerciseConfig1.setId(1L);
        ExcerciseConfig excerciseConfig2 = new ExcerciseConfig();
        excerciseConfig2.setId(excerciseConfig1.getId());
        assertThat(excerciseConfig1).isEqualTo(excerciseConfig2);
        excerciseConfig2.setId(2L);
        assertThat(excerciseConfig1).isNotEqualTo(excerciseConfig2);
        excerciseConfig1.setId(null);
        assertThat(excerciseConfig1).isNotEqualTo(excerciseConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcerciseConfigDTO.class);
        ExcerciseConfigDTO excerciseConfigDTO1 = new ExcerciseConfigDTO();
        excerciseConfigDTO1.setId(1L);
        ExcerciseConfigDTO excerciseConfigDTO2 = new ExcerciseConfigDTO();
        assertThat(excerciseConfigDTO1).isNotEqualTo(excerciseConfigDTO2);
        excerciseConfigDTO2.setId(excerciseConfigDTO1.getId());
        assertThat(excerciseConfigDTO1).isEqualTo(excerciseConfigDTO2);
        excerciseConfigDTO2.setId(2L);
        assertThat(excerciseConfigDTO1).isNotEqualTo(excerciseConfigDTO2);
        excerciseConfigDTO1.setId(null);
        assertThat(excerciseConfigDTO1).isNotEqualTo(excerciseConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(excerciseConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(excerciseConfigMapper.fromId(null)).isNull();
    }
}
