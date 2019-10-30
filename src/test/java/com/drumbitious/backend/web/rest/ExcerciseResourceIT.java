package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.Excercise;
import com.drumbitious.backend.domain.User;
import com.drumbitious.backend.domain.SkillType;
import com.drumbitious.backend.domain.ExcerciseType;
import com.drumbitious.backend.repository.ExcerciseRepository;
import com.drumbitious.backend.service.ExcerciseService;
import com.drumbitious.backend.service.dto.ExcerciseDTO;
import com.drumbitious.backend.service.mapper.ExcerciseMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.ExcerciseCriteria;
import com.drumbitious.backend.service.ExcerciseQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

import static com.drumbitious.backend.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ExcerciseResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class ExcerciseResourceIT {

    private static final String DEFAULT_EXCERCISE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXCERCISE_NAME = "BBBBBBBBBB";

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

    @Autowired
    private ExcerciseRepository excerciseRepository;

    @Mock
    private ExcerciseRepository excerciseRepositoryMock;

    @Autowired
    private ExcerciseMapper excerciseMapper;

    @Mock
    private ExcerciseService excerciseServiceMock;

    @Autowired
    private ExcerciseService excerciseService;

    @Autowired
    private ExcerciseQueryService excerciseQueryService;

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

    private MockMvc restExcerciseMockMvc;

    private Excercise excercise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExcerciseResource excerciseResource = new ExcerciseResource(excerciseService, excerciseQueryService);
        this.restExcerciseMockMvc = MockMvcBuilders.standaloneSetup(excerciseResource)
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
    public static Excercise createEntity(EntityManager em) {
        Excercise excercise = new Excercise()
            .excerciseName(DEFAULT_EXCERCISE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .sourceUrl(DEFAULT_SOURCE_URL)
            .defaultMinutes(DEFAULT_DEFAULT_MINUTES)
            .defaultBpmMin(DEFAULT_DEFAULT_BPM_MIN)
            .defaultBpmMax(DEFAULT_DEFAULT_BPM_MAX)
            .deactivted(DEFAULT_DEACTIVTED)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return excercise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Excercise createUpdatedEntity(EntityManager em) {
        Excercise excercise = new Excercise()
            .excerciseName(UPDATED_EXCERCISE_NAME)
            .description(UPDATED_DESCRIPTION)
            .sourceUrl(UPDATED_SOURCE_URL)
            .defaultMinutes(UPDATED_DEFAULT_MINUTES)
            .defaultBpmMin(UPDATED_DEFAULT_BPM_MIN)
            .defaultBpmMax(UPDATED_DEFAULT_BPM_MAX)
            .deactivted(UPDATED_DEACTIVTED)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return excercise;
    }

    @BeforeEach
    public void initTest() {
        excercise = createEntity(em);
    }

    @Test
    @Transactional
    public void createExcercise() throws Exception {
        int databaseSizeBeforeCreate = excerciseRepository.findAll().size();

        // Create the Excercise
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(excercise);
        restExcerciseMockMvc.perform(post("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isCreated());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeCreate + 1);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getExcerciseName()).isEqualTo(DEFAULT_EXCERCISE_NAME);
        assertThat(testExcercise.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testExcercise.getSourceUrl()).isEqualTo(DEFAULT_SOURCE_URL);
        assertThat(testExcercise.getDefaultMinutes()).isEqualTo(DEFAULT_DEFAULT_MINUTES);
        assertThat(testExcercise.getDefaultBpmMin()).isEqualTo(DEFAULT_DEFAULT_BPM_MIN);
        assertThat(testExcercise.getDefaultBpmMax()).isEqualTo(DEFAULT_DEFAULT_BPM_MAX);
        assertThat(testExcercise.isDeactivted()).isEqualTo(DEFAULT_DEACTIVTED);
        assertThat(testExcercise.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testExcercise.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createExcerciseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = excerciseRepository.findAll().size();

        // Create the Excercise with an existing ID
        excercise.setId(1L);
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(excercise);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExcerciseMockMvc.perform(post("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkExcerciseNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = excerciseRepository.findAll().size();
        // set the field null
        excercise.setExcerciseName(null);

        // Create the Excercise, which fails.
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(excercise);

        restExcerciseMockMvc.perform(post("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = excerciseRepository.findAll().size();
        // set the field null
        excercise.setCreateDate(null);

        // Create the Excercise, which fails.
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(excercise);

        restExcerciseMockMvc.perform(post("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = excerciseRepository.findAll().size();
        // set the field null
        excercise.setModifyDate(null);

        // Create the Excercise, which fails.
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(excercise);

        restExcerciseMockMvc.perform(post("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isBadRequest());

        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExcercises() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList
        restExcerciseMockMvc.perform(get("/api/excercises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].excerciseName").value(hasItem(DEFAULT_EXCERCISE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].defaultMinutes").value(hasItem(DEFAULT_DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].defaultBpmMin").value(hasItem(DEFAULT_DEFAULT_BPM_MIN)))
            .andExpect(jsonPath("$.[*].defaultBpmMax").value(hasItem(DEFAULT_DEFAULT_BPM_MAX)))
            .andExpect(jsonPath("$.[*].deactivted").value(hasItem(DEFAULT_DEACTIVTED.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllExcercisesWithEagerRelationshipsIsEnabled() throws Exception {
        ExcerciseResource excerciseResource = new ExcerciseResource(excerciseServiceMock, excerciseQueryService);
        when(excerciseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restExcerciseMockMvc = MockMvcBuilders.standaloneSetup(excerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restExcerciseMockMvc.perform(get("/api/excercises?eagerload=true"))
        .andExpect(status().isOk());

        verify(excerciseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllExcercisesWithEagerRelationshipsIsNotEnabled() throws Exception {
        ExcerciseResource excerciseResource = new ExcerciseResource(excerciseServiceMock, excerciseQueryService);
            when(excerciseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restExcerciseMockMvc = MockMvcBuilders.standaloneSetup(excerciseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restExcerciseMockMvc.perform(get("/api/excercises?eagerload=true"))
        .andExpect(status().isOk());

            verify(excerciseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get the excercise
        restExcerciseMockMvc.perform(get("/api/excercises/{id}", excercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(excercise.getId().intValue()))
            .andExpect(jsonPath("$.excerciseName").value(DEFAULT_EXCERCISE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.sourceUrl").value(DEFAULT_SOURCE_URL))
            .andExpect(jsonPath("$.defaultMinutes").value(DEFAULT_DEFAULT_MINUTES))
            .andExpect(jsonPath("$.defaultBpmMin").value(DEFAULT_DEFAULT_BPM_MIN))
            .andExpect(jsonPath("$.defaultBpmMax").value(DEFAULT_DEFAULT_BPM_MAX))
            .andExpect(jsonPath("$.deactivted").value(DEFAULT_DEACTIVTED.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllExcercisesByExcerciseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where excerciseName equals to DEFAULT_EXCERCISE_NAME
        defaultExcerciseShouldBeFound("excerciseName.equals=" + DEFAULT_EXCERCISE_NAME);

        // Get all the excerciseList where excerciseName equals to UPDATED_EXCERCISE_NAME
        defaultExcerciseShouldNotBeFound("excerciseName.equals=" + UPDATED_EXCERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExcercisesByExcerciseNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where excerciseName not equals to DEFAULT_EXCERCISE_NAME
        defaultExcerciseShouldNotBeFound("excerciseName.notEquals=" + DEFAULT_EXCERCISE_NAME);

        // Get all the excerciseList where excerciseName not equals to UPDATED_EXCERCISE_NAME
        defaultExcerciseShouldBeFound("excerciseName.notEquals=" + UPDATED_EXCERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExcercisesByExcerciseNameIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where excerciseName in DEFAULT_EXCERCISE_NAME or UPDATED_EXCERCISE_NAME
        defaultExcerciseShouldBeFound("excerciseName.in=" + DEFAULT_EXCERCISE_NAME + "," + UPDATED_EXCERCISE_NAME);

        // Get all the excerciseList where excerciseName equals to UPDATED_EXCERCISE_NAME
        defaultExcerciseShouldNotBeFound("excerciseName.in=" + UPDATED_EXCERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExcercisesByExcerciseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where excerciseName is not null
        defaultExcerciseShouldBeFound("excerciseName.specified=true");

        // Get all the excerciseList where excerciseName is null
        defaultExcerciseShouldNotBeFound("excerciseName.specified=false");
    }
                @Test
    @Transactional
    public void getAllExcercisesByExcerciseNameContainsSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where excerciseName contains DEFAULT_EXCERCISE_NAME
        defaultExcerciseShouldBeFound("excerciseName.contains=" + DEFAULT_EXCERCISE_NAME);

        // Get all the excerciseList where excerciseName contains UPDATED_EXCERCISE_NAME
        defaultExcerciseShouldNotBeFound("excerciseName.contains=" + UPDATED_EXCERCISE_NAME);
    }

    @Test
    @Transactional
    public void getAllExcercisesByExcerciseNameNotContainsSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where excerciseName does not contain DEFAULT_EXCERCISE_NAME
        defaultExcerciseShouldNotBeFound("excerciseName.doesNotContain=" + DEFAULT_EXCERCISE_NAME);

        // Get all the excerciseList where excerciseName does not contain UPDATED_EXCERCISE_NAME
        defaultExcerciseShouldBeFound("excerciseName.doesNotContain=" + UPDATED_EXCERCISE_NAME);
    }


    @Test
    @Transactional
    public void getAllExcercisesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where description equals to DEFAULT_DESCRIPTION
        defaultExcerciseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the excerciseList where description equals to UPDATED_DESCRIPTION
        defaultExcerciseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where description not equals to DEFAULT_DESCRIPTION
        defaultExcerciseShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the excerciseList where description not equals to UPDATED_DESCRIPTION
        defaultExcerciseShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultExcerciseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the excerciseList where description equals to UPDATED_DESCRIPTION
        defaultExcerciseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where description is not null
        defaultExcerciseShouldBeFound("description.specified=true");

        // Get all the excerciseList where description is null
        defaultExcerciseShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllExcercisesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where description contains DEFAULT_DESCRIPTION
        defaultExcerciseShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the excerciseList where description contains UPDATED_DESCRIPTION
        defaultExcerciseShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where description does not contain DEFAULT_DESCRIPTION
        defaultExcerciseShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the excerciseList where description does not contain UPDATED_DESCRIPTION
        defaultExcerciseShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllExcercisesBySourceUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where sourceUrl equals to DEFAULT_SOURCE_URL
        defaultExcerciseShouldBeFound("sourceUrl.equals=" + DEFAULT_SOURCE_URL);

        // Get all the excerciseList where sourceUrl equals to UPDATED_SOURCE_URL
        defaultExcerciseShouldNotBeFound("sourceUrl.equals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExcercisesBySourceUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where sourceUrl not equals to DEFAULT_SOURCE_URL
        defaultExcerciseShouldNotBeFound("sourceUrl.notEquals=" + DEFAULT_SOURCE_URL);

        // Get all the excerciseList where sourceUrl not equals to UPDATED_SOURCE_URL
        defaultExcerciseShouldBeFound("sourceUrl.notEquals=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExcercisesBySourceUrlIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where sourceUrl in DEFAULT_SOURCE_URL or UPDATED_SOURCE_URL
        defaultExcerciseShouldBeFound("sourceUrl.in=" + DEFAULT_SOURCE_URL + "," + UPDATED_SOURCE_URL);

        // Get all the excerciseList where sourceUrl equals to UPDATED_SOURCE_URL
        defaultExcerciseShouldNotBeFound("sourceUrl.in=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExcercisesBySourceUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where sourceUrl is not null
        defaultExcerciseShouldBeFound("sourceUrl.specified=true");

        // Get all the excerciseList where sourceUrl is null
        defaultExcerciseShouldNotBeFound("sourceUrl.specified=false");
    }
                @Test
    @Transactional
    public void getAllExcercisesBySourceUrlContainsSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where sourceUrl contains DEFAULT_SOURCE_URL
        defaultExcerciseShouldBeFound("sourceUrl.contains=" + DEFAULT_SOURCE_URL);

        // Get all the excerciseList where sourceUrl contains UPDATED_SOURCE_URL
        defaultExcerciseShouldNotBeFound("sourceUrl.contains=" + UPDATED_SOURCE_URL);
    }

    @Test
    @Transactional
    public void getAllExcercisesBySourceUrlNotContainsSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where sourceUrl does not contain DEFAULT_SOURCE_URL
        defaultExcerciseShouldNotBeFound("sourceUrl.doesNotContain=" + DEFAULT_SOURCE_URL);

        // Get all the excerciseList where sourceUrl does not contain UPDATED_SOURCE_URL
        defaultExcerciseShouldBeFound("sourceUrl.doesNotContain=" + UPDATED_SOURCE_URL);
    }


    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes equals to DEFAULT_DEFAULT_MINUTES
        defaultExcerciseShouldBeFound("defaultMinutes.equals=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes equals to UPDATED_DEFAULT_MINUTES
        defaultExcerciseShouldNotBeFound("defaultMinutes.equals=" + UPDATED_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes not equals to DEFAULT_DEFAULT_MINUTES
        defaultExcerciseShouldNotBeFound("defaultMinutes.notEquals=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes not equals to UPDATED_DEFAULT_MINUTES
        defaultExcerciseShouldBeFound("defaultMinutes.notEquals=" + UPDATED_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes in DEFAULT_DEFAULT_MINUTES or UPDATED_DEFAULT_MINUTES
        defaultExcerciseShouldBeFound("defaultMinutes.in=" + DEFAULT_DEFAULT_MINUTES + "," + UPDATED_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes equals to UPDATED_DEFAULT_MINUTES
        defaultExcerciseShouldNotBeFound("defaultMinutes.in=" + UPDATED_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes is not null
        defaultExcerciseShouldBeFound("defaultMinutes.specified=true");

        // Get all the excerciseList where defaultMinutes is null
        defaultExcerciseShouldNotBeFound("defaultMinutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes is greater than or equal to DEFAULT_DEFAULT_MINUTES
        defaultExcerciseShouldBeFound("defaultMinutes.greaterThanOrEqual=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes is greater than or equal to (DEFAULT_DEFAULT_MINUTES + 1)
        defaultExcerciseShouldNotBeFound("defaultMinutes.greaterThanOrEqual=" + (DEFAULT_DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes is less than or equal to DEFAULT_DEFAULT_MINUTES
        defaultExcerciseShouldBeFound("defaultMinutes.lessThanOrEqual=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes is less than or equal to SMALLER_DEFAULT_MINUTES
        defaultExcerciseShouldNotBeFound("defaultMinutes.lessThanOrEqual=" + SMALLER_DEFAULT_MINUTES);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes is less than DEFAULT_DEFAULT_MINUTES
        defaultExcerciseShouldNotBeFound("defaultMinutes.lessThan=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes is less than (DEFAULT_DEFAULT_MINUTES + 1)
        defaultExcerciseShouldBeFound("defaultMinutes.lessThan=" + (DEFAULT_DEFAULT_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultMinutes is greater than DEFAULT_DEFAULT_MINUTES
        defaultExcerciseShouldNotBeFound("defaultMinutes.greaterThan=" + DEFAULT_DEFAULT_MINUTES);

        // Get all the excerciseList where defaultMinutes is greater than SMALLER_DEFAULT_MINUTES
        defaultExcerciseShouldBeFound("defaultMinutes.greaterThan=" + SMALLER_DEFAULT_MINUTES);
    }


    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin equals to DEFAULT_DEFAULT_BPM_MIN
        defaultExcerciseShouldBeFound("defaultBpmMin.equals=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin equals to UPDATED_DEFAULT_BPM_MIN
        defaultExcerciseShouldNotBeFound("defaultBpmMin.equals=" + UPDATED_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin not equals to DEFAULT_DEFAULT_BPM_MIN
        defaultExcerciseShouldNotBeFound("defaultBpmMin.notEquals=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin not equals to UPDATED_DEFAULT_BPM_MIN
        defaultExcerciseShouldBeFound("defaultBpmMin.notEquals=" + UPDATED_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin in DEFAULT_DEFAULT_BPM_MIN or UPDATED_DEFAULT_BPM_MIN
        defaultExcerciseShouldBeFound("defaultBpmMin.in=" + DEFAULT_DEFAULT_BPM_MIN + "," + UPDATED_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin equals to UPDATED_DEFAULT_BPM_MIN
        defaultExcerciseShouldNotBeFound("defaultBpmMin.in=" + UPDATED_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin is not null
        defaultExcerciseShouldBeFound("defaultBpmMin.specified=true");

        // Get all the excerciseList where defaultBpmMin is null
        defaultExcerciseShouldNotBeFound("defaultBpmMin.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin is greater than or equal to DEFAULT_DEFAULT_BPM_MIN
        defaultExcerciseShouldBeFound("defaultBpmMin.greaterThanOrEqual=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin is greater than or equal to (DEFAULT_DEFAULT_BPM_MIN + 1)
        defaultExcerciseShouldNotBeFound("defaultBpmMin.greaterThanOrEqual=" + (DEFAULT_DEFAULT_BPM_MIN + 1));
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin is less than or equal to DEFAULT_DEFAULT_BPM_MIN
        defaultExcerciseShouldBeFound("defaultBpmMin.lessThanOrEqual=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin is less than or equal to SMALLER_DEFAULT_BPM_MIN
        defaultExcerciseShouldNotBeFound("defaultBpmMin.lessThanOrEqual=" + SMALLER_DEFAULT_BPM_MIN);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsLessThanSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin is less than DEFAULT_DEFAULT_BPM_MIN
        defaultExcerciseShouldNotBeFound("defaultBpmMin.lessThan=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin is less than (DEFAULT_DEFAULT_BPM_MIN + 1)
        defaultExcerciseShouldBeFound("defaultBpmMin.lessThan=" + (DEFAULT_DEFAULT_BPM_MIN + 1));
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMin is greater than DEFAULT_DEFAULT_BPM_MIN
        defaultExcerciseShouldNotBeFound("defaultBpmMin.greaterThan=" + DEFAULT_DEFAULT_BPM_MIN);

        // Get all the excerciseList where defaultBpmMin is greater than SMALLER_DEFAULT_BPM_MIN
        defaultExcerciseShouldBeFound("defaultBpmMin.greaterThan=" + SMALLER_DEFAULT_BPM_MIN);
    }


    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax equals to DEFAULT_DEFAULT_BPM_MAX
        defaultExcerciseShouldBeFound("defaultBpmMax.equals=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax equals to UPDATED_DEFAULT_BPM_MAX
        defaultExcerciseShouldNotBeFound("defaultBpmMax.equals=" + UPDATED_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax not equals to DEFAULT_DEFAULT_BPM_MAX
        defaultExcerciseShouldNotBeFound("defaultBpmMax.notEquals=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax not equals to UPDATED_DEFAULT_BPM_MAX
        defaultExcerciseShouldBeFound("defaultBpmMax.notEquals=" + UPDATED_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax in DEFAULT_DEFAULT_BPM_MAX or UPDATED_DEFAULT_BPM_MAX
        defaultExcerciseShouldBeFound("defaultBpmMax.in=" + DEFAULT_DEFAULT_BPM_MAX + "," + UPDATED_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax equals to UPDATED_DEFAULT_BPM_MAX
        defaultExcerciseShouldNotBeFound("defaultBpmMax.in=" + UPDATED_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax is not null
        defaultExcerciseShouldBeFound("defaultBpmMax.specified=true");

        // Get all the excerciseList where defaultBpmMax is null
        defaultExcerciseShouldNotBeFound("defaultBpmMax.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax is greater than or equal to DEFAULT_DEFAULT_BPM_MAX
        defaultExcerciseShouldBeFound("defaultBpmMax.greaterThanOrEqual=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax is greater than or equal to (DEFAULT_DEFAULT_BPM_MAX + 1)
        defaultExcerciseShouldNotBeFound("defaultBpmMax.greaterThanOrEqual=" + (DEFAULT_DEFAULT_BPM_MAX + 1));
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax is less than or equal to DEFAULT_DEFAULT_BPM_MAX
        defaultExcerciseShouldBeFound("defaultBpmMax.lessThanOrEqual=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax is less than or equal to SMALLER_DEFAULT_BPM_MAX
        defaultExcerciseShouldNotBeFound("defaultBpmMax.lessThanOrEqual=" + SMALLER_DEFAULT_BPM_MAX);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsLessThanSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax is less than DEFAULT_DEFAULT_BPM_MAX
        defaultExcerciseShouldNotBeFound("defaultBpmMax.lessThan=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax is less than (DEFAULT_DEFAULT_BPM_MAX + 1)
        defaultExcerciseShouldBeFound("defaultBpmMax.lessThan=" + (DEFAULT_DEFAULT_BPM_MAX + 1));
    }

    @Test
    @Transactional
    public void getAllExcercisesByDefaultBpmMaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where defaultBpmMax is greater than DEFAULT_DEFAULT_BPM_MAX
        defaultExcerciseShouldNotBeFound("defaultBpmMax.greaterThan=" + DEFAULT_DEFAULT_BPM_MAX);

        // Get all the excerciseList where defaultBpmMax is greater than SMALLER_DEFAULT_BPM_MAX
        defaultExcerciseShouldBeFound("defaultBpmMax.greaterThan=" + SMALLER_DEFAULT_BPM_MAX);
    }


    @Test
    @Transactional
    public void getAllExcercisesByDeactivtedIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where deactivted equals to DEFAULT_DEACTIVTED
        defaultExcerciseShouldBeFound("deactivted.equals=" + DEFAULT_DEACTIVTED);

        // Get all the excerciseList where deactivted equals to UPDATED_DEACTIVTED
        defaultExcerciseShouldNotBeFound("deactivted.equals=" + UPDATED_DEACTIVTED);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDeactivtedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where deactivted not equals to DEFAULT_DEACTIVTED
        defaultExcerciseShouldNotBeFound("deactivted.notEquals=" + DEFAULT_DEACTIVTED);

        // Get all the excerciseList where deactivted not equals to UPDATED_DEACTIVTED
        defaultExcerciseShouldBeFound("deactivted.notEquals=" + UPDATED_DEACTIVTED);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDeactivtedIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where deactivted in DEFAULT_DEACTIVTED or UPDATED_DEACTIVTED
        defaultExcerciseShouldBeFound("deactivted.in=" + DEFAULT_DEACTIVTED + "," + UPDATED_DEACTIVTED);

        // Get all the excerciseList where deactivted equals to UPDATED_DEACTIVTED
        defaultExcerciseShouldNotBeFound("deactivted.in=" + UPDATED_DEACTIVTED);
    }

    @Test
    @Transactional
    public void getAllExcercisesByDeactivtedIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where deactivted is not null
        defaultExcerciseShouldBeFound("deactivted.specified=true");

        // Get all the excerciseList where deactivted is null
        defaultExcerciseShouldNotBeFound("deactivted.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcercisesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where createDate equals to DEFAULT_CREATE_DATE
        defaultExcerciseShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the excerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultExcerciseShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExcercisesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where createDate not equals to DEFAULT_CREATE_DATE
        defaultExcerciseShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the excerciseList where createDate not equals to UPDATED_CREATE_DATE
        defaultExcerciseShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExcercisesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultExcerciseShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the excerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultExcerciseShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllExcercisesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where createDate is not null
        defaultExcerciseShouldBeFound("createDate.specified=true");

        // Get all the excerciseList where createDate is null
        defaultExcerciseShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcercisesByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultExcerciseShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the excerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExcerciseShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExcercisesByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultExcerciseShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the excerciseList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultExcerciseShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExcercisesByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultExcerciseShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the excerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultExcerciseShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllExcercisesByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        // Get all the excerciseList where modifyDate is not null
        defaultExcerciseShouldBeFound("modifyDate.specified=true");

        // Get all the excerciseList where modifyDate is null
        defaultExcerciseShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllExcercisesByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);
        User creator = UserResourceIT.createEntity(em);
        em.persist(creator);
        em.flush();
        excercise.setCreator(creator);
        excerciseRepository.saveAndFlush(excercise);
        Long creatorId = creator.getId();

        // Get all the excerciseList where creator equals to creatorId
        defaultExcerciseShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the excerciseList where creator equals to creatorId + 1
        defaultExcerciseShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllExcercisesBySkillNameIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);
        SkillType skillName = SkillTypeResourceIT.createEntity(em);
        em.persist(skillName);
        em.flush();
        excercise.addSkillName(skillName);
        excerciseRepository.saveAndFlush(excercise);
        Long skillNameId = skillName.getId();

        // Get all the excerciseList where skillName equals to skillNameId
        defaultExcerciseShouldBeFound("skillNameId.equals=" + skillNameId);

        // Get all the excerciseList where skillName equals to skillNameId + 1
        defaultExcerciseShouldNotBeFound("skillNameId.equals=" + (skillNameId + 1));
    }


    @Test
    @Transactional
    public void getAllExcercisesByExcerciseTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);
        ExcerciseType excerciseType = ExcerciseTypeResourceIT.createEntity(em);
        em.persist(excerciseType);
        em.flush();
        excercise.addExcerciseType(excerciseType);
        excerciseRepository.saveAndFlush(excercise);
        Long excerciseTypeId = excerciseType.getId();

        // Get all the excerciseList where excerciseType equals to excerciseTypeId
        defaultExcerciseShouldBeFound("excerciseTypeId.equals=" + excerciseTypeId);

        // Get all the excerciseList where excerciseType equals to excerciseTypeId + 1
        defaultExcerciseShouldNotBeFound("excerciseTypeId.equals=" + (excerciseTypeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExcerciseShouldBeFound(String filter) throws Exception {
        restExcerciseMockMvc.perform(get("/api/excercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(excercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].excerciseName").value(hasItem(DEFAULT_EXCERCISE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].sourceUrl").value(hasItem(DEFAULT_SOURCE_URL)))
            .andExpect(jsonPath("$.[*].defaultMinutes").value(hasItem(DEFAULT_DEFAULT_MINUTES)))
            .andExpect(jsonPath("$.[*].defaultBpmMin").value(hasItem(DEFAULT_DEFAULT_BPM_MIN)))
            .andExpect(jsonPath("$.[*].defaultBpmMax").value(hasItem(DEFAULT_DEFAULT_BPM_MAX)))
            .andExpect(jsonPath("$.[*].deactivted").value(hasItem(DEFAULT_DEACTIVTED.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restExcerciseMockMvc.perform(get("/api/excercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExcerciseShouldNotBeFound(String filter) throws Exception {
        restExcerciseMockMvc.perform(get("/api/excercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExcerciseMockMvc.perform(get("/api/excercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingExcercise() throws Exception {
        // Get the excercise
        restExcerciseMockMvc.perform(get("/api/excercises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Update the excercise
        Excercise updatedExcercise = excerciseRepository.findById(excercise.getId()).get();
        // Disconnect from session so that the updates on updatedExcercise are not directly saved in db
        em.detach(updatedExcercise);
        updatedExcercise
            .excerciseName(UPDATED_EXCERCISE_NAME)
            .description(UPDATED_DESCRIPTION)
            .sourceUrl(UPDATED_SOURCE_URL)
            .defaultMinutes(UPDATED_DEFAULT_MINUTES)
            .defaultBpmMin(UPDATED_DEFAULT_BPM_MIN)
            .defaultBpmMax(UPDATED_DEFAULT_BPM_MAX)
            .deactivted(UPDATED_DEACTIVTED)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(updatedExcercise);

        restExcerciseMockMvc.perform(put("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isOk());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
        Excercise testExcercise = excerciseList.get(excerciseList.size() - 1);
        assertThat(testExcercise.getExcerciseName()).isEqualTo(UPDATED_EXCERCISE_NAME);
        assertThat(testExcercise.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testExcercise.getSourceUrl()).isEqualTo(UPDATED_SOURCE_URL);
        assertThat(testExcercise.getDefaultMinutes()).isEqualTo(UPDATED_DEFAULT_MINUTES);
        assertThat(testExcercise.getDefaultBpmMin()).isEqualTo(UPDATED_DEFAULT_BPM_MIN);
        assertThat(testExcercise.getDefaultBpmMax()).isEqualTo(UPDATED_DEFAULT_BPM_MAX);
        assertThat(testExcercise.isDeactivted()).isEqualTo(UPDATED_DEACTIVTED);
        assertThat(testExcercise.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testExcercise.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingExcercise() throws Exception {
        int databaseSizeBeforeUpdate = excerciseRepository.findAll().size();

        // Create the Excercise
        ExcerciseDTO excerciseDTO = excerciseMapper.toDto(excercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExcerciseMockMvc.perform(put("/api/excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(excerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Excercise in the database
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExcercise() throws Exception {
        // Initialize the database
        excerciseRepository.saveAndFlush(excercise);

        int databaseSizeBeforeDelete = excerciseRepository.findAll().size();

        // Delete the excercise
        restExcerciseMockMvc.perform(delete("/api/excercises/{id}", excercise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Excercise> excerciseList = excerciseRepository.findAll();
        assertThat(excerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Excercise.class);
        Excercise excercise1 = new Excercise();
        excercise1.setId(1L);
        Excercise excercise2 = new Excercise();
        excercise2.setId(excercise1.getId());
        assertThat(excercise1).isEqualTo(excercise2);
        excercise2.setId(2L);
        assertThat(excercise1).isNotEqualTo(excercise2);
        excercise1.setId(null);
        assertThat(excercise1).isNotEqualTo(excercise2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExcerciseDTO.class);
        ExcerciseDTO excerciseDTO1 = new ExcerciseDTO();
        excerciseDTO1.setId(1L);
        ExcerciseDTO excerciseDTO2 = new ExcerciseDTO();
        assertThat(excerciseDTO1).isNotEqualTo(excerciseDTO2);
        excerciseDTO2.setId(excerciseDTO1.getId());
        assertThat(excerciseDTO1).isEqualTo(excerciseDTO2);
        excerciseDTO2.setId(2L);
        assertThat(excerciseDTO1).isNotEqualTo(excerciseDTO2);
        excerciseDTO1.setId(null);
        assertThat(excerciseDTO1).isNotEqualTo(excerciseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(excerciseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(excerciseMapper.fromId(null)).isNull();
    }
}
