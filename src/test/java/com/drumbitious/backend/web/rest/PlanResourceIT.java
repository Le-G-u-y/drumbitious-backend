package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.Plan;
import com.drumbitious.backend.domain.User;
import com.drumbitious.backend.domain.ExcerciseConfig;
import com.drumbitious.backend.repository.PlanRepository;
import com.drumbitious.backend.service.PlanService;
import com.drumbitious.backend.service.dto.PlanDTO;
import com.drumbitious.backend.service.mapper.PlanMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.PlanCriteria;
import com.drumbitious.backend.service.PlanQueryService;

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
 * Integration tests for the {@link PlanResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class PlanResourceIT {

    private static final String DEFAULT_PLAN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PLAN_FOCUS = "AAAAAAAAAA";
    private static final String UPDATED_PLAN_FOCUS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_MINUTES_PER_SESSION = 1;
    private static final Integer UPDATED_MINUTES_PER_SESSION = 2;
    private static final Integer SMALLER_MINUTES_PER_SESSION = 1 - 1;

    private static final Integer DEFAULT_SESSIONS_PER_WEEK = 1;
    private static final Integer UPDATED_SESSIONS_PER_WEEK = 2;
    private static final Integer SMALLER_SESSIONS_PER_WEEK = 1 - 1;

    private static final Instant DEFAULT_TARGET_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TARGET_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanQueryService planQueryService;

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

    private MockMvc restPlanMockMvc;

    private Plan plan;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlanResource planResource = new PlanResource(planService, planQueryService);
        this.restPlanMockMvc = MockMvcBuilders.standaloneSetup(planResource)
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
    public static Plan createEntity(EntityManager em) {
        Plan plan = new Plan()
            .planName(DEFAULT_PLAN_NAME)
            .planFocus(DEFAULT_PLAN_FOCUS)
            .description(DEFAULT_DESCRIPTION)
            .minutesPerSession(DEFAULT_MINUTES_PER_SESSION)
            .sessionsPerWeek(DEFAULT_SESSIONS_PER_WEEK)
            .targetDate(DEFAULT_TARGET_DATE)
            .active(DEFAULT_ACTIVE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return plan;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plan createUpdatedEntity(EntityManager em) {
        Plan plan = new Plan()
            .planName(UPDATED_PLAN_NAME)
            .planFocus(UPDATED_PLAN_FOCUS)
            .description(UPDATED_DESCRIPTION)
            .minutesPerSession(UPDATED_MINUTES_PER_SESSION)
            .sessionsPerWeek(UPDATED_SESSIONS_PER_WEEK)
            .targetDate(UPDATED_TARGET_DATE)
            .active(UPDATED_ACTIVE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return plan;
    }

    @BeforeEach
    public void initTest() {
        plan = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlan() throws Exception {
        int databaseSizeBeforeCreate = planRepository.findAll().size();

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);
        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isCreated());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeCreate + 1);
        Plan testPlan = planList.get(planList.size() - 1);
        assertThat(testPlan.getPlanName()).isEqualTo(DEFAULT_PLAN_NAME);
        assertThat(testPlan.getPlanFocus()).isEqualTo(DEFAULT_PLAN_FOCUS);
        assertThat(testPlan.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPlan.getMinutesPerSession()).isEqualTo(DEFAULT_MINUTES_PER_SESSION);
        assertThat(testPlan.getSessionsPerWeek()).isEqualTo(DEFAULT_SESSIONS_PER_WEEK);
        assertThat(testPlan.getTargetDate()).isEqualTo(DEFAULT_TARGET_DATE);
        assertThat(testPlan.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPlan.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testPlan.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createPlanWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = planRepository.findAll().size();

        // Create the Plan with an existing ID
        plan.setId(1L);
        PlanDTO planDTO = planMapper.toDto(plan);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPlanNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setPlanName(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isBadRequest());

        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPlanFocusIsRequired() throws Exception {
        int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setPlanFocus(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isBadRequest());

        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setCreateDate(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isBadRequest());

        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = planRepository.findAll().size();
        // set the field null
        plan.setModifyDate(null);

        // Create the Plan, which fails.
        PlanDTO planDTO = planMapper.toDto(plan);

        restPlanMockMvc.perform(post("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isBadRequest());

        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlans() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList
        restPlanMockMvc.perform(get("/api/plans?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].planFocus").value(hasItem(DEFAULT_PLAN_FOCUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minutesPerSession").value(hasItem(DEFAULT_MINUTES_PER_SESSION)))
            .andExpect(jsonPath("$.[*].sessionsPerWeek").value(hasItem(DEFAULT_SESSIONS_PER_WEEK)))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getPlan() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", plan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(plan.getId().intValue()))
            .andExpect(jsonPath("$.planName").value(DEFAULT_PLAN_NAME))
            .andExpect(jsonPath("$.planFocus").value(DEFAULT_PLAN_FOCUS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.minutesPerSession").value(DEFAULT_MINUTES_PER_SESSION))
            .andExpect(jsonPath("$.sessionsPerWeek").value(DEFAULT_SESSIONS_PER_WEEK))
            .andExpect(jsonPath("$.targetDate").value(DEFAULT_TARGET_DATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllPlansByPlanNameIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planName equals to DEFAULT_PLAN_NAME
        defaultPlanShouldBeFound("planName.equals=" + DEFAULT_PLAN_NAME);

        // Get all the planList where planName equals to UPDATED_PLAN_NAME
        defaultPlanShouldNotBeFound("planName.equals=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planName not equals to DEFAULT_PLAN_NAME
        defaultPlanShouldNotBeFound("planName.notEquals=" + DEFAULT_PLAN_NAME);

        // Get all the planList where planName not equals to UPDATED_PLAN_NAME
        defaultPlanShouldBeFound("planName.notEquals=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanNameIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planName in DEFAULT_PLAN_NAME or UPDATED_PLAN_NAME
        defaultPlanShouldBeFound("planName.in=" + DEFAULT_PLAN_NAME + "," + UPDATED_PLAN_NAME);

        // Get all the planList where planName equals to UPDATED_PLAN_NAME
        defaultPlanShouldNotBeFound("planName.in=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planName is not null
        defaultPlanShouldBeFound("planName.specified=true");

        // Get all the planList where planName is null
        defaultPlanShouldNotBeFound("planName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlansByPlanNameContainsSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planName contains DEFAULT_PLAN_NAME
        defaultPlanShouldBeFound("planName.contains=" + DEFAULT_PLAN_NAME);

        // Get all the planList where planName contains UPDATED_PLAN_NAME
        defaultPlanShouldNotBeFound("planName.contains=" + UPDATED_PLAN_NAME);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanNameNotContainsSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planName does not contain DEFAULT_PLAN_NAME
        defaultPlanShouldNotBeFound("planName.doesNotContain=" + DEFAULT_PLAN_NAME);

        // Get all the planList where planName does not contain UPDATED_PLAN_NAME
        defaultPlanShouldBeFound("planName.doesNotContain=" + UPDATED_PLAN_NAME);
    }


    @Test
    @Transactional
    public void getAllPlansByPlanFocusIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planFocus equals to DEFAULT_PLAN_FOCUS
        defaultPlanShouldBeFound("planFocus.equals=" + DEFAULT_PLAN_FOCUS);

        // Get all the planList where planFocus equals to UPDATED_PLAN_FOCUS
        defaultPlanShouldNotBeFound("planFocus.equals=" + UPDATED_PLAN_FOCUS);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanFocusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planFocus not equals to DEFAULT_PLAN_FOCUS
        defaultPlanShouldNotBeFound("planFocus.notEquals=" + DEFAULT_PLAN_FOCUS);

        // Get all the planList where planFocus not equals to UPDATED_PLAN_FOCUS
        defaultPlanShouldBeFound("planFocus.notEquals=" + UPDATED_PLAN_FOCUS);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanFocusIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planFocus in DEFAULT_PLAN_FOCUS or UPDATED_PLAN_FOCUS
        defaultPlanShouldBeFound("planFocus.in=" + DEFAULT_PLAN_FOCUS + "," + UPDATED_PLAN_FOCUS);

        // Get all the planList where planFocus equals to UPDATED_PLAN_FOCUS
        defaultPlanShouldNotBeFound("planFocus.in=" + UPDATED_PLAN_FOCUS);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanFocusIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planFocus is not null
        defaultPlanShouldBeFound("planFocus.specified=true");

        // Get all the planList where planFocus is null
        defaultPlanShouldNotBeFound("planFocus.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlansByPlanFocusContainsSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planFocus contains DEFAULT_PLAN_FOCUS
        defaultPlanShouldBeFound("planFocus.contains=" + DEFAULT_PLAN_FOCUS);

        // Get all the planList where planFocus contains UPDATED_PLAN_FOCUS
        defaultPlanShouldNotBeFound("planFocus.contains=" + UPDATED_PLAN_FOCUS);
    }

    @Test
    @Transactional
    public void getAllPlansByPlanFocusNotContainsSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where planFocus does not contain DEFAULT_PLAN_FOCUS
        defaultPlanShouldNotBeFound("planFocus.doesNotContain=" + DEFAULT_PLAN_FOCUS);

        // Get all the planList where planFocus does not contain UPDATED_PLAN_FOCUS
        defaultPlanShouldBeFound("planFocus.doesNotContain=" + UPDATED_PLAN_FOCUS);
    }


    @Test
    @Transactional
    public void getAllPlansByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where description equals to DEFAULT_DESCRIPTION
        defaultPlanShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the planList where description equals to UPDATED_DESCRIPTION
        defaultPlanShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlansByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where description not equals to DEFAULT_DESCRIPTION
        defaultPlanShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the planList where description not equals to UPDATED_DESCRIPTION
        defaultPlanShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlansByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPlanShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the planList where description equals to UPDATED_DESCRIPTION
        defaultPlanShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlansByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where description is not null
        defaultPlanShouldBeFound("description.specified=true");

        // Get all the planList where description is null
        defaultPlanShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlansByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where description contains DEFAULT_DESCRIPTION
        defaultPlanShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the planList where description contains UPDATED_DESCRIPTION
        defaultPlanShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPlansByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where description does not contain DEFAULT_DESCRIPTION
        defaultPlanShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the planList where description does not contain UPDATED_DESCRIPTION
        defaultPlanShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession equals to DEFAULT_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.equals=" + DEFAULT_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession equals to UPDATED_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.equals=" + UPDATED_MINUTES_PER_SESSION);
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession not equals to DEFAULT_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.notEquals=" + DEFAULT_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession not equals to UPDATED_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.notEquals=" + UPDATED_MINUTES_PER_SESSION);
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession in DEFAULT_MINUTES_PER_SESSION or UPDATED_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.in=" + DEFAULT_MINUTES_PER_SESSION + "," + UPDATED_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession equals to UPDATED_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.in=" + UPDATED_MINUTES_PER_SESSION);
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession is not null
        defaultPlanShouldBeFound("minutesPerSession.specified=true");

        // Get all the planList where minutesPerSession is null
        defaultPlanShouldNotBeFound("minutesPerSession.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession is greater than or equal to DEFAULT_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.greaterThanOrEqual=" + DEFAULT_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession is greater than or equal to UPDATED_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.greaterThanOrEqual=" + UPDATED_MINUTES_PER_SESSION);
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession is less than or equal to DEFAULT_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.lessThanOrEqual=" + DEFAULT_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession is less than or equal to SMALLER_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.lessThanOrEqual=" + SMALLER_MINUTES_PER_SESSION);
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsLessThanSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession is less than DEFAULT_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.lessThan=" + DEFAULT_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession is less than UPDATED_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.lessThan=" + UPDATED_MINUTES_PER_SESSION);
    }

    @Test
    @Transactional
    public void getAllPlansByMinutesPerSessionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where minutesPerSession is greater than DEFAULT_MINUTES_PER_SESSION
        defaultPlanShouldNotBeFound("minutesPerSession.greaterThan=" + DEFAULT_MINUTES_PER_SESSION);

        // Get all the planList where minutesPerSession is greater than SMALLER_MINUTES_PER_SESSION
        defaultPlanShouldBeFound("minutesPerSession.greaterThan=" + SMALLER_MINUTES_PER_SESSION);
    }


    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek equals to DEFAULT_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.equals=" + DEFAULT_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek equals to UPDATED_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.equals=" + UPDATED_SESSIONS_PER_WEEK);
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek not equals to DEFAULT_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.notEquals=" + DEFAULT_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek not equals to UPDATED_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.notEquals=" + UPDATED_SESSIONS_PER_WEEK);
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek in DEFAULT_SESSIONS_PER_WEEK or UPDATED_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.in=" + DEFAULT_SESSIONS_PER_WEEK + "," + UPDATED_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek equals to UPDATED_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.in=" + UPDATED_SESSIONS_PER_WEEK);
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek is not null
        defaultPlanShouldBeFound("sessionsPerWeek.specified=true");

        // Get all the planList where sessionsPerWeek is null
        defaultPlanShouldNotBeFound("sessionsPerWeek.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek is greater than or equal to DEFAULT_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.greaterThanOrEqual=" + DEFAULT_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek is greater than or equal to UPDATED_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.greaterThanOrEqual=" + UPDATED_SESSIONS_PER_WEEK);
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek is less than or equal to DEFAULT_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.lessThanOrEqual=" + DEFAULT_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek is less than or equal to SMALLER_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.lessThanOrEqual=" + SMALLER_SESSIONS_PER_WEEK);
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsLessThanSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek is less than DEFAULT_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.lessThan=" + DEFAULT_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek is less than UPDATED_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.lessThan=" + UPDATED_SESSIONS_PER_WEEK);
    }

    @Test
    @Transactional
    public void getAllPlansBySessionsPerWeekIsGreaterThanSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where sessionsPerWeek is greater than DEFAULT_SESSIONS_PER_WEEK
        defaultPlanShouldNotBeFound("sessionsPerWeek.greaterThan=" + DEFAULT_SESSIONS_PER_WEEK);

        // Get all the planList where sessionsPerWeek is greater than SMALLER_SESSIONS_PER_WEEK
        defaultPlanShouldBeFound("sessionsPerWeek.greaterThan=" + SMALLER_SESSIONS_PER_WEEK);
    }


    @Test
    @Transactional
    public void getAllPlansByTargetDateIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where targetDate equals to DEFAULT_TARGET_DATE
        defaultPlanShouldBeFound("targetDate.equals=" + DEFAULT_TARGET_DATE);

        // Get all the planList where targetDate equals to UPDATED_TARGET_DATE
        defaultPlanShouldNotBeFound("targetDate.equals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByTargetDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where targetDate not equals to DEFAULT_TARGET_DATE
        defaultPlanShouldNotBeFound("targetDate.notEquals=" + DEFAULT_TARGET_DATE);

        // Get all the planList where targetDate not equals to UPDATED_TARGET_DATE
        defaultPlanShouldBeFound("targetDate.notEquals=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByTargetDateIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where targetDate in DEFAULT_TARGET_DATE or UPDATED_TARGET_DATE
        defaultPlanShouldBeFound("targetDate.in=" + DEFAULT_TARGET_DATE + "," + UPDATED_TARGET_DATE);

        // Get all the planList where targetDate equals to UPDATED_TARGET_DATE
        defaultPlanShouldNotBeFound("targetDate.in=" + UPDATED_TARGET_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByTargetDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where targetDate is not null
        defaultPlanShouldBeFound("targetDate.specified=true");

        // Get all the planList where targetDate is null
        defaultPlanShouldNotBeFound("targetDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlansByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where active equals to DEFAULT_ACTIVE
        defaultPlanShouldBeFound("active.equals=" + DEFAULT_ACTIVE);

        // Get all the planList where active equals to UPDATED_ACTIVE
        defaultPlanShouldNotBeFound("active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlansByActiveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where active not equals to DEFAULT_ACTIVE
        defaultPlanShouldNotBeFound("active.notEquals=" + DEFAULT_ACTIVE);

        // Get all the planList where active not equals to UPDATED_ACTIVE
        defaultPlanShouldBeFound("active.notEquals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlansByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where active in DEFAULT_ACTIVE or UPDATED_ACTIVE
        defaultPlanShouldBeFound("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE);

        // Get all the planList where active equals to UPDATED_ACTIVE
        defaultPlanShouldNotBeFound("active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllPlansByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where active is not null
        defaultPlanShouldBeFound("active.specified=true");

        // Get all the planList where active is null
        defaultPlanShouldNotBeFound("active.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlansByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where createDate equals to DEFAULT_CREATE_DATE
        defaultPlanShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the planList where createDate equals to UPDATED_CREATE_DATE
        defaultPlanShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where createDate not equals to DEFAULT_CREATE_DATE
        defaultPlanShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the planList where createDate not equals to UPDATED_CREATE_DATE
        defaultPlanShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultPlanShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the planList where createDate equals to UPDATED_CREATE_DATE
        defaultPlanShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where createDate is not null
        defaultPlanShouldBeFound("createDate.specified=true");

        // Get all the planList where createDate is null
        defaultPlanShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlansByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultPlanShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the planList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultPlanShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultPlanShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the planList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultPlanShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultPlanShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the planList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultPlanShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllPlansByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        // Get all the planList where modifyDate is not null
        defaultPlanShouldBeFound("modifyDate.specified=true");

        // Get all the planList where modifyDate is null
        defaultPlanShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlansByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);
        User owner = UserResourceIT.createEntity(em);
        em.persist(owner);
        em.flush();
        plan.setOwner(owner);
        planRepository.saveAndFlush(plan);
        Long ownerId = owner.getId();

        // Get all the planList where owner equals to ownerId
        defaultPlanShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the planList where owner equals to ownerId + 1
        defaultPlanShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }


    @Test
    @Transactional
    public void getAllPlansByCreatorIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);
        User creator = UserResourceIT.createEntity(em);
        em.persist(creator);
        em.flush();
        plan.setCreator(creator);
        planRepository.saveAndFlush(plan);
        Long creatorId = creator.getId();

        // Get all the planList where creator equals to creatorId
        defaultPlanShouldBeFound("creatorId.equals=" + creatorId);

        // Get all the planList where creator equals to creatorId + 1
        defaultPlanShouldNotBeFound("creatorId.equals=" + (creatorId + 1));
    }


    @Test
    @Transactional
    public void getAllPlansByExcerciseConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);
        ExcerciseConfig excerciseConfig = ExcerciseConfigResourceIT.createEntity(em);
        em.persist(excerciseConfig);
        em.flush();
        plan.setExcerciseConfig(excerciseConfig);
        planRepository.saveAndFlush(plan);
        Long excerciseConfigId = excerciseConfig.getId();

        // Get all the planList where excerciseConfig equals to excerciseConfigId
        defaultPlanShouldBeFound("excerciseConfigId.equals=" + excerciseConfigId);

        // Get all the planList where excerciseConfig equals to excerciseConfigId + 1
        defaultPlanShouldNotBeFound("excerciseConfigId.equals=" + (excerciseConfigId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlanShouldBeFound(String filter) throws Exception {
        restPlanMockMvc.perform(get("/api/plans?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plan.getId().intValue())))
            .andExpect(jsonPath("$.[*].planName").value(hasItem(DEFAULT_PLAN_NAME)))
            .andExpect(jsonPath("$.[*].planFocus").value(hasItem(DEFAULT_PLAN_FOCUS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].minutesPerSession").value(hasItem(DEFAULT_MINUTES_PER_SESSION)))
            .andExpect(jsonPath("$.[*].sessionsPerWeek").value(hasItem(DEFAULT_SESSIONS_PER_WEEK)))
            .andExpect(jsonPath("$.[*].targetDate").value(hasItem(DEFAULT_TARGET_DATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restPlanMockMvc.perform(get("/api/plans/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlanShouldNotBeFound(String filter) throws Exception {
        restPlanMockMvc.perform(get("/api/plans?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlanMockMvc.perform(get("/api/plans/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlan() throws Exception {
        // Get the plan
        restPlanMockMvc.perform(get("/api/plans/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlan() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        int databaseSizeBeforeUpdate = planRepository.findAll().size();

        // Update the plan
        Plan updatedPlan = planRepository.findById(plan.getId()).get();
        // Disconnect from session so that the updates on updatedPlan are not directly saved in db
        em.detach(updatedPlan);
        updatedPlan
            .planName(UPDATED_PLAN_NAME)
            .planFocus(UPDATED_PLAN_FOCUS)
            .description(UPDATED_DESCRIPTION)
            .minutesPerSession(UPDATED_MINUTES_PER_SESSION)
            .sessionsPerWeek(UPDATED_SESSIONS_PER_WEEK)
            .targetDate(UPDATED_TARGET_DATE)
            .active(UPDATED_ACTIVE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        PlanDTO planDTO = planMapper.toDto(updatedPlan);

        restPlanMockMvc.perform(put("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isOk());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeUpdate);
        Plan testPlan = planList.get(planList.size() - 1);
        assertThat(testPlan.getPlanName()).isEqualTo(UPDATED_PLAN_NAME);
        assertThat(testPlan.getPlanFocus()).isEqualTo(UPDATED_PLAN_FOCUS);
        assertThat(testPlan.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPlan.getMinutesPerSession()).isEqualTo(UPDATED_MINUTES_PER_SESSION);
        assertThat(testPlan.getSessionsPerWeek()).isEqualTo(UPDATED_SESSIONS_PER_WEEK);
        assertThat(testPlan.getTargetDate()).isEqualTo(UPDATED_TARGET_DATE);
        assertThat(testPlan.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPlan.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testPlan.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingPlan() throws Exception {
        int databaseSizeBeforeUpdate = planRepository.findAll().size();

        // Create the Plan
        PlanDTO planDTO = planMapper.toDto(plan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanMockMvc.perform(put("/api/plans")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(planDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plan in the database
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlan() throws Exception {
        // Initialize the database
        planRepository.saveAndFlush(plan);

        int databaseSizeBeforeDelete = planRepository.findAll().size();

        // Delete the plan
        restPlanMockMvc.perform(delete("/api/plans/{id}", plan.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plan> planList = planRepository.findAll();
        assertThat(planList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Plan.class);
        Plan plan1 = new Plan();
        plan1.setId(1L);
        Plan plan2 = new Plan();
        plan2.setId(plan1.getId());
        assertThat(plan1).isEqualTo(plan2);
        plan2.setId(2L);
        assertThat(plan1).isNotEqualTo(plan2);
        plan1.setId(null);
        assertThat(plan1).isNotEqualTo(plan2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlanDTO.class);
        PlanDTO planDTO1 = new PlanDTO();
        planDTO1.setId(1L);
        PlanDTO planDTO2 = new PlanDTO();
        assertThat(planDTO1).isNotEqualTo(planDTO2);
        planDTO2.setId(planDTO1.getId());
        assertThat(planDTO1).isEqualTo(planDTO2);
        planDTO2.setId(2L);
        assertThat(planDTO1).isNotEqualTo(planDTO2);
        planDTO1.setId(null);
        assertThat(planDTO1).isNotEqualTo(planDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(planMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(planMapper.fromId(null)).isNull();
    }
}
