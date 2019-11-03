package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.DrumbitiousBackendApp;
import com.drumbitious.backend.domain.DrummerStatistics;
import com.drumbitious.backend.domain.User;
import com.drumbitious.backend.repository.DrummerStatisticsRepository;
import com.drumbitious.backend.service.DrummerStatisticsService;
import com.drumbitious.backend.service.dto.DrummerStatisticsDTO;
import com.drumbitious.backend.service.mapper.DrummerStatisticsMapper;
import com.drumbitious.backend.web.rest.errors.ExceptionTranslator;
import com.drumbitious.backend.service.dto.DrummerStatisticsCriteria;
import com.drumbitious.backend.service.DrummerStatisticsQueryService;

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
 * Integration tests for the {@link DrummerStatisticsResource} REST controller.
 */
@SpringBootTest(classes = DrumbitiousBackendApp.class)
public class DrummerStatisticsResourceIT {

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_SPEED = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_SPEED = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_SPEED = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_GROOVE = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_GROOVE = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_GROOVE = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_CREATIVITY = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_ADAPTABILITY = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_DYNAMICS = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_INDEPENDENCE = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE = 0 - 1;

    private static final Integer DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC = 0;
    private static final Integer UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC = 1;
    private static final Integer SMALLER_SELF_ASSESSED_LEVEL_READING_MUSIC = 0 - 1;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DrummerStatisticsRepository drummerStatisticsRepository;

    @Autowired
    private DrummerStatisticsMapper drummerStatisticsMapper;

    @Autowired
    private DrummerStatisticsService drummerStatisticsService;

    @Autowired
    private DrummerStatisticsQueryService drummerStatisticsQueryService;

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

    private MockMvc restDrummerStatisticsMockMvc;

