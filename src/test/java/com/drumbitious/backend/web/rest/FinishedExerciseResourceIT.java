package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.FinishedExercise;
import com.drumbitious.backend.domain.FinishedSession;
import com.drumbitious.backend.domain.Exercise;
import com.drumbitious.backend.repository.FinishedExerciseRepository;
import com.drumbitious.backend.service.FinishedExerciseService;
import com.drumbitious.backend.service.dto.FinishedExerciseDTO;
import com.drumbitious.backend.service.mapper.FinishedExerciseMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.FinishedExerciseCriteria;
import com.drumbitious.backend.service.FinishedExerciseQueryService;

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
 * Integration tests for the {@link FinishedExerciseResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class FinishedExerciseResourceIT {

    private static final Integer DEFAULT_ACTUAL_BPM = 1;
    private static final Integer UPDATED_ACTUAL_BPM = 2;
    private static final Integer SMALLER_ACTUAL_BPM = 1 - 1;

    private static final Integer DEFAULT_ACTUAL_MINUTES = 1;
    private static final Integer UPDATED_ACTUAL_MINUTES = 2;
    private static final Integer SMALLER_ACTUAL_MINUTES = 1 - 1;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FinishedExerciseRepository finishedExerciseRepository;

    @Autowired
    private FinishedExerciseMapper finishedExerciseMapper;

    @Autowired
    private FinishedExerciseService finishedExerciseService;

    @Autowired
    private FinishedExerciseQueryService finishedExerciseQueryService;

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

    private MockMvc restFinishedExerciseMockMvc;

    private FinishedExercise finishedExercise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FinishedExerciseResource finishedExerciseResource = new FinishedExerciseResource(finishedExerciseService, finishedExerciseQueryService);
        this.restFinishedExerciseMockMvc = MockMvcBuilders.standaloneSetup(finishedExerciseResource)
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
    public static FinishedExercise createEntity(EntityManager em) {
        FinishedExercise finishedExercise = new FinishedExercise()
            .actualBpm(DEFAULT_ACTUAL_BPM)
            .actualMinutes(DEFAULT_ACTUAL_MINUTES)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return finishedExercise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinishedExercise createUpdatedEntity(EntityManager em) {
        FinishedExercise finishedExercise = new FinishedExercise()
            .actualBpm(UPDATED_ACTUAL_BPM)
            .actualMinutes(UPDATED_ACTUAL_MINUTES)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return finishedExercise;
    }

    @BeforeEach
    public void initTest() {
        finishedExercise = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinishedExercise() throws Exception {
        int databaseSizeBeforeCreate = finishedExerciseRepository.findAll().size();

        // Create the FinishedExercise
        FinishedExerciseDTO finishedExerciseDTO = finishedExerciseMapper.toDto(finishedExercise);
        restFinishedExerciseMockMvc.perform(post("/api/finished-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExerciseDTO)))
            .andExpect(status().isCreated());

        // Validate the FinishedExercise in the database
        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeCreate + 1);
        FinishedExercise testFinishedExercise = finishedExerciseList.get(finishedExerciseList.size() - 1);
        assertThat(testFinishedExercise.getActualBpm()).isEqualTo(DEFAULT_ACTUAL_BPM);
        assertThat(testFinishedExercise.getActualMinutes()).isEqualTo(DEFAULT_ACTUAL_MINUTES);
        assertThat(testFinishedExercise.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFinishedExercise.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createFinishedExerciseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = finishedExerciseRepository.findAll().size();

        // Create the FinishedExercise with an existing ID
        finishedExercise.setId(1L);
        FinishedExerciseDTO finishedExerciseDTO = finishedExerciseMapper.toDto(finishedExercise);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinishedExerciseMockMvc.perform(post("/api/finished-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinishedExercise in the database
        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = finishedExerciseRepository.findAll().size();
        // set the field null
        finishedExercise.setCreateDate(null);

        // Create the FinishedExercise, which fails.
        FinishedExerciseDTO finishedExerciseDTO = finishedExerciseMapper.toDto(finishedExercise);

        restFinishedExerciseMockMvc.perform(post("/api/finished-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExerciseDTO)))
            .andExpect(status().isBadRequest());

        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = finishedExerciseRepository.findAll().size();
        // set the field null
        finishedExercise.setModifyDate(null);

        // Create the FinishedExercise, which fails.
        FinishedExerciseDTO finishedExerciseDTO = finishedExerciseMapper.toDto(finishedExercise);

        restFinishedExerciseMockMvc.perform(post("/api/finished-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExerciseDTO)))
            .andExpect(status().isBadRequest());

        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFinishedExercises() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualBpm").value(hasItem(DEFAULT_ACTUAL_BPM)))
            .andExpect(jsonPath("$.[*].actualMinutes").value(hasItem(DEFAULT_ACTUAL_MINUTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getFinishedExercise() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get the finishedExercise
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises/{id}", finishedExercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(finishedExercise.getId().intValue()))
            .andExpect(jsonPath("$.actualBpm").value(DEFAULT_ACTUAL_BPM))
            .andExpect(jsonPath("$.actualMinutes").value(DEFAULT_ACTUAL_MINUTES))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm equals to DEFAULT_ACTUAL_BPM
        defaultFinishedExerciseShouldBeFound("actualBpm.equals=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm equals to UPDATED_ACTUAL_BPM
        defaultFinishedExerciseShouldNotBeFound("actualBpm.equals=" + UPDATED_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm not equals to DEFAULT_ACTUAL_BPM
        defaultFinishedExerciseShouldNotBeFound("actualBpm.notEquals=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm not equals to UPDATED_ACTUAL_BPM
        defaultFinishedExerciseShouldBeFound("actualBpm.notEquals=" + UPDATED_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm in DEFAULT_ACTUAL_BPM or UPDATED_ACTUAL_BPM
        defaultFinishedExerciseShouldBeFound("actualBpm.in=" + DEFAULT_ACTUAL_BPM + "," + UPDATED_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm equals to UPDATED_ACTUAL_BPM
        defaultFinishedExerciseShouldNotBeFound("actualBpm.in=" + UPDATED_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm is not null
        defaultFinishedExerciseShouldBeFound("actualBpm.specified=true");

        // Get all the finishedExerciseList where actualBpm is null
        defaultFinishedExerciseShouldNotBeFound("actualBpm.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm is greater than or equal to DEFAULT_ACTUAL_BPM
        defaultFinishedExerciseShouldBeFound("actualBpm.greaterThanOrEqual=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm is greater than or equal to (DEFAULT_ACTUAL_BPM + 1)
        defaultFinishedExerciseShouldNotBeFound("actualBpm.greaterThanOrEqual=" + (DEFAULT_ACTUAL_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm is less than or equal to DEFAULT_ACTUAL_BPM
        defaultFinishedExerciseShouldBeFound("actualBpm.lessThanOrEqual=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm is less than or equal to SMALLER_ACTUAL_BPM
        defaultFinishedExerciseShouldNotBeFound("actualBpm.lessThanOrEqual=" + SMALLER_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsLessThanSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm is less than DEFAULT_ACTUAL_BPM
        defaultFinishedExerciseShouldNotBeFound("actualBpm.lessThan=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm is less than (DEFAULT_ACTUAL_BPM + 1)
        defaultFinishedExerciseShouldBeFound("actualBpm.lessThan=" + (DEFAULT_ACTUAL_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualBpmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualBpm is greater than DEFAULT_ACTUAL_BPM
        defaultFinishedExerciseShouldNotBeFound("actualBpm.greaterThan=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExerciseList where actualBpm is greater than SMALLER_ACTUAL_BPM
        defaultFinishedExerciseShouldBeFound("actualBpm.greaterThan=" + SMALLER_ACTUAL_BPM);
    }


    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes equals to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExerciseShouldBeFound("actualMinutes.equals=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes equals to UPDATED_ACTUAL_MINUTES
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.equals=" + UPDATED_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes not equals to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.notEquals=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes not equals to UPDATED_ACTUAL_MINUTES
        defaultFinishedExerciseShouldBeFound("actualMinutes.notEquals=" + UPDATED_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes in DEFAULT_ACTUAL_MINUTES or UPDATED_ACTUAL_MINUTES
        defaultFinishedExerciseShouldBeFound("actualMinutes.in=" + DEFAULT_ACTUAL_MINUTES + "," + UPDATED_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes equals to UPDATED_ACTUAL_MINUTES
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.in=" + UPDATED_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes is not null
        defaultFinishedExerciseShouldBeFound("actualMinutes.specified=true");

        // Get all the finishedExerciseList where actualMinutes is null
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes is greater than or equal to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExerciseShouldBeFound("actualMinutes.greaterThanOrEqual=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes is greater than or equal to (DEFAULT_ACTUAL_MINUTES + 1)
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.greaterThanOrEqual=" + (DEFAULT_ACTUAL_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes is less than or equal to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExerciseShouldBeFound("actualMinutes.lessThanOrEqual=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes is less than or equal to SMALLER_ACTUAL_MINUTES
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.lessThanOrEqual=" + SMALLER_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes is less than DEFAULT_ACTUAL_MINUTES
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.lessThan=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes is less than (DEFAULT_ACTUAL_MINUTES + 1)
        defaultFinishedExerciseShouldBeFound("actualMinutes.lessThan=" + (DEFAULT_ACTUAL_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByActualMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where actualMinutes is greater than DEFAULT_ACTUAL_MINUTES
        defaultFinishedExerciseShouldNotBeFound("actualMinutes.greaterThan=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExerciseList where actualMinutes is greater than SMALLER_ACTUAL_MINUTES
        defaultFinishedExerciseShouldBeFound("actualMinutes.greaterThan=" + SMALLER_ACTUAL_MINUTES);
    }


    @Test
    @Transactional
    public void getAllFinishedExercisesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where createDate equals to DEFAULT_CREATE_DATE
        defaultFinishedExerciseShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the finishedExerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultFinishedExerciseShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where createDate not equals to DEFAULT_CREATE_DATE
        defaultFinishedExerciseShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the finishedExerciseList where createDate not equals to UPDATED_CREATE_DATE
        defaultFinishedExerciseShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultFinishedExerciseShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the finishedExerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultFinishedExerciseShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where createDate is not null
        defaultFinishedExerciseShouldBeFound("createDate.specified=true");

        // Get all the finishedExerciseList where createDate is null
        defaultFinishedExerciseShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultFinishedExerciseShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the finishedExerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultFinishedExerciseShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultFinishedExerciseShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the finishedExerciseList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultFinishedExerciseShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultFinishedExerciseShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the finishedExerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultFinishedExerciseShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        // Get all the finishedExerciseList where modifyDate is not null
        defaultFinishedExerciseShouldBeFound("modifyDate.specified=true");

        // Get all the finishedExerciseList where modifyDate is null
        defaultFinishedExerciseShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExercisesByFinishedSessionIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);
        FinishedSession finishedSession = FinishedSessionResourceIT.createEntity(em);
        em.persist(finishedSession);
        em.flush();
        finishedExercise.setFinishedSession(finishedSession);
        finishedExerciseRepository.saveAndFlush(finishedExercise);
        Long finishedSessionId = finishedSession.getId();

        // Get all the finishedExerciseList where finishedSession equals to finishedSessionId
        defaultFinishedExerciseShouldBeFound("finishedSessionId.equals=" + finishedSessionId);

        // Get all the finishedExerciseList where finishedSession equals to finishedSessionId + 1
        defaultFinishedExerciseShouldNotBeFound("finishedSessionId.equals=" + (finishedSessionId + 1));
    }


    @Test
    @Transactional
    public void getAllFinishedExercisesByExerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);
        Exercise exercise = ExerciseResourceIT.createEntity(em);
        em.persist(exercise);
        em.flush();
        finishedExercise.setExercise(exercise);
        finishedExerciseRepository.saveAndFlush(finishedExercise);
        Long exerciseId = exercise.getId();

        // Get all the finishedExerciseList where exercise equals to exerciseId
        defaultFinishedExerciseShouldBeFound("exerciseId.equals=" + exerciseId);

        // Get all the finishedExerciseList where exercise equals to exerciseId + 1
        defaultFinishedExerciseShouldNotBeFound("exerciseId.equals=" + (exerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinishedExerciseShouldBeFound(String filter) throws Exception {
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedExercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualBpm").value(hasItem(DEFAULT_ACTUAL_BPM)))
            .andExpect(jsonPath("$.[*].actualMinutes").value(hasItem(DEFAULT_ACTUAL_MINUTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinishedExerciseShouldNotBeFound(String filter) throws Exception {
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFinishedExercise() throws Exception {
        // Get the finishedExercise
        restFinishedExerciseMockMvc.perform(get("/api/finished-exercises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinishedExercise() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        int databaseSizeBeforeUpdate = finishedExerciseRepository.findAll().size();

        // Update the finishedExercise
        FinishedExercise updatedFinishedExercise = finishedExerciseRepository.findById(finishedExercise.getId()).get();
        // Disconnect from session so that the updates on updatedFinishedExercise are not directly saved in db
        em.detach(updatedFinishedExercise);
        updatedFinishedExercise
            .actualBpm(UPDATED_ACTUAL_BPM)
            .actualMinutes(UPDATED_ACTUAL_MINUTES)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        FinishedExerciseDTO finishedExerciseDTO = finishedExerciseMapper.toDto(updatedFinishedExercise);

        restFinishedExerciseMockMvc.perform(put("/api/finished-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExerciseDTO)))
            .andExpect(status().isOk());

        // Validate the FinishedExercise in the database
        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeUpdate);
        FinishedExercise testFinishedExercise = finishedExerciseList.get(finishedExerciseList.size() - 1);
        assertThat(testFinishedExercise.getActualBpm()).isEqualTo(UPDATED_ACTUAL_BPM);
        assertThat(testFinishedExercise.getActualMinutes()).isEqualTo(UPDATED_ACTUAL_MINUTES);
        assertThat(testFinishedExercise.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFinishedExercise.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFinishedExercise() throws Exception {
        int databaseSizeBeforeUpdate = finishedExerciseRepository.findAll().size();

        // Create the FinishedExercise
        FinishedExerciseDTO finishedExerciseDTO = finishedExerciseMapper.toDto(finishedExercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinishedExerciseMockMvc.perform(put("/api/finished-exercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinishedExercise in the database
        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFinishedExercise() throws Exception {
        // Initialize the database
        finishedExerciseRepository.saveAndFlush(finishedExercise);

        int databaseSizeBeforeDelete = finishedExerciseRepository.findAll().size();

        // Delete the finishedExercise
        restFinishedExerciseMockMvc.perform(delete("/api/finished-exercises/{id}", finishedExercise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinishedExercise> finishedExerciseList = finishedExerciseRepository.findAll();
        assertThat(finishedExerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedExercise.class);
        FinishedExercise finishedExercise1 = new FinishedExercise();
        finishedExercise1.setId(1L);
        FinishedExercise finishedExercise2 = new FinishedExercise();
        finishedExercise2.setId(finishedExercise1.getId());
        assertThat(finishedExercise1).isEqualTo(finishedExercise2);
        finishedExercise2.setId(2L);
        assertThat(finishedExercise1).isNotEqualTo(finishedExercise2);
        finishedExercise1.setId(null);
        assertThat(finishedExercise1).isNotEqualTo(finishedExercise2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedExerciseDTO.class);
        FinishedExerciseDTO finishedExerciseDTO1 = new FinishedExerciseDTO();
        finishedExerciseDTO1.setId(1L);
        FinishedExerciseDTO finishedExerciseDTO2 = new FinishedExerciseDTO();
        assertThat(finishedExerciseDTO1).isNotEqualTo(finishedExerciseDTO2);
        finishedExerciseDTO2.setId(finishedExerciseDTO1.getId());
        assertThat(finishedExerciseDTO1).isEqualTo(finishedExerciseDTO2);
        finishedExerciseDTO2.setId(2L);
        assertThat(finishedExerciseDTO1).isNotEqualTo(finishedExerciseDTO2);
        finishedExerciseDTO1.setId(null);
        assertThat(finishedExerciseDTO1).isNotEqualTo(finishedExerciseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(finishedExerciseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(finishedExerciseMapper.fromId(null)).isNull();
    }
}
