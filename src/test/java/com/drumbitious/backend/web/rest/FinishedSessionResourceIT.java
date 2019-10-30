package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.FinishedSession;
import com.drumbitious.backend.domain.Plan;
import com.drumbitious.backend.domain.FinishedExcercise;
import com.drumbitious.backend.repository.FinishedSessionRepository;
import com.drumbitious.backend.service.FinishedSessionService;
import com.drumbitious.backend.service.dto.FinishedSessionDTO;
import com.drumbitious.backend.service.mapper.FinishedSessionMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.FinishedSessionCriteria;
import com.drumbitious.backend.service.FinishedSessionQueryService;

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
 * Integration tests for the {@link FinishedSessionResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class FinishedSessionResourceIT {

    private static final Integer DEFAULT_MINUTES_TOTAL = 1;
    private static final Integer UPDATED_MINUTES_TOTAL = 2;
    private static final Integer SMALLER_MINUTES_TOTAL = 1 - 1;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FinishedSessionRepository finishedSessionRepository;

    @Autowired
    private FinishedSessionMapper finishedSessionMapper;

    @Autowired
    private FinishedSessionService finishedSessionService;

    @Autowired
    private FinishedSessionQueryService finishedSessionQueryService;

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

    private MockMvc restFinishedSessionMockMvc;

    private FinishedSession finishedSession;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FinishedSessionResource finishedSessionResource = new FinishedSessionResource(finishedSessionService, finishedSessionQueryService);
        this.restFinishedSessionMockMvc = MockMvcBuilders.standaloneSetup(finishedSessionResource)
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
    public static FinishedSession createEntity(EntityManager em) {
        FinishedSession finishedSession = new FinishedSession()
            .minutesTotal(DEFAULT_MINUTES_TOTAL)
            .note(DEFAULT_NOTE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return finishedSession;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinishedSession createUpdatedEntity(EntityManager em) {
        FinishedSession finishedSession = new FinishedSession()
            .minutesTotal(UPDATED_MINUTES_TOTAL)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return finishedSession;
    }

    @BeforeEach
    public void initTest() {
        finishedSession = createEntity(em);
    }

    @Test
    @Transactional
    public void createFinishedSession() throws Exception {
        int databaseSizeBeforeCreate = finishedSessionRepository.findAll().size();

        // Create the FinishedSession
        FinishedSessionDTO finishedSessionDTO = finishedSessionMapper.toDto(finishedSession);
        restFinishedSessionMockMvc.perform(post("/api/finished-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedSessionDTO)))
            .andExpect(status().isCreated());

        // Validate the FinishedSession in the database
        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeCreate + 1);
        FinishedSession testFinishedSession = finishedSessionList.get(finishedSessionList.size() - 1);
        assertThat(testFinishedSession.getMinutesTotal()).isEqualTo(DEFAULT_MINUTES_TOTAL);
        assertThat(testFinishedSession.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testFinishedSession.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFinishedSession.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createFinishedSessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = finishedSessionRepository.findAll().size();

        // Create the FinishedSession with an existing ID
        finishedSession.setId(1L);
        FinishedSessionDTO finishedSessionDTO = finishedSessionMapper.toDto(finishedSession);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinishedSessionMockMvc.perform(post("/api/finished-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinishedSession in the database
        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = finishedSessionRepository.findAll().size();
        // set the field null
        finishedSession.setCreateDate(null);

        // Create the FinishedSession, which fails.
        FinishedSessionDTO finishedSessionDTO = finishedSessionMapper.toDto(finishedSession);

        restFinishedSessionMockMvc.perform(post("/api/finished-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedSessionDTO)))
            .andExpect(status().isBadRequest());

        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = finishedSessionRepository.findAll().size();
        // set the field null
        finishedSession.setModifyDate(null);

        // Create the FinishedSession, which fails.
        FinishedSessionDTO finishedSessionDTO = finishedSessionMapper.toDto(finishedSession);

        restFinishedSessionMockMvc.perform(post("/api/finished-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedSessionDTO)))
            .andExpect(status().isBadRequest());

        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFinishedSessions() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].minutesTotal").value(hasItem(DEFAULT_MINUTES_TOTAL)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getFinishedSession() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get the finishedSession
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions/{id}", finishedSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(finishedSession.getId().intValue()))
            .andExpect(jsonPath("$.minutesTotal").value(DEFAULT_MINUTES_TOTAL))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal equals to DEFAULT_MINUTES_TOTAL
        defaultFinishedSessionShouldBeFound("minutesTotal.equals=" + DEFAULT_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal equals to UPDATED_MINUTES_TOTAL
        defaultFinishedSessionShouldNotBeFound("minutesTotal.equals=" + UPDATED_MINUTES_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal not equals to DEFAULT_MINUTES_TOTAL
        defaultFinishedSessionShouldNotBeFound("minutesTotal.notEquals=" + DEFAULT_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal not equals to UPDATED_MINUTES_TOTAL
        defaultFinishedSessionShouldBeFound("minutesTotal.notEquals=" + UPDATED_MINUTES_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsInShouldWork() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal in DEFAULT_MINUTES_TOTAL or UPDATED_MINUTES_TOTAL
        defaultFinishedSessionShouldBeFound("minutesTotal.in=" + DEFAULT_MINUTES_TOTAL + "," + UPDATED_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal equals to UPDATED_MINUTES_TOTAL
        defaultFinishedSessionShouldNotBeFound("minutesTotal.in=" + UPDATED_MINUTES_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal is not null
        defaultFinishedSessionShouldBeFound("minutesTotal.specified=true");

        // Get all the finishedSessionList where minutesTotal is null
        defaultFinishedSessionShouldNotBeFound("minutesTotal.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal is greater than or equal to DEFAULT_MINUTES_TOTAL
        defaultFinishedSessionShouldBeFound("minutesTotal.greaterThanOrEqual=" + DEFAULT_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal is greater than or equal to (DEFAULT_MINUTES_TOTAL + 1)
        defaultFinishedSessionShouldNotBeFound("minutesTotal.greaterThanOrEqual=" + (DEFAULT_MINUTES_TOTAL + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal is less than or equal to DEFAULT_MINUTES_TOTAL
        defaultFinishedSessionShouldBeFound("minutesTotal.lessThanOrEqual=" + DEFAULT_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal is less than or equal to SMALLER_MINUTES_TOTAL
        defaultFinishedSessionShouldNotBeFound("minutesTotal.lessThanOrEqual=" + SMALLER_MINUTES_TOTAL);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal is less than DEFAULT_MINUTES_TOTAL
        defaultFinishedSessionShouldNotBeFound("minutesTotal.lessThan=" + DEFAULT_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal is less than (DEFAULT_MINUTES_TOTAL + 1)
        defaultFinishedSessionShouldBeFound("minutesTotal.lessThan=" + (DEFAULT_MINUTES_TOTAL + 1));
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByMinutesTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where minutesTotal is greater than DEFAULT_MINUTES_TOTAL
        defaultFinishedSessionShouldNotBeFound("minutesTotal.greaterThan=" + DEFAULT_MINUTES_TOTAL);

        // Get all the finishedSessionList where minutesTotal is greater than SMALLER_MINUTES_TOTAL
        defaultFinishedSessionShouldBeFound("minutesTotal.greaterThan=" + SMALLER_MINUTES_TOTAL);
    }


    @Test
    @Transactional
    public void getAllFinishedSessionsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where note equals to DEFAULT_NOTE
        defaultFinishedSessionShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the finishedSessionList where note equals to UPDATED_NOTE
        defaultFinishedSessionShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where note not equals to DEFAULT_NOTE
        defaultFinishedSessionShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the finishedSessionList where note not equals to UPDATED_NOTE
        defaultFinishedSessionShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultFinishedSessionShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the finishedSessionList where note equals to UPDATED_NOTE
        defaultFinishedSessionShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where note is not null
        defaultFinishedSessionShouldBeFound("note.specified=true");

        // Get all the finishedSessionList where note is null
        defaultFinishedSessionShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllFinishedSessionsByNoteContainsSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where note contains DEFAULT_NOTE
        defaultFinishedSessionShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the finishedSessionList where note contains UPDATED_NOTE
        defaultFinishedSessionShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where note does not contain DEFAULT_NOTE
        defaultFinishedSessionShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the finishedSessionList where note does not contain UPDATED_NOTE
        defaultFinishedSessionShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllFinishedSessionsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where createDate equals to DEFAULT_CREATE_DATE
        defaultFinishedSessionShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the finishedSessionList where createDate equals to UPDATED_CREATE_DATE
        defaultFinishedSessionShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where createDate not equals to DEFAULT_CREATE_DATE
        defaultFinishedSessionShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the finishedSessionList where createDate not equals to UPDATED_CREATE_DATE
        defaultFinishedSessionShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultFinishedSessionShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the finishedSessionList where createDate equals to UPDATED_CREATE_DATE
        defaultFinishedSessionShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where createDate is not null
        defaultFinishedSessionShouldBeFound("createDate.specified=true");

        // Get all the finishedSessionList where createDate is null
        defaultFinishedSessionShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultFinishedSessionShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the finishedSessionList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultFinishedSessionShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultFinishedSessionShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the finishedSessionList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultFinishedSessionShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultFinishedSessionShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the finishedSessionList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultFinishedSessionShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        // Get all the finishedSessionList where modifyDate is not null
        defaultFinishedSessionShouldBeFound("modifyDate.specified=true");

        // Get all the finishedSessionList where modifyDate is null
        defaultFinishedSessionShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllFinishedSessionsByPlanIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);
        Plan plan = PlanResourceIT.createEntity(em);
        em.persist(plan);
        em.flush();
        finishedSession.setPlan(plan);
        finishedSessionRepository.saveAndFlush(finishedSession);
        Long planId = plan.getId();

        // Get all the finishedSessionList where plan equals to planId
        defaultFinishedSessionShouldBeFound("planId.equals=" + planId);

        // Get all the finishedSessionList where plan equals to planId + 1
        defaultFinishedSessionShouldNotBeFound("planId.equals=" + (planId + 1));
    }


    @Test
    @Transactional
    public void getAllFinishedSessionsByExcerciseIsEqualToSomething() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);
        FinishedExcercise excercise = FinishedExcerciseResourceIT.createEntity(em);
        em.persist(excercise);
        em.flush();
        finishedSession.addExcercise(excercise);
        finishedSessionRepository.saveAndFlush(finishedSession);
        Long excerciseId = excercise.getId();

        // Get all the finishedSessionList where excercise equals to excerciseId
        defaultFinishedSessionShouldBeFound("excerciseId.equals=" + excerciseId);

        // Get all the finishedSessionList where excercise equals to excerciseId + 1
        defaultFinishedSessionShouldNotBeFound("excerciseId.equals=" + (excerciseId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinishedSessionShouldBeFound(String filter) throws Exception {
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(finishedSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].minutesTotal").value(hasItem(DEFAULT_MINUTES_TOTAL)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinishedSessionShouldNotBeFound(String filter) throws Exception {
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingFinishedSession() throws Exception {
        // Get the finishedSession
        restFinishedSessionMockMvc.perform(get("/api/finished-sessions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFinishedSession() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        int databaseSizeBeforeUpdate = finishedSessionRepository.findAll().size();

        // Update the finishedSession
        FinishedSession updatedFinishedSession = finishedSessionRepository.findById(finishedSession.getId()).get();
        // Disconnect from session so that the updates on updatedFinishedSession are not directly saved in db
        em.detach(updatedFinishedSession);
        updatedFinishedSession
            .minutesTotal(UPDATED_MINUTES_TOTAL)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        FinishedSessionDTO finishedSessionDTO = finishedSessionMapper.toDto(updatedFinishedSession);

        restFinishedSessionMockMvc.perform(put("/api/finished-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedSessionDTO)))
            .andExpect(status().isOk());

        // Validate the FinishedSession in the database
        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeUpdate);
        FinishedSession testFinishedSession = finishedSessionList.get(finishedSessionList.size() - 1);
        assertThat(testFinishedSession.getMinutesTotal()).isEqualTo(UPDATED_MINUTES_TOTAL);
        assertThat(testFinishedSession.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testFinishedSession.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFinishedSession.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingFinishedSession() throws Exception {
        int databaseSizeBeforeUpdate = finishedSessionRepository.findAll().size();

        // Create the FinishedSession
        FinishedSessionDTO finishedSessionDTO = finishedSessionMapper.toDto(finishedSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinishedSessionMockMvc.perform(put("/api/finished-sessions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(finishedSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FinishedSession in the database
        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFinishedSession() throws Exception {
        // Initialize the database
        finishedSessionRepository.saveAndFlush(finishedSession);

        int databaseSizeBeforeDelete = finishedSessionRepository.findAll().size();

        // Delete the finishedSession
        restFinishedSessionMockMvc.perform(delete("/api/finished-sessions/{id}", finishedSession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FinishedSession> finishedSessionList = finishedSessionRepository.findAll();
        assertThat(finishedSessionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedSession.class);
        FinishedSession finishedSession1 = new FinishedSession();
        finishedSession1.setId(1L);
        FinishedSession finishedSession2 = new FinishedSession();
        finishedSession2.setId(finishedSession1.getId());
        assertThat(finishedSession1).isEqualTo(finishedSession2);
        finishedSession2.setId(2L);
        assertThat(finishedSession1).isNotEqualTo(finishedSession2);
        finishedSession1.setId(null);
        assertThat(finishedSession1).isNotEqualTo(finishedSession2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FinishedSessionDTO.class);
        FinishedSessionDTO finishedSessionDTO1 = new FinishedSessionDTO();
        finishedSessionDTO1.setId(1L);
        FinishedSessionDTO finishedSessionDTO2 = new FinishedSessionDTO();
        assertThat(finishedSessionDTO1).isNotEqualTo(finishedSessionDTO2);
        finishedSessionDTO2.setId(finishedSessionDTO1.getId());
        assertThat(finishedSessionDTO1).isEqualTo(finishedSessionDTO2);
        finishedSessionDTO2.setId(2L);
        assertThat(finishedSessionDTO1).isNotEqualTo(finishedSessionDTO2);
        finishedSessionDTO1.setId(null);
        assertThat(finishedSessionDTO1).isNotEqualTo(finishedSessionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(finishedSessionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(finishedSessionMapper.fromId(null)).isNull();
    }
}