    private DrummerStatistics drummerStatistics;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DrummerStatisticsResource drummerStatisticsResource = new DrummerStatisticsResource(drummerStatisticsService, drummerStatisticsQueryService);
        this.restDrummerStatisticsMockMvc = MockMvcBuilders.standaloneSetup(drummerStatisticsResource)
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
    public static DrummerStatistics createEntity(EntityManager em) {
        DrummerStatistics drummerStatistics = new DrummerStatistics()
            .selfAssessedLevelSpeed(DEFAULT_SELF_ASSESSED_LEVEL_SPEED)
            .selfAssessedLevelGroove(DEFAULT_SELF_ASSESSED_LEVEL_GROOVE)
            .selfAssessedLevelCreativity(DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY)
            .selfAssessedLevelAdaptability(DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY)
            .selfAssessedLevelDynamics(DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS)
            .selfAssessedLevelIndependence(DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE)
            .selfAssessedLevelLivePerformance(DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE)
            .selfAssessedLevelReadingMusic(DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC)
            .note(DEFAULT_NOTE)
            .createDate(DEFAULT_CREATE_DATE)
            .modifyDate(DEFAULT_MODIFY_DATE);
        return drummerStatistics;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DrummerStatistics createUpdatedEntity(EntityManager em) {
        DrummerStatistics drummerStatistics = new DrummerStatistics()
            .selfAssessedLevelSpeed(UPDATED_SELF_ASSESSED_LEVEL_SPEED)
            .selfAssessedLevelGroove(UPDATED_SELF_ASSESSED_LEVEL_GROOVE)
            .selfAssessedLevelCreativity(UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY)
            .selfAssessedLevelAdaptability(UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY)
            .selfAssessedLevelDynamics(UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS)
            .selfAssessedLevelIndependence(UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE)
            .selfAssessedLevelLivePerformance(UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE)
            .selfAssessedLevelReadingMusic(UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        return drummerStatistics;
    }

    @BeforeEach
    public void initTest() {
        drummerStatistics = createEntity(em);
    }

    @Test
    @Transactional
    public void createDrummerStatistics() throws Exception {
        int databaseSizeBeforeCreate = drummerStatisticsRepository.findAll().size();

        // Create the DrummerStatistics
        DrummerStatisticsDTO drummerStatisticsDTO = drummerStatisticsMapper.toDto(drummerStatistics);
        restDrummerStatisticsMockMvc.perform(post("/api/drummer-statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drummerStatisticsDTO)))
            .andExpect(status().isCreated());

        // Validate the DrummerStatistics in the database
        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeCreate + 1);
        DrummerStatistics testDrummerStatistics = drummerStatisticsList.get(drummerStatisticsList.size() - 1);
        assertThat(testDrummerStatistics.getSelfAssessedLevelSpeed()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_SPEED);
        assertThat(testDrummerStatistics.getSelfAssessedLevelGroove()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);
        assertThat(testDrummerStatistics.getSelfAssessedLevelCreativity()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);
        assertThat(testDrummerStatistics.getSelfAssessedLevelAdaptability()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);
        assertThat(testDrummerStatistics.getSelfAssessedLevelDynamics()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);
        assertThat(testDrummerStatistics.getSelfAssessedLevelIndependence()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);
        assertThat(testDrummerStatistics.getSelfAssessedLevelLivePerformance()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
        assertThat(testDrummerStatistics.getSelfAssessedLevelReadingMusic()).isEqualTo(DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);
        assertThat(testDrummerStatistics.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testDrummerStatistics.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testDrummerStatistics.getModifyDate()).isEqualTo(DEFAULT_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void createDrummerStatisticsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = drummerStatisticsRepository.findAll().size();

        // Create the DrummerStatistics with an existing ID
        drummerStatistics.setId(1L);
        DrummerStatisticsDTO drummerStatisticsDTO = drummerStatisticsMapper.toDto(drummerStatistics);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDrummerStatisticsMockMvc.perform(post("/api/drummer-statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drummerStatisticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DrummerStatistics in the database
        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = drummerStatisticsRepository.findAll().size();
        // set the field null
        drummerStatistics.setCreateDate(null);

        // Create the DrummerStatistics, which fails.
        DrummerStatisticsDTO drummerStatisticsDTO = drummerStatisticsMapper.toDto(drummerStatistics);

        restDrummerStatisticsMockMvc.perform(post("/api/drummer-statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drummerStatisticsDTO)))
            .andExpect(status().isBadRequest());

        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = drummerStatisticsRepository.findAll().size();
        // set the field null
        drummerStatistics.setModifyDate(null);

        // Create the DrummerStatistics, which fails.
        DrummerStatisticsDTO drummerStatisticsDTO = drummerStatisticsMapper.toDto(drummerStatistics);

        restDrummerStatisticsMockMvc.perform(post("/api/drummer-statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drummerStatisticsDTO)))
            .andExpect(status().isBadRequest());

        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDrummerStatistics() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drummerStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].selfAssessedLevelSpeed").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_SPEED)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelGroove").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_GROOVE)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelCreativity").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelAdaptability").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelDynamics").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelIndependence").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelLivePerformance").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelReadingMusic").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getDrummerStatistics() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get the drummerStatistics
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics/{id}", drummerStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(drummerStatistics.getId().intValue()))
            .andExpect(jsonPath("$.selfAssessedLevelSpeed").value(DEFAULT_SELF_ASSESSED_LEVEL_SPEED))
            .andExpect(jsonPath("$.selfAssessedLevelGroove").value(DEFAULT_SELF_ASSESSED_LEVEL_GROOVE))
            .andExpect(jsonPath("$.selfAssessedLevelCreativity").value(DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY))
            .andExpect(jsonPath("$.selfAssessedLevelAdaptability").value(DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY))
            .andExpect(jsonPath("$.selfAssessedLevelDynamics").value(DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS))
            .andExpect(jsonPath("$.selfAssessedLevelIndependence").value(DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE))
            .andExpect(jsonPath("$.selfAssessedLevelLivePerformance").value(DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE))
            .andExpect(jsonPath("$.selfAssessedLevelReadingMusic").value(DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.modifyDate").value(DEFAULT_MODIFY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed equals to DEFAULT_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed equals to UPDATED_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.equals=" + UPDATED_SELF_ASSESSED_LEVEL_SPEED);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed not equals to DEFAULT_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed not equals to UPDATED_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_SPEED);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed in DEFAULT_SELF_ASSESSED_LEVEL_SPEED or UPDATED_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.in=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED + "," + UPDATED_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed equals to UPDATED_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.in=" + UPDATED_SELF_ASSESSED_LEVEL_SPEED);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_SPEED + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_SPEED + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_SPEED);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is less than DEFAULT_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is less than (DEFAULT_SELF_ASSESSED_LEVEL_SPEED + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_SPEED + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelSpeedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is greater than DEFAULT_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelSpeed.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_SPEED);

        // Get all the drummerStatisticsList where selfAssessedLevelSpeed is greater than SMALLER_SELF_ASSESSED_LEVEL_SPEED
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelSpeed.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_SPEED);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove equals to DEFAULT_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove equals to UPDATED_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.equals=" + UPDATED_SELF_ASSESSED_LEVEL_GROOVE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove not equals to DEFAULT_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove not equals to UPDATED_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_GROOVE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove in DEFAULT_SELF_ASSESSED_LEVEL_GROOVE or UPDATED_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.in=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE + "," + UPDATED_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove equals to UPDATED_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.in=" + UPDATED_SELF_ASSESSED_LEVEL_GROOVE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_GROOVE + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_GROOVE + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_GROOVE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is less than DEFAULT_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is less than (DEFAULT_SELF_ASSESSED_LEVEL_GROOVE + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_GROOVE + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelGrooveIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is greater than DEFAULT_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelGroove.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_GROOVE);

        // Get all the drummerStatisticsList where selfAssessedLevelGroove is greater than SMALLER_SELF_ASSESSED_LEVEL_GROOVE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelGroove.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_GROOVE);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity equals to DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity equals to UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.equals=" + UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity not equals to DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity not equals to UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity in DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY or UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.in=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY + "," + UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity equals to UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.in=" + UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_CREATIVITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is less than DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is less than (DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelCreativityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is greater than DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelCreativity.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY);

        // Get all the drummerStatisticsList where selfAssessedLevelCreativity is greater than SMALLER_SELF_ASSESSED_LEVEL_CREATIVITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelCreativity.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_CREATIVITY);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability equals to DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability equals to UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.equals=" + UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability not equals to DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability not equals to UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability in DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY or UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.in=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY + "," + UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability equals to UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.in=" + UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_ADAPTABILITY);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is less than DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is less than (DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelAdaptabilityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is greater than DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelAdaptability.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY);

        // Get all the drummerStatisticsList where selfAssessedLevelAdaptability is greater than SMALLER_SELF_ASSESSED_LEVEL_ADAPTABILITY
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelAdaptability.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_ADAPTABILITY);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics equals to DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics equals to UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.equals=" + UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics not equals to DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics not equals to UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics in DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS or UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.in=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS + "," + UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics equals to UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.in=" + UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_DYNAMICS);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is less than DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is less than (DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelDynamicsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is greater than DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelDynamics.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS);

        // Get all the drummerStatisticsList where selfAssessedLevelDynamics is greater than SMALLER_SELF_ASSESSED_LEVEL_DYNAMICS
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelDynamics.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_DYNAMICS);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence equals to DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence equals to UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.equals=" + UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence not equals to DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence not equals to UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence in DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE or UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.in=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE + "," + UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence equals to UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.in=" + UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_INDEPENDENCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is less than DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is less than (DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelIndependenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is greater than DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelIndependence.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE);

        // Get all the drummerStatisticsList where selfAssessedLevelIndependence is greater than SMALLER_SELF_ASSESSED_LEVEL_INDEPENDENCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelIndependence.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_INDEPENDENCE);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance equals to DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance equals to UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.equals=" + UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance not equals to DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance not equals to UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance in DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE or UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.in=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE + "," + UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance equals to UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.in=" + UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is less than DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is less than (DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelLivePerformanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is greater than DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelLivePerformance.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);

        // Get all the drummerStatisticsList where selfAssessedLevelLivePerformance is greater than SMALLER_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelLivePerformance.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic equals to DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.equals=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic equals to UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.equals=" + UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic not equals to DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.notEquals=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic not equals to UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.notEquals=" + UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic in DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC or UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.in=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC + "," + UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic equals to UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.in=" + UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is not null
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.specified=true");

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is null
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is greater than or equal to DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.greaterThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is greater than or equal to (DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC + 1)
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.greaterThanOrEqual=" + (DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is less than or equal to DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.lessThanOrEqual=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is less than or equal to SMALLER_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.lessThanOrEqual=" + SMALLER_SELF_ASSESSED_LEVEL_READING_MUSIC);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsLessThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is less than DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.lessThan=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is less than (DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC + 1)
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.lessThan=" + (DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC + 1));
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsBySelfAssessedLevelReadingMusicIsGreaterThanSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is greater than DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldNotBeFound("selfAssessedLevelReadingMusic.greaterThan=" + DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC);

        // Get all the drummerStatisticsList where selfAssessedLevelReadingMusic is greater than SMALLER_SELF_ASSESSED_LEVEL_READING_MUSIC
        defaultDrummerStatisticsShouldBeFound("selfAssessedLevelReadingMusic.greaterThan=" + SMALLER_SELF_ASSESSED_LEVEL_READING_MUSIC);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where note equals to DEFAULT_NOTE
        defaultDrummerStatisticsShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the drummerStatisticsList where note equals to UPDATED_NOTE
        defaultDrummerStatisticsShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where note not equals to DEFAULT_NOTE
        defaultDrummerStatisticsShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the drummerStatisticsList where note not equals to UPDATED_NOTE
        defaultDrummerStatisticsShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultDrummerStatisticsShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the drummerStatisticsList where note equals to UPDATED_NOTE
        defaultDrummerStatisticsShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where note is not null
        defaultDrummerStatisticsShouldBeFound("note.specified=true");

        // Get all the drummerStatisticsList where note is null
        defaultDrummerStatisticsShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllDrummerStatisticsByNoteContainsSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where note contains DEFAULT_NOTE
        defaultDrummerStatisticsShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the drummerStatisticsList where note contains UPDATED_NOTE
        defaultDrummerStatisticsShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where note does not contain DEFAULT_NOTE
        defaultDrummerStatisticsShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the drummerStatisticsList where note does not contain UPDATED_NOTE
        defaultDrummerStatisticsShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllDrummerStatisticsByCreateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where createDate equals to DEFAULT_CREATE_DATE
        defaultDrummerStatisticsShouldBeFound("createDate.equals=" + DEFAULT_CREATE_DATE);

        // Get all the drummerStatisticsList where createDate equals to UPDATED_CREATE_DATE
        defaultDrummerStatisticsShouldNotBeFound("createDate.equals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByCreateDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where createDate not equals to DEFAULT_CREATE_DATE
        defaultDrummerStatisticsShouldNotBeFound("createDate.notEquals=" + DEFAULT_CREATE_DATE);

        // Get all the drummerStatisticsList where createDate not equals to UPDATED_CREATE_DATE
        defaultDrummerStatisticsShouldBeFound("createDate.notEquals=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByCreateDateIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where createDate in DEFAULT_CREATE_DATE or UPDATED_CREATE_DATE
        defaultDrummerStatisticsShouldBeFound("createDate.in=" + DEFAULT_CREATE_DATE + "," + UPDATED_CREATE_DATE);

        // Get all the drummerStatisticsList where createDate equals to UPDATED_CREATE_DATE
        defaultDrummerStatisticsShouldNotBeFound("createDate.in=" + UPDATED_CREATE_DATE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByCreateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where createDate is not null
        defaultDrummerStatisticsShouldBeFound("createDate.specified=true");

        // Get all the drummerStatisticsList where createDate is null
        defaultDrummerStatisticsShouldNotBeFound("createDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByModifyDateIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where modifyDate equals to DEFAULT_MODIFY_DATE
        defaultDrummerStatisticsShouldBeFound("modifyDate.equals=" + DEFAULT_MODIFY_DATE);

        // Get all the drummerStatisticsList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultDrummerStatisticsShouldNotBeFound("modifyDate.equals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByModifyDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where modifyDate not equals to DEFAULT_MODIFY_DATE
        defaultDrummerStatisticsShouldNotBeFound("modifyDate.notEquals=" + DEFAULT_MODIFY_DATE);

        // Get all the drummerStatisticsList where modifyDate not equals to UPDATED_MODIFY_DATE
        defaultDrummerStatisticsShouldBeFound("modifyDate.notEquals=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByModifyDateIsInShouldWork() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where modifyDate in DEFAULT_MODIFY_DATE or UPDATED_MODIFY_DATE
        defaultDrummerStatisticsShouldBeFound("modifyDate.in=" + DEFAULT_MODIFY_DATE + "," + UPDATED_MODIFY_DATE);

        // Get all the drummerStatisticsList where modifyDate equals to UPDATED_MODIFY_DATE
        defaultDrummerStatisticsShouldNotBeFound("modifyDate.in=" + UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByModifyDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        // Get all the drummerStatisticsList where modifyDate is not null
        defaultDrummerStatisticsShouldBeFound("modifyDate.specified=true");

        // Get all the drummerStatisticsList where modifyDate is null
        defaultDrummerStatisticsShouldNotBeFound("modifyDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDrummerStatisticsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        drummerStatistics.setUser(user);
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);
        Long userId = user.getId();

        // Get all the drummerStatisticsList where user equals to userId
        defaultDrummerStatisticsShouldBeFound("userId.equals=" + userId);

        // Get all the drummerStatisticsList where user equals to userId + 1
        defaultDrummerStatisticsShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDrummerStatisticsShouldBeFound(String filter) throws Exception {
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(drummerStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].selfAssessedLevelSpeed").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_SPEED)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelGroove").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_GROOVE)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelCreativity").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_CREATIVITY)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelAdaptability").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_ADAPTABILITY)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelDynamics").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_DYNAMICS)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelIndependence").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_INDEPENDENCE)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelLivePerformance").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE)))
            .andExpect(jsonPath("$.[*].selfAssessedLevelReadingMusic").value(hasItem(DEFAULT_SELF_ASSESSED_LEVEL_READING_MUSIC)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].modifyDate").value(hasItem(DEFAULT_MODIFY_DATE.toString())));

        // Check, that the count call also returns 1
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDrummerStatisticsShouldNotBeFound(String filter) throws Exception {
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDrummerStatistics() throws Exception {
        // Get the drummerStatistics
        restDrummerStatisticsMockMvc.perform(get("/api/drummer-statistics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDrummerStatistics() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        int databaseSizeBeforeUpdate = drummerStatisticsRepository.findAll().size();

        // Update the drummerStatistics
        DrummerStatistics updatedDrummerStatistics = drummerStatisticsRepository.findById(drummerStatistics.getId()).get();
        // Disconnect from session so that the updates on updatedDrummerStatistics are not directly saved in db
        em.detach(updatedDrummerStatistics);
        updatedDrummerStatistics
            .selfAssessedLevelSpeed(UPDATED_SELF_ASSESSED_LEVEL_SPEED)
            .selfAssessedLevelGroove(UPDATED_SELF_ASSESSED_LEVEL_GROOVE)
            .selfAssessedLevelCreativity(UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY)
            .selfAssessedLevelAdaptability(UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY)
            .selfAssessedLevelDynamics(UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS)
            .selfAssessedLevelIndependence(UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE)
            .selfAssessedLevelLivePerformance(UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE)
            .selfAssessedLevelReadingMusic(UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC)
            .note(UPDATED_NOTE)
            .createDate(UPDATED_CREATE_DATE)
            .modifyDate(UPDATED_MODIFY_DATE);
        DrummerStatisticsDTO drummerStatisticsDTO = drummerStatisticsMapper.toDto(updatedDrummerStatistics);

        restDrummerStatisticsMockMvc.perform(put("/api/drummer-statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drummerStatisticsDTO)))
            .andExpect(status().isOk());

        // Validate the DrummerStatistics in the database
        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeUpdate);
        DrummerStatistics testDrummerStatistics = drummerStatisticsList.get(drummerStatisticsList.size() - 1);
        assertThat(testDrummerStatistics.getSelfAssessedLevelSpeed()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_SPEED);
        assertThat(testDrummerStatistics.getSelfAssessedLevelGroove()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_GROOVE);
        assertThat(testDrummerStatistics.getSelfAssessedLevelCreativity()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_CREATIVITY);
        assertThat(testDrummerStatistics.getSelfAssessedLevelAdaptability()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_ADAPTABILITY);
        assertThat(testDrummerStatistics.getSelfAssessedLevelDynamics()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_DYNAMICS);
        assertThat(testDrummerStatistics.getSelfAssessedLevelIndependence()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_INDEPENDENCE);
        assertThat(testDrummerStatistics.getSelfAssessedLevelLivePerformance()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_LIVE_PERFORMANCE);
        assertThat(testDrummerStatistics.getSelfAssessedLevelReadingMusic()).isEqualTo(UPDATED_SELF_ASSESSED_LEVEL_READING_MUSIC);
        assertThat(testDrummerStatistics.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testDrummerStatistics.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testDrummerStatistics.getModifyDate()).isEqualTo(UPDATED_MODIFY_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDrummerStatistics() throws Exception {
        int databaseSizeBeforeUpdate = drummerStatisticsRepository.findAll().size();

        // Create the DrummerStatistics
        DrummerStatisticsDTO drummerStatisticsDTO = drummerStatisticsMapper.toDto(drummerStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDrummerStatisticsMockMvc.perform(put("/api/drummer-statistics")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(drummerStatisticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DrummerStatistics in the database
        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDrummerStatistics() throws Exception {
        // Initialize the database
        drummerStatisticsRepository.saveAndFlush(drummerStatistics);

        int databaseSizeBeforeDelete = drummerStatisticsRepository.findAll().size();

        // Delete the drummerStatistics
        restDrummerStatisticsMockMvc.perform(delete("/api/drummer-statistics/{id}", drummerStatistics.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DrummerStatistics> drummerStatisticsList = drummerStatisticsRepository.findAll();
        assertThat(drummerStatisticsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrummerStatistics.class);
        DrummerStatistics drummerStatistics1 = new DrummerStatistics();
        drummerStatistics1.setId(1L);
        DrummerStatistics drummerStatistics2 = new DrummerStatistics();
        drummerStatistics2.setId(drummerStatistics1.getId());
        assertThat(drummerStatistics1).isEqualTo(drummerStatistics2);
        drummerStatistics2.setId(2L);
        assertThat(drummerStatistics1).isNotEqualTo(drummerStatistics2);
        drummerStatistics1.setId(null);
        assertThat(drummerStatistics1).isNotEqualTo(drummerStatistics2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DrummerStatisticsDTO.class);
        DrummerStatisticsDTO drummerStatisticsDTO1 = new DrummerStatisticsDTO();
        drummerStatisticsDTO1.setId(1L);
        DrummerStatisticsDTO drummerStatisticsDTO2 = new DrummerStatisticsDTO();
        assertThat(drummerStatisticsDTO1).isNotEqualTo(drummerStatisticsDTO2);
        drummerStatisticsDTO2.setId(drummerStatisticsDTO1.getId());
        assertThat(drummerStatisticsDTO1).isEqualTo(drummerStatisticsDTO2);
        drummerStatisticsDTO2.setId(2L);
        assertThat(drummerStatisticsDTO1).isNotEqualTo(drummerStatisticsDTO2);
        drummerStatisticsDTO1.setId(null);
        assertThat(drummerStatisticsDTO1).isNotEqualTo(drummerStatisticsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(drummerStatisticsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(drummerStatisticsMapper.fromId(null)).isNull();
    }
}
