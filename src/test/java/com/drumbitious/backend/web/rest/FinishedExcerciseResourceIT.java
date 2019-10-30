package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.FinishedExcercise;
import com.drumbitious.backend.domain.FinishedSession;
import com.drumbitious.backend.domain.Excercise;
import com.drumbitious.backend.repository.FinishedExcerciseRepository;
import com.drumbitious.backend.service.FinishedExcerciseService;
import com.drumbitious.backend.service.dto.FinishedExcerciseDTO;
import com.drumbitious.backend.service.mapper.FinishedExcerciseMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.FinishedExcerciseCriteria;
import com.drumbitious.backend.service.FinishedExcerciseQueryService;

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
 * Integration tests for the {@link FinishedExcerciseResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class FinishedExcerciseResourceIT {

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
    private FinishedExcerciseRepository finishedExcerciseRepository;

    @Autowired
    private FinishedExcerciseMapper finishedExcerciseMapper;

    @Autowired
    private FinishedExcerciseService finishedExcerciseService;

    @Autowired
    private FinishedExcerciseQueryService finishedExcerciseQueryService;

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

    private MockMvc restFinishedExcerciseMockMvc;

    private FinishedExcercise finishedExcercise;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FinishedExcerciseResource finishedExcerciseResource = new FinishedExcerciseResource(finishedExcerciseService, finishedExcerciseQueryService);
        this.restFinishedExcerciseMockMvc = MockMvcBuilders.standaloneSetup(finishedExcerciseResource)
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
    public static FinishedExcercise createEntity(EntityManager em) {
        FinishedExcercise finishedExcercise = new FinishedExcercise()
            .actualBpm(DEFAULT_ACTUAL_BPM)
            .actualMinutes(DEFAULT_ACTUAL_MINUTES)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return finishedExcercise;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinishedExcercise createUpdatedEntity(EntityManager em) {
        FinishedExcercise finishedExcercise = new FinishedExcercise()
            .actualBpm(UPDATED_ACTUAL_BPM)
            .actualMinutes(UPDATED_ACTUAL_MINUTES)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return finishedExcercise;
    }

    @BeforeEach
    public void initTest() {
        finishedExcercise = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinishedExcercise() throws Exception {
        int databaseSizeBeforeCreate = finishedExcerciseRepository.findAll().size();

        // Create the FinishedExcercise
        FinishedExcerciseDTO finishedExcerciseDTO = finishedExcerciseMapper.toDto(finishedExcercise);
        restFinishedExcerciseMockMvc.perform(post("/api/finished-excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExcerciseDTO)))
            .andExpect(status().isCreated());

        // Validate the FinishedExcercise in the database
        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeCreate + 1);
        FinishedExcercise testFinishedExcercise = finishedExcerciseList.get(finishedExcerciseList.size() - 1);
        assertThat(testFinishedExcercise.getActualBpm()).isEqualTo(DEFAULT_ACTUAL_BPM);
        assertThat(testFinishedExcercise.getActualMinutes()).isEqualTo(DEFAULT_ACTUAL_MINUTES);
        assertThat(testFinishedExcercise.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFinishedExcercise.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createFinishedExcerciseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = finishedExcerciseRepository.findAll().size();

        // Create the FinishedExcercise with an existing ID
        finishedExcercise.setId(1L);
        FinishedExcerciseDTO finishedExcerciseDTO = finishedExcerciseMapper.toDto(finishedExcercise);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinishedExcerciseMockMvc.perform(post("/api/finished-excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExcerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinishedExcercise in the database
        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = finishedExcerciseRepository.findAll().size();
        // set the field null
        finishedExcercise.setCreateDate(null);

        // Create the FinishedExcercise, which fails.
        FinishedExcerciseDTO finishedExcerciseDTO = finishedExcerciseMapper.toDto(finishedExcercise);

        restFinishedExcerciseMockMvc.perform(post("/api/finished-excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExcerciseDTO)))
            .andExpect(status().isBadRequest());

        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = finishedExcerciseRepository.findAll().size();
        // set the field null
        finishedExcercise.setModifyDate(null);

        // Create the FinishedExcercise, which fails.
        FinishedExcerciseDTO finishedExcerciseDTO = finishedExcerciseMapper.toDto(finishedExcercise);

        restFinishedExcerciseMockMvc.perform(post("/api/finished-excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExcerciseDTO)))
            .andExpect(status().isBadRequest());

        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercises() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedExcercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualBpm").value(hasItem(DEFAULT_ACTUAL_BPM)))
            .andExpect(jsonPath("$.[*].actualMinutes").value(hasItem(DEFAULT_ACTUAL_MINUTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getFinishedExcercise() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get the finishedExcercise
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises/{id}", finishedExcercise.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(finishedExcercise.getId().intValue()))
            .andExpect(jsonPath("$.actualBpm").value(DEFAULT_ACTUAL_BPM))
            .andExpect(jsonPath("$.actualMinutes").value(DEFAULT_ACTUAL_MINUTES))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm equals to DEFAULT_ACTUAL_BPM
        defaultFinishedExcerciseShouldBeFound("actualBpm.equals=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm equals to UPDATED_ACTUAL_BPM
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.equals=" + UPDATED_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm not equals to DEFAULT_ACTUAL_BPM
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.notEquals=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm not equals to UPDATED_ACTUAL_BPM
        defaultFinishedExcerciseShouldBeFound("actualBpm.notEquals=" + UPDATED_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm in DEFAULT_ACTUAL_BPM or UPDATED_ACTUAL_BPM
        defaultFinishedExcerciseShouldBeFound("actualBpm.in=" + DEFAULT_ACTUAL_BPM + "," + UPDATED_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm equals to UPDATED_ACTUAL_BPM
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.in=" + UPDATED_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm is not null
        defaultFinishedExcerciseShouldBeFound("actualBpm.specified=true");

        // Get all the finishedExcerciseList where actualBpm is null
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm is greater than or equal to DEFAULT_ACTUAL_BPM
        defaultFinishedExcerciseShouldBeFound("actualBpm.greaterThanOrEqual=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm is greater than or equal to (DEFAULT_ACTUAL_BPM + 1)
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.greaterThanOrEqual=" + (DEFAULT_ACTUAL_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm is less than or equal to DEFAULT_ACTUAL_BPM
        defaultFinishedExcerciseShouldBeFound("actualBpm.lessThanOrEqual=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm is less than or equal to SMALLER_ACTUAL_BPM
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.lessThanOrEqual=" + SMALLER_ACTUAL_BPM);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsLessThanSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm is less than DEFAULT_ACTUAL_BPM
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.lessThan=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm is less than (DEFAULT_ACTUAL_BPM + 1)
        defaultFinishedExcerciseShouldBeFound("actualBpm.lessThan=" + (DEFAULT_ACTUAL_BPM + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualBpmIsGreaterThanSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualBpm is greater than DEFAULT_ACTUAL_BPM
        defaultFinishedExcerciseShouldNotBeFound("actualBpm.greaterThan=" + DEFAULT_ACTUAL_BPM);

        // Get all the finishedExcerciseList where actualBpm is greater than SMALLER_ACTUAL_BPM
        defaultFinishedExcerciseShouldBeFound("actualBpm.greaterThan=" + SMALLER_ACTUAL_BPM);
    }


    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes equals to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldBeFound("actualMinutes.equals=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes equals to UPDATED_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.equals=" + UPDATED_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes not equals to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.notEquals=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes not equals to UPDATED_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldBeFound("actualMinutes.notEquals=" + UPDATED_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes in DEFAULT_ACTUAL_MINUTES or UPDATED_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldBeFound("actualMinutes.in=" + DEFAULT_ACTUAL_MINUTES + "," + UPDATED_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes equals to UPDATED_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.in=" + UPDATED_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes is not null
        defaultFinishedExcerciseShouldBeFound("actualMinutes.specified=true");

        // Get all the finishedExcerciseList where actualMinutes is null
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes is greater than or equal to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldBeFound("actualMinutes.greaterThanOrEqual=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes is greater than or equal to (DEFAULT_ACTUAL_MINUTES + 1)
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.greaterThanOrEqual=" + (DEFAULT_ACTUAL_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes is less than or equal to DEFAULT_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldBeFound("actualMinutes.lessThanOrEqual=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes is less than or equal to SMALLER_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.lessThanOrEqual=" + SMALLER_ACTUAL_MINUTES);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsLessThanSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes is less than DEFAULT_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.lessThan=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes is less than (DEFAULT_ACTUAL_MINUTES + 1)
        defaultFinishedExcerciseShouldBeFound("actualMinutes.lessThan=" + (DEFAULT_ACTUAL_MINUTES + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByActualMinutesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where actualMinutes is greater than DEFAULT_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldNotBeFound("actualMinutes.greaterThan=" + DEFAULT_ACTUAL_MINUTES);

        // Get all the finishedExcerciseList where actualMinutes is greater than SMALLER_ACTUAL_MINUTES
        defaultFinishedExcerciseShouldBeFound("actualMinutes.greaterThan=" + SMALLER_ACTUAL_MINUTES);
    }


    @Test
    @Transactional
    public void getAllFinishedExcercisesByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where createDate equals to DEFAULT_CREATE_DATE
        defaultFinishedExcerciseShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the finishedExcerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultFinishedExcerciseShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where createDate not equals to DEFAULT_CREATE_DATE
        defaultFinishedExcerciseShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the finishedExcerciseList where createDate not equals to UPDATED_CREATE_DATE
        defaultFinishedExcerciseShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultFinishedExcerciseShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the finishedExcerciseList where createDate equals to UPDATED_CREATE_DATE
        defaultFinishedExcerciseShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where createDate is not null
        defaultFinishedExcerciseShouldBeFound("createDate.specified=true");

        // Get all the finishedExcerciseList where createDate is null
        defaultFinishedExcerciseShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultFinishedExcerciseShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the finishedExcerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultFinishedExcerciseShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultFinishedExcerciseShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the finishedExcerciseList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultFinishedExcerciseShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultFinishedExcerciseShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the finishedExcerciseList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultFinishedExcerciseShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        // Get all the finishedExcerciseList where modifyDate is not null
        defaultFinishedExcerciseShouldBeFound("modifyDate.specified=true");

        // Get all the finishedExcerciseList where modifyDate is null
        defaultFinishedExcerciseShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedExcercisesByFinishedSessionIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);
        FinishedSession finishedSession = FinishedSessionResourceIT.createEntity(em);
        em.persist(finishedSession);
        em.flush();
        finishedExcercise.setFinishedSession(finishedSession);
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);
        Long finishedSessionId = finishedSession.getId();

        // Get all the finishedExcerciseList where finishedSession equals to finishedSessionId
        defaultFinishedExcerciseShouldBeFound("finishedSessionId.equals=" + finishedSessionId);

        // Get all the finishedExcerciseList where finishedSession equals to finishedSessionId + 1
        defaultFinishedExcerciseShouldNotBeFound("finishedSessionId.equals=" + (finishedSessionId + 1));
    }


    @Test
    @Transactional
    public void getAllFinishedExcercisesByExcerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);
        Excercise excercise = ExcerciseResourceIT.createEntity(em);
        em.persist(excercise);
        em.flush();
        finishedExcercise.setExcercise(excercise);
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);
        Long excerciseId = excercise.getId();

        // Get all the finishedExcerciseList where excercise equals to excerciseId
        defaultFinishedExcerciseShouldBeFound("excerciseId.equals=" + excerciseId);

        // Get all the finishedExcerciseList where excercise equals to excerciseId + 1
        defaultFinishedExcerciseShouldNotBeFound("excerciseId.equals=" + (excerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinishedExcerciseShouldBeFound(String filter) throws Exception {
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedExcercise.getId().intValue())))
            .andExpect(jsonPath("$.[*].actualBpm").value(hasItem(DEFAULT_ACTUAL_BPM)))
            .andExpect(jsonPath("$.[*].actualMinutes").value(hasItem(DEFAULT_ACTUAL_MINUTES)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinishedExcerciseShouldNotBeFound(String filter) throws Exception {
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFinishedExcercise() throws Exception {
        // Get the finishedExcercise
        restFinishedExcerciseMockMvc.perform(get("/api/finished-excercises/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinishedExcercise() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        int databaseSizeBeforeUpdate = finishedExcerciseRepository.findAll().size();

        // Update the finishedExcercise
        FinishedExcercise updatedFinishedExcercise = finishedExcerciseRepository.findById(finishedExcercise.getId()).get();
        // Disconnect from session so that the updates on updatedFinishedExcercise are not directly saved in db
        em.detach(updatedFinishedExcercise);
        updatedFinishedExcercise
            .actualBpm(UPDATED_ACTUAL_BPM)
            .actualMinutes(UPDATED_ACTUAL_MINUTES)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        FinishedExcerciseDTO finishedExcerciseDTO = finishedExcerciseMapper.toDto(updatedFinishedExcercise);

        restFinishedExcerciseMockMvc.perform(put("/api/finished-excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExcerciseDTO)))
            .andExpect(status().isOk());

        // Validate the FinishedExcercise in the database
        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeUpdate);
        FinishedExcercise testFinishedExcercise = finishedExcerciseList.get(finishedExcerciseList.size() - 1);
        assertThat(testFinishedExcercise.getActualBpm()).isEqualTo(UPDATED_ACTUAL_BPM);
        assertThat(testFinishedExcercise.getActualMinutes()).isEqualTo(UPDATED_ACTUAL_MINUTES);
        assertThat(testFinishedExcercise.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFinishedExcercise.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFinishedExcercise() throws Exception {
        int databaseSizeBeforeUpdate = finishedExcerciseRepository.findAll().size();

        // Create the FinishedExcercise
        FinishedExcerciseDTO finishedExcerciseDTO = finishedExcerciseMapper.toDto(finishedExcercise);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinishedExcerciseMockMvc.perform(put("/api/finished-excercises")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedExcerciseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinishedExcercise in the database
        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFinishedExcercise() throws Exception {
        // Initialize the database
        finishedExcerciseRepository.saveAndFlush(finishedExcercise);

        int databaseSizeBeforeDelete = finishedExcerciseRepository.findAll().size();

        // Delete the finishedExcercise
        restFinishedExcerciseMockMvc.perform(delete("/api/finished-excercises/{id}", finishedExcercise.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinishedExcercise> finishedExcerciseList = finishedExcerciseRepository.findAll();
        assertThat(finishedExcerciseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedExcercise.class);
        FinishedExcercise finishedExcercise1 = new FinishedExcercise();
        finishedExcercise1.setId(1L);
        FinishedExcercise finishedExcercise2 = new FinishedExcercise();
        finishedExcercise2.setId(finishedExcercise1.getId());
        assertThat(finishedExcercise1).isEqualTo(finishedExcercise2);
        finishedExcercise2.setId(2L);
        assertThat(finishedExcercise1).isNotEqualTo(finishedExcercise2);
        finishedExcercise1.setId(null);
        assertThat(finishedExcercise1).isNotEqualTo(finishedExcercise2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedExcerciseDTO.class);
        FinishedExcerciseDTO finishedExcerciseDTO1 = new FinishedExcerciseDTO();
        finishedExcerciseDTO1.setId(1L);
        FinishedExcerciseDTO finishedExcerciseDTO2 = new FinishedExcerciseDTO();
        assertThat(finishedExcerciseDTO1).isNotEqualTo(finishedExcerciseDTO2);
        finishedExcerciseDTO2.setId(finishedExcerciseDTO1.getId());
        assertThat(finishedExcerciseDTO1).isEqualTo(finishedExcerciseDTO2);
        finishedExcerciseDTO2.setId(2L);
        assertThat(finishedExcerciseDTO1).isNotEqualTo(finishedExcerciseDTO2);
        finishedExcerciseDTO1.setId(null);
        assertThat(finishedExcerciseDTO1).isNotEqualTo(finishedExcerciseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(finishedExcerciseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(finishedExcerciseMapper.fromId(null)).isNull();
    }
}
