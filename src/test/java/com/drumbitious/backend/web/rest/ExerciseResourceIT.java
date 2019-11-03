package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.Exercise;
import com.drumbitious.backend.domain.User;
import com.drumbitious.backend.repository.ExerciseRepository;
import com.drumbitious.backend.service.ExerciseService;
import com.drumbitious.backend.service.dto.ExerciseDTO;
import com.drumbitious.backend.service.mapper.ExerciseMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.ExerciseCriteria;
import com.drumbitious.backend.service.ExerciseQueryService;

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

import com.drumbitious.backend.domain.enumeration.SkillType;
import com.drumbitious.backend.domain.enumeration.ExerciseType;
/**
 * Integration tests for the {@link ExerciseResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class ExerciseResourceIT {

    private static final String DEFAULT_EXERCISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_URL = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_DEFAULT_MINUTES = 9000;
    private static final Integer UPDATED_DEFAULT_MINUTES = 8999;
    private static final Integer SMALLER_DEFAULT_MINUTES = 9000 - 1;

    private static final Integer DEFAULT_DEFAULT_BPM_MIN = 1;
    private static final Integer UPDATED_DEFAULT_BPM_MIN = 2;
    private static final Integer SMALLER_DEFAULT_BPM_MIN = 1 - 1;

    private static final Integer DEFAULT_DEFAULT_BPM_MAX = 1;
    private static final Integer UPDATED_DEFAULT_BPM_MAX = 2;
    private static final Integer SMALLER_DEFAULT_BPM_MAX = 1 - 1;

    private static final Boolean DEFAULT_DEACTIVTED = false;
    private static final Boolean UPDATED_DEACTIVTED = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final SkillType DEFAULT_SKILL_TYPE = SkillType.SPEED;
    private static final SkillType UPDATED_SKILL_TYPE = SkillType.GROOVE;

    private static final ExerciseType DEFAULT_EXERCISE = ExerciseType.RUDIMENT;
    private static final ExerciseType UPDATED_EXERCISE = ExerciseType.TECHNIQUE;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private ExerciseQueryService exerciseQueryService;

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

    private MockMvc restExerciseMockMvc;

    private Exercise exercise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExerciseResource exerciseResource = new ExerciseResource(exerciseService, exerciseQueryService);
        this.restExerciseMockMvc = MockMvcBuilders.standaloneSetup(exerciseResource)
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
    public static Exercise createEntity(EntityManager em) {
        Exercise exercise = new Exercise()
            .exerciseName(DEFAULT_EXERCISE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sourceUrl(DEFAULT_SOURCE_URL)
            .defaultMinutes(DEFAULT_DEFAULT_MINUTES)
            .defaultBpmMin(DEFAULT_DEFAULT_BPM_MIN)
            .defaultBpmMax(DEFAULT_DEFAULT_BPM_MAX)
            .deactivted(DEFAULT_DEACTIVTED)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE)
            .skillType(DEFAULT_SKILL_TYPE)
            .exercise(DEFAULT_EXERCISE);
        return exercise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exercise createUpdatedEntity(EntityManager em) {
        Exercise exercise = new Exercise()
            .exerciseName(UPDATED_EXERCISE_NAME)
            .description(UPDATED_DESCRIPTION)
            .sourceUrl(UPDATED_SOURCE_URL)
            .defaultMinutes(UPDATED_DEFAULT_MINUTES)
            .defaultBpmMin(UPDATED_DEFAULT_BPM_MIN)
            .defaultBpmMax(UPDATED_DEFAULT_BPM_MAX)
            .deactivted(UPDATED_DEACTIVTED)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE)
            .skillType(UPDATED_SKILL_TYPE)
            .exercise(UPDATED_EXERCISE);
        return exercise;
    }

    @BeforeEach
    public void initTest() {
        exercise = createEntity(em);
    }

    @Test
    @Transactional
    public void createExercise() throws Exception {
        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);
        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isCreated());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getExerciseName()).isEqualTo(DEFAULT_EXERCISE_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExercise.getSourceUrl()).isEqualTo(DEFAULT_SOURCE_URL);
        assertThat(testExercise.getDefaultMinutes()).isEqualTo(DEFAULT_DEFAULT_MINUTES);
        assertThat(testExercise.getDefaultBpmMin()).isEqualTo(DEFAULT_DEFAULT_BPM_MIN);
        assertThat(testExercise.getDefaultBpmMax()).isEqualTo(DEFAULT_DEFAULT_BPM_MAX);
        assertThat(testExercise.isDeactivted()).isEqualTo(DEFAULT_DEACTIVTED);
        assertThat(testExercise.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testExercise.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
        assertThat(testExercise.getSkillType()).isEqualTo(DEFAULT_SKILL_TYPE);
        assertThat(testExercise.getExercise()).isEqualTo(DEFAULT_EXERCISE);
    }

    @Test
    @Transactional
    public void createExerciseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = exerciseRepository.findAll().size();

        // Create the Exercise with an existing ID
        exercise.setId(1L);
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkExerciseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setExerciseName(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setCreateDate(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setModifyDate(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSkillTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setSkillType(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExerciseIsRequired() throws Exception {
        int databaseSizeBeforeTest = exerciseRepository.findAll().size();
        // set the field null
        exercise.setExercise(null);

        // Create the Exercise, which fails.
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        restExerciseMockMvc.perform(post("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExercises() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList
        restExerciseMockMvc.perform(get("/api/exercises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].defaultMinutes").value(hasItem(DEFAULT_DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].defaultBpmMin").value(hasItem(DEFAULT_DEFAULT_BPM_MIN)))
            .andExpect(jsonPath("$.[*].defaultBpmMax").value(hasItem(DEFAULT_DEFAULT_BPM_MAX)))
            .andExpect(jsonPath("$.[*].deactivted").value(hasItem(DEFAULT_DEACTIVTED.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].skillType").value(hasItem(DEFAULT_SKILL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].exercise").value(hasItem(DEFAULT_EXERCISE.toString())));
    }
    
    @Test
    @Transactional
    public void getExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get the exercise
        restExerciseMockMvc.perform(get("/api/exercises/{id}", exercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exercise.getId().intValue()))
            .andExpect(jsonPath("$.exerciseName").value(DEFAULT_EXERCISE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.defaultMinutes").value(DEFAULT_DEFAULT_MINUTES))
            .andExpect(jsonPath("$.defaultBpmMin").value(DEFAULT_DEFAULT_BPM_MIN))
            .andExpect(jsonPath("$.defaultBpmMax").value(DEFAULT_DEFAULT_BPM_MAX))
            .andExpect(jsonPath("$.deactivted").value(DEFAULT_DEACTIVTED.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()))
            .andExpect(jsonPath("$.skillType").value(DEFAULT_SKILL_TYPE.toString()))
            .andExpect(jsonPath("$.exercise").value(DEFAULT_EXERCISE.toString()));
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exerciseName equals to DEFAULT_EXERCISE_NAME
        defaultExerciseShouldBeFound("exerciseName.equals=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultExerciseShouldNotBeFound("exerciseName.equals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exerciseName not equals to DEFAULT_EXERCISE_NAME
        defaultExerciseShouldNotBeFound("exerciseName.notEquals=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseList where exerciseName not equals to UPDATED_EXERCISE_NAME
        defaultExerciseShouldBeFound("exerciseName.notEquals=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseNameIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exerciseName in DEFAULT_EXERCISE_NAME or UPDATED_EXERCISE_NAME
        defaultExerciseShouldBeFound("exerciseName.in=" + DEFAULT_EXERCISE_NAME + "," + UPDATED_EXERCISE_NAME);

        // Get all the exerciseList where exerciseName equals to UPDATED_EXERCISE_NAME
        defaultExerciseShouldNotBeFound("exerciseName.in=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exerciseName is not null
        defaultExerciseShouldBeFound("exerciseName.specified=true");

        // Get all the exerciseList where exerciseName is null
        defaultExerciseShouldNotBeFound("exerciseName.specified=false");
    }
                @Test
    @Transactional
    public void getAllExercisesByExerciseNameContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exerciseName contains DEFAULT_EXERCISE_NAME
        defaultExerciseShouldBeFound("exerciseName.contains=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseList where exerciseName contains UPDATED_EXERCISE_NAME
        defaultExerciseShouldNotBeFound("exerciseName.contains=" + UPDATED_EXERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseNameNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exerciseName does not contain DEFAULT_EXERCISE_NAME
        defaultExerciseShouldNotBeFound("exerciseName.doesNotContain=" + DEFAULT_EXERCISE_NAME);

        // Get all the exerciseList where exerciseName does not contain UPDATED_EXERCISE_NAME
        defaultExerciseShouldBeFound("exerciseName.doesNotContain=" + UPDATED_EXERCISE_NAME);
    }


    @Test
    @Transactional
    public void getAllExercisesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where description equals to DEFAULT_DESCRIPTION
        defaultExerciseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the exerciseList where description equals to UPDATED_DESCRIPTION
        defaultExerciseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExercisesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where description not equals to DEFAULT_DESCRIPTION
        defaultExerciseShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the exerciseList where description not equals to UPDATED_DESCRIPTION
        defaultExerciseShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExercisesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultExerciseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the exerciseList where description equals to UPDATED_DESCRIPTION
        defaultExerciseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExercisesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where description is not null
        defaultExerciseShouldBeFound("description.specified=true");

        // Get all the exerciseList where description is null
        defaultExerciseShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllExercisesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where description contains DEFAULT_DESCRIPTION
        defaultExerciseShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the exerciseList where description contains UPDATED_DESCRIPTION
        defaultExerciseShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExercisesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where description does not contain DEFAULT_DESCRIPTION
        defaultExerciseShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the exerciseList where description does not contain UPDATED_DESCRIPTION
        defaultExerciseShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllExercisesBySourceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where sourceUrl equals to DEFAULT_SOURCE_URL
        defaultExerciseShouldBeFound("sourceUrl.equals=" + DEFAULT_SOURCE_URL);

        // Get all the exerciseList where sourceUrl equals to UPDATED_SOURCE_URL
        defaultExerciseShouldNotBeFound("sourceUrl.equals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExercisesBySourceUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where sourceUrl not equals to DEFAULT_SOURCE_URL
        defaultExerciseShouldNotBeFound("sourceUrl.notEquals=" + DEFAULT_SOURCE_URL);

        // Get all the exerciseList where sourceUrl not equals to UPDATED_SOURCE_URL
        defaultExerciseShouldBeFound("sourceUrl.notEquals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExercisesBySourceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where sourceUrl in DEFAULT_SOURCE_URL or UPDATED_SOURCE_URL
        defaultExerciseShouldBeFound("sourceUrl.in=" + DEFAULT_SOURCE_URL + "," + UPDATED_SOURCE_URL);

        // Get all the exerciseList where sourceUrl equals to UPDATED_SOURCE_URL
        defaultExerciseShouldNotBeFound("sourceUrl.in=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExercisesBySourceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where sourceUrl is not null
        defaultExerciseShouldBeFound("sourceUrl.specified=true");

        // Get all the exerciseList where sourceUrl is null
        defaultExerciseShouldNotBeFound("sourceUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllExercisesBySourceUrlContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where sourceUrl contains DEFAULT_SOURCE_URL
        defaultExerciseShouldBeFound("sourceUrl.contains=" + DEFAULT_SOURCE_URL);

        // Get all the exerciseList where sourceUrl contains UPDATED_SOURCE_URL
        defaultExerciseShouldNotBeFound("sourceUrl.contains=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExercisesBySourceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where sourceUrl does not contain DEFAULT_SOURCE_URL
        defaultExerciseShouldNotBeFound("sourceUrl.doesNotContain=" + DEFAULT_SOURCE_URL);

        // Get all the exerciseList where sourceUrl does not contain UPDATED_SOURCE_URL
        defaultExerciseShouldBeFound("sourceUrl.doesNotContain=" + UPDATED_SOURCE_URL);
    }


    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes equals to DEFAULT_DEFAULT_MINUTES
        defaultExerciseShouldBeFound("defaultMinutes.equals=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes equals to UPDATED_DEFAULT_MINUTES
        defaultExerciseShouldNotBeFound("defaultMinutes.equals=" + UPDATED_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes not equals to DEFAULT_DEFAULT_MINUTES
        defaultExerciseShouldNotBeFound("defaultMinutes.notEquals=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes not equals to UPDATED_DEFAULT_MINUTES
        defaultExerciseShouldBeFound("defaultMinutes.notEquals=" + UPDATED_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes in DEFAULT_DEFAULT_MINUTES or UPDATED_DEFAULT_MINUTES
        defaultExerciseShouldBeFound("defaultMinutes.in=" + DEFAULT_DEFAULT_MINUTES + "," + UPDATED_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes equals to UPDATED_DEFAULT_MINUTES
        defaultExerciseShouldNotBeFound("defaultMinutes.in=" + UPDATED_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes is not null
        defaultExerciseShouldBeFound("defaultMinutes.specified=true");

        // Get all the exerciseList where defaultMinutes is null
        defaultExerciseShouldNotBeFound("defaultMinutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes is greater than or equal to DEFAULT_DEFAULT_MINUTES
        defaultExerciseShouldBeFound("defaultMinutes.greaterThanOrEqual=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes is greater than or equal to (DEFAULT_DEFAULT_MINUTES + 1)
        defaultExerciseShouldNotBeFound("defaultMinutes.greaterThanOrEqual=" + (DEFAULT_DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes is less than or equal to DEFAULT_DEFAULT_MINUTES
        defaultExerciseShouldBeFound("defaultMinutes.lessThanOrEqual=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes is less than or equal to SMALLER_DEFAULT_MINUTES
        defaultExerciseShouldNotBeFound("defaultMinutes.lessThanOrEqual=" + SMALLER_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes is less than DEFAULT_DEFAULT_MINUTES
        defaultExerciseShouldNotBeFound("defaultMinutes.lessThan=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes is less than (DEFAULT_DEFAULT_MINUTES + 1)
        defaultExerciseShouldBeFound("defaultMinutes.lessThan=" + (DEFAULT_DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultMinutes is greater than DEFAULT_DEFAULT_MINUTES
        defaultExerciseShouldNotBeFound("defaultMinutes.greaterThan=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the exerciseList where defaultMinutes is greater than SMALLER_DEFAULT_MINUTES
        defaultExerciseShouldBeFound("defaultMinutes.greaterThan=" + SMALLER_DEFAULT_MINUTES);
    }


    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin equals to DEFAULT_DEFAULT_BPM_MIN
        defaultExerciseShouldBeFound("defaultBpmMin.equals=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin equals to UPDATED_DEFAULT_BPM_MIN
        defaultExerciseShouldNotBeFound("defaultBpmMin.equals=" + UPDATED_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin not equals to DEFAULT_DEFAULT_BPM_MIN
        defaultExerciseShouldNotBeFound("defaultBpmMin.notEquals=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin not equals to UPDATED_DEFAULT_BPM_MIN
        defaultExerciseShouldBeFound("defaultBpmMin.notEquals=" + UPDATED_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin in DEFAULT_DEFAULT_BPM_MIN or UPDATED_DEFAULT_BPM_MIN
        defaultExerciseShouldBeFound("defaultBpmMin.in=" + DEFAULT_DEFAULT_BPM_MIN + "," + UPDATED_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin equals to UPDATED_DEFAULT_BPM_MIN
        defaultExerciseShouldNotBeFound("defaultBpmMin.in=" + UPDATED_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin is not null
        defaultExerciseShouldBeFound("defaultBpmMin.specified=true");

        // Get all the exerciseList where defaultBpmMin is null
        defaultExerciseShouldNotBeFound("defaultBpmMin.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin is greater than or equal to DEFAULT_DEFAULT_BPM_MIN
        defaultExerciseShouldBeFound("defaultBpmMin.greaterThanOrEqual=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin is greater than or equal to (DEFAULT_DEFAULT_BPM_MIN + 1)
        defaultExerciseShouldNotBeFound("defaultBpmMin.greaterThanOrEqual=" + (DEFAULT_DEFAULT_BPM_MIN + 1));
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin is less than or equal to DEFAULT_DEFAULT_BPM_MIN
        defaultExerciseShouldBeFound("defaultBpmMin.lessThanOrEqual=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin is less than or equal to SMALLER_DEFAULT_BPM_MIN
        defaultExerciseShouldNotBeFound("defaultBpmMin.lessThanOrEqual=" + SMALLER_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin is less than DEFAULT_DEFAULT_BPM_MIN
        defaultExerciseShouldNotBeFound("defaultBpmMin.lessThan=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin is less than (DEFAULT_DEFAULT_BPM_MIN + 1)
        defaultExerciseShouldBeFound("defaultBpmMin.lessThan=" + (DEFAULT_DEFAULT_BPM_MIN + 1));
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMin is greater than DEFAULT_DEFAULT_BPM_MIN
        defaultExerciseShouldNotBeFound("defaultBpmMin.greaterThan=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the exerciseList where defaultBpmMin is greater than SMALLER_DEFAULT_BPM_MIN
        defaultExerciseShouldBeFound("defaultBpmMin.greaterThan=" + SMALLER_DEFAULT_BPM_MIN);
    }


    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax equals to DEFAULT_DEFAULT_BPM_MAX
        defaultExerciseShouldBeFound("defaultBpmMax.equals=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax equals to UPDATED_DEFAULT_BPM_MAX
        defaultExerciseShouldNotBeFound("defaultBpmMax.equals=" + UPDATED_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax not equals to DEFAULT_DEFAULT_BPM_MAX
        defaultExerciseShouldNotBeFound("defaultBpmMax.notEquals=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax not equals to UPDATED_DEFAULT_BPM_MAX
        defaultExerciseShouldBeFound("defaultBpmMax.notEquals=" + UPDATED_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax in DEFAULT_DEFAULT_BPM_MAX or UPDATED_DEFAULT_BPM_MAX
        defaultExerciseShouldBeFound("defaultBpmMax.in=" + DEFAULT_DEFAULT_BPM_MAX + "," + UPDATED_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax equals to UPDATED_DEFAULT_BPM_MAX
        defaultExerciseShouldNotBeFound("defaultBpmMax.in=" + UPDATED_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax is not null
        defaultExerciseShouldBeFound("defaultBpmMax.specified=true");

        // Get all the exerciseList where defaultBpmMax is null
        defaultExerciseShouldNotBeFound("defaultBpmMax.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax is greater than or equal to DEFAULT_DEFAULT_BPM_MAX
        defaultExerciseShouldBeFound("defaultBpmMax.greaterThanOrEqual=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax is greater than or equal to (DEFAULT_DEFAULT_BPM_MAX + 1)
        defaultExerciseShouldNotBeFound("defaultBpmMax.greaterThanOrEqual=" + (DEFAULT_DEFAULT_BPM_MAX + 1));
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax is less than or equal to DEFAULT_DEFAULT_BPM_MAX
        defaultExerciseShouldBeFound("defaultBpmMax.lessThanOrEqual=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax is less than or equal to SMALLER_DEFAULT_BPM_MAX
        defaultExerciseShouldNotBeFound("defaultBpmMax.lessThanOrEqual=" + SMALLER_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax is less than DEFAULT_DEFAULT_BPM_MAX
        defaultExerciseShouldNotBeFound("defaultBpmMax.lessThan=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax is less than (DEFAULT_DEFAULT_BPM_MAX + 1)
        defaultExerciseShouldBeFound("defaultBpmMax.lessThan=" + (DEFAULT_DEFAULT_BPM_MAX + 1));
    }

    @Test
    @Transactional
    public void getAllExercisesByDefaultBpmMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where defaultBpmMax is greater than DEFAULT_DEFAULT_BPM_MAX
        defaultExerciseShouldNotBeFound("defaultBpmMax.greaterThan=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the exerciseList where defaultBpmMax is greater than SMALLER_DEFAULT_BPM_MAX
        defaultExerciseShouldBeFound("defaultBpmMax.greaterThan=" + SMALLER_DEFAULT_BPM_MAX);
    }


    @Test
    @Transactional
    public void getAllExercisesByDeactivtedIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where deactivted equals to DEFAULT_DEACTIVTED
        defaultExerciseShouldBeFound("deactivted.equals=" + DEFAULT_DEACTIVTED);

        // Get all the exerciseList where deactivted equals to UPDATED_DEACTIVTED
        defaultExerciseShouldNotBeFound("deactivted.equals=" + UPDATED_DEACTIVTED);
    }

    @Test
    @Transactional
    public void getAllExercisesByDeactivtedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where deactivted not equals to DEFAULT_DEACTIVTED
        defaultExerciseShouldNotBeFound("deactivted.notEquals=" + DEFAULT_DEACTIVTED);

        // Get all the exerciseList where deactivted not equals to UPDATED_DEACTIVTED
        defaultExerciseShouldBeFound("deactivted.notEquals=" + UPDATED_DEACTIVTED);
    }

    @Test
    @Transactional
    public void getAllExercisesByDeactivtedIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where deactivted in DEFAULT_DEACTIVTED or UPDATED_DEACTIVTED
        defaultExerciseShouldBeFound("deactivted.in=" + DEFAULT_DEACTIVTED + "," + UPDATED_DEACTIVTED);

        // Get all the exerciseList where deactivted equals to UPDATED_DEACTIVTED
        defaultExerciseShouldNotBeFound("deactivted.in=" + UPDATED_DEACTIVTED);
    }

    @Test
    @Transactional
    public void getAllExercisesByDeactivtedIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where deactivted is not null
        defaultExerciseShouldBeFound("deactivted.specified=true");

        // Get all the exerciseList where deactivted is null
        defaultExerciseShouldNotBeFound("deactivted.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where createDate equals to DEFAULT_CREATE_DATE
        defaultExerciseShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the exerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultExerciseShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExercisesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where createDate not equals to DEFAULT_CREATE_DATE
        defaultExerciseShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the exerciseList where createDate not equals to UPDATED_CREATE_DATE
        defaultExerciseShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExercisesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultExerciseShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the exerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultExerciseShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExercisesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where createDate is not null
        defaultExerciseShouldBeFound("createDate.specified=true");

        // Get all the exerciseList where createDate is null
        defaultExerciseShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultExerciseShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the exerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExerciseShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExercisesByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultExerciseShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the exerciseList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultExerciseShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExercisesByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultExerciseShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the exerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExerciseShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExercisesByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where modifyDate is not null
        defaultExerciseShouldBeFound("modifyDate.specified=true");

        // Get all the exerciseList where modifyDate is null
        defaultExerciseShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesBySkillTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where skillType equals to DEFAULT_SKILL_TYPE
        defaultExerciseShouldBeFound("skillType.equals=" + DEFAULT_SKILL_TYPE);

        // Get all the exerciseList where skillType equals to UPDATED_SKILL_TYPE
        defaultExerciseShouldNotBeFound("skillType.equals=" + UPDATED_SKILL_TYPE);
    }

    @Test
    @Transactional
    public void getAllExercisesBySkillTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where skillType not equals to DEFAULT_SKILL_TYPE
        defaultExerciseShouldNotBeFound("skillType.notEquals=" + DEFAULT_SKILL_TYPE);

        // Get all the exerciseList where skillType not equals to UPDATED_SKILL_TYPE
        defaultExerciseShouldBeFound("skillType.notEquals=" + UPDATED_SKILL_TYPE);
    }

    @Test
    @Transactional
    public void getAllExercisesBySkillTypeIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where skillType in DEFAULT_SKILL_TYPE or UPDATED_SKILL_TYPE
        defaultExerciseShouldBeFound("skillType.in=" + DEFAULT_SKILL_TYPE + "," + UPDATED_SKILL_TYPE);

        // Get all the exerciseList where skillType equals to UPDATED_SKILL_TYPE
        defaultExerciseShouldNotBeFound("skillType.in=" + UPDATED_SKILL_TYPE);
    }

    @Test
    @Transactional
    public void getAllExercisesBySkillTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where skillType is not null
        defaultExerciseShouldBeFound("skillType.specified=true");

        // Get all the exerciseList where skillType is null
        defaultExerciseShouldNotBeFound("skillType.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exercise equals to DEFAULT_EXERCISE
        defaultExerciseShouldBeFound("exercise.equals=" + DEFAULT_EXERCISE);

        // Get all the exerciseList where exercise equals to UPDATED_EXERCISE
        defaultExerciseShouldNotBeFound("exercise.equals=" + UPDATED_EXERCISE);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exercise not equals to DEFAULT_EXERCISE
        defaultExerciseShouldNotBeFound("exercise.notEquals=" + DEFAULT_EXERCISE);

        // Get all the exerciseList where exercise not equals to UPDATED_EXERCISE
        defaultExerciseShouldBeFound("exercise.notEquals=" + UPDATED_EXERCISE);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseIsInShouldWork() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exercise in DEFAULT_EXERCISE or UPDATED_EXERCISE
        defaultExerciseShouldBeFound("exercise.in=" + DEFAULT_EXERCISE + "," + UPDATED_EXERCISE);

        // Get all the exerciseList where exercise equals to UPDATED_EXERCISE
        defaultExerciseShouldNotBeFound("exercise.in=" + UPDATED_EXERCISE);
    }

    @Test
    @Transactional
    public void getAllExercisesByExerciseIsNullOrNotNull() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        // Get all the exerciseList where exercise is not null
        defaultExerciseShouldBeFound("exercise.specified=true");

        // Get all the exerciseList where exercise is null
        defaultExerciseShouldNotBeFound("exercise.specified=false");
    }

    @Test
    @Transactional
    public void getAllExercisesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);
        User creator = UserResourceIT.createEntity(em);
        em.persist(creator);
        em.flush();
        exercise.setCreator(creator);
        exerciseRepository.saveAndFlush(exercise);
        Long creatorId = creator.getId();

        // Get all the exerciseList where creator equals to creatorId
        defaultExerciseShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the exerciseList where creator equals to creatorId + 1
        defaultExerciseShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExerciseShouldBeFound(String filter) throws Exception {
        restExerciseMockMvc.perform(get("/api/exercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].exerciseName").value(hasItem(DEFAULT_EXERCISE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].defaultMinutes").value(hasItem(DEFAULT_DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].defaultBpmMin").value(hasItem(DEFAULT_DEFAULT_BPM_MIN)))
            .andExpect(jsonPath("$.[*].defaultBpmMax").value(hasItem(DEFAULT_DEFAULT_BPM_MAX)))
            .andExpect(jsonPath("$.[*].deactivted").value(hasItem(DEFAULT_DEACTIVTED.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())))
            .andExpect(jsonPath("$.[*].skillType").value(hasItem(DEFAULT_SKILL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].exercise").value(hasItem(DEFAULT_EXERCISE.toString())));

        // Check, that the count call also returns 1
        restExerciseMockMvc.perform(get("/api/exercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExerciseShouldNotBeFound(String filter) throws Exception {
        restExerciseMockMvc.perform(get("/api/exercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExerciseMockMvc.perform(get("/api/exercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExercise() throws Exception {
        // Get the exercise
        restExerciseMockMvc.perform(get("/api/exercises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Update the exercise
        Exercise updatedExercise = exerciseRepository.findById(exercise.getId()).get();
        // Disconnect from session so that the updates on updatedExercise are not directly saved in db
        em.detach(updatedExercise);
        updatedExercise
            .exerciseName(UPDATED_EXERCISE_NAME)
            .description(UPDATED_DESCRIPTION)
            .sourceUrl(UPDATED_SOURCE_URL)
            .defaultMinutes(UPDATED_DEFAULT_MINUTES)
            .defaultBpmMin(UPDATED_DEFAULT_BPM_MIN)
            .defaultBpmMax(UPDATED_DEFAULT_BPM_MAX)
            .deactivted(UPDATED_DEACTIVTED)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE)
            .skillType(UPDATED_SKILL_TYPE)
            .exercise(UPDATED_EXERCISE);
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(updatedExercise);

        restExerciseMockMvc.perform(put("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isOk());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
        Exercise testExercise = exerciseList.get(exerciseList.size() - 1);
        assertThat(testExercise.getExerciseName()).isEqualTo(UPDATED_EXERCISE_NAME);
        assertThat(testExercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExercise.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testExercise.getDefaultMinutes()).isEqualTo(UPDATED_DEFAULT_MINUTES);
        assertThat(testExercise.getDefaultBpmMin()).isEqualTo(UPDATED_DEFAULT_BPM_MIN);
        assertThat(testExercise.getDefaultBpmMax()).isEqualTo(UPDATED_DEFAULT_BPM_MAX);
        assertThat(testExercise.isDeactivted()).isEqualTo(UPDATED_DEACTIVTED);
        assertThat(testExercise.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testExercise.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
        assertThat(testExercise.getSkillType()).isEqualTo(UPDATED_SKILL_TYPE);
        assertThat(testExercise.getExercise()).isEqualTo(UPDATED_EXERCISE);
    }

    @Test
    @Transactional
    public void updateNonExistingExercise() throws Exception {
        int databaseSizeBeforeUpdate = exerciseRepository.findAll().size();

        // Create the Exercise
        ExerciseDTO exerciseDTO = exerciseMapper.toDto(exercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExerciseMockMvc.perform(put("/api/exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Exercise in the database
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExercise() throws Exception {
        // Initialize the database
        exerciseRepository.saveAndFlush(exercise);

        int databaseSizeBeforeDelete = exerciseRepository.findAll().size();

        // Delete the exercise
        restExerciseMockMvc.perform(delete("/api/exercises/{id}", exercise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exercise> exerciseList = exerciseRepository.findAll();
        assertThat(exerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exercise.class);
        Exercise exercise1 = new Exercise();
        exercise1.setId(1L);
        Exercise exercise2 = new Exercise();
        exercise2.setId(exercise1.getId());
        assertThat(exercise1).isEqualTo(exercise2);
        exercise2.setId(2L);
        assertThat(exercise1).isNotEqualTo(exercise2);
        exercise1.setId(null);
        assertThat(exercise1).isNotEqualTo(exercise2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExerciseDTO.class);
        ExerciseDTO exerciseDTO1 = new ExerciseDTO();
        exerciseDTO1.setId(1L);
        ExerciseDTO exerciseDTO2 = new ExerciseDTO();
        assertThat(exerciseDTO1).isNotEqualTo(exerciseDTO2);
        exerciseDTO2.setId(exerciseDTO1.getId());
        assertThat(exerciseDTO1).isEqualTo(exerciseDTO2);
        exerciseDTO2.setId(2L);
        assertThat(exerciseDTO1).isNotEqualTo(exerciseDTO2);
        exerciseDTO1.setId(null);
        assertThat(exerciseDTO1).isNotEqualTo(exerciseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(exerciseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(exerciseMapper.fromId(null)).isNull();
    }
}
