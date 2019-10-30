package com.drumbitious.backend.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.drumbitious.backend.domain.DrummerStatistics;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.DrummerStatisticsRepository;
import com.drumbitious.backend.service.dto.DrummerStatisticsCriteria;
import com.drumbitious.backend.service.dto.DrummerStatisticsDTO;
import com.drumbitious.backend.service.mapper.DrummerStatisticsMapper;

/**
 * Service for executing complex queries for {@link DrummerStatistics} entities in the database.
 * The main input is a {@link DrummerStatisticsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DrummerStatisticsDTO} or a {@link Page} of {@link DrummerStatisticsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DrummerStatisticsQueryService extends QueryService<DrummerStatistics> {

    private final Logger log = LoggerFactory.getLogger(DrummerStatisticsQueryService.class);

    private final DrummerStatisticsRepository drummerStatisticsRepository;

    private final DrummerStatisticsMapper drummerStatisticsMapper;

    public DrummerStatisticsQueryService(DrummerStatisticsRepository drummerStatisticsRepository, DrummerStatisticsMapper drummerStatisticsMapper) {
        this.drummerStatisticsRepository = drummerStatisticsRepository;
        this.drummerStatisticsMapper = drummerStatisticsMapper;
    }

    /**
     * Return a {@link List} of {@link DrummerStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DrummerStatisticsDTO> findByCriteria(DrummerStatisticsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DrummerStatistics> specification = createSpecification(criteria);
        return drummerStatisticsMapper.toDto(drummerStatisticsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DrummerStatisticsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DrummerStatisticsDTO> findByCriteria(DrummerStatisticsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DrummerStatistics> specification = createSpecification(criteria);
        return drummerStatisticsRepository.findAll(specification, page)
            .map(drummerStatisticsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DrummerStatisticsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DrummerStatistics> specification = createSpecification(criteria);
        return drummerStatisticsRepository.count(specification);
    }

    /**
     * Function to convert {@link DrummerStatisticsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DrummerStatistics> createSpecification(DrummerStatisticsCriteria criteria) {
        Specification<DrummerStatistics> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DrummerStatistics_.id));
            }
            if (criteria.getSelfAssessedLevelSpeed() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelSpeed(), DrummerStatistics_.selfAssessedLevelSpeed));
            }
            if (criteria.getSelfAssessedLevelGroove() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelGroove(), DrummerStatistics_.selfAssessedLevelGroove));
            }
            if (criteria.getSelfAssessedLevelCreativity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelCreativity(), DrummerStatistics_.selfAssessedLevelCreativity));
            }
            if (criteria.getSelfAssessedLevelAdaptability() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelAdaptability(), DrummerStatistics_.selfAssessedLevelAdaptability));
            }
            if (criteria.getSelfAssessedLevelDynamics() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelDynamics(), DrummerStatistics_.selfAssessedLevelDynamics));
            }
            if (criteria.getSelfAssessedLevelIndependence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelIndependence(), DrummerStatistics_.selfAssessedLevelIndependence));
            }
            if (criteria.getSelfAssessedLevelLivePerformance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelLivePerformance(), DrummerStatistics_.selfAssessedLevelLivePerformance));
            }
            if (criteria.getSelfAssessedLevelReadingMusic() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSelfAssessedLevelReadingMusic(), DrummerStatistics_.selfAssessedLevelReadingMusic));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), DrummerStatistics_.note));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), DrummerStatistics_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), DrummerStatistics_.modifyDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(DrummerStatistics_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
