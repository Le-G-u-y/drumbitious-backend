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

import com.drumbitious.backend.domain.ExerciseConfig;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.ExerciseConfigRepository;
import com.drumbitious.backend.service.dto.ExerciseConfigCriteria;
import com.drumbitious.backend.service.dto.ExerciseConfigDTO;
import com.drumbitious.backend.service.mapper.ExerciseConfigMapper;

/**
 * Service for executing complex queries for {@link ExerciseConfig} entities in the database.
 * The main input is a {@link ExerciseConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseConfigDTO} or a {@link Page} of {@link ExerciseConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseConfigQueryService extends QueryService<ExerciseConfig> {

    private final Logger log = LoggerFactory.getLogger(ExerciseConfigQueryService.class);

    private final ExerciseConfigRepository exerciseConfigRepository;

    private final ExerciseConfigMapper exerciseConfigMapper;

    public ExerciseConfigQueryService(ExerciseConfigRepository exerciseConfigRepository, ExerciseConfigMapper exerciseConfigMapper) {
        this.exerciseConfigRepository = exerciseConfigRepository;
        this.exerciseConfigMapper = exerciseConfigMapper;
    }

    /**
     * Return a {@link List} of {@link ExerciseConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseConfigDTO> findByCriteria(ExerciseConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExerciseConfig> specification = createSpecification(criteria);
        return exerciseConfigMapper.toDto(exerciseConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExerciseConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseConfigDTO> findByCriteria(ExerciseConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExerciseConfig> specification = createSpecification(criteria);
        return exerciseConfigRepository.findAll(specification, page)
            .map(exerciseConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExerciseConfig> specification = createSpecification(criteria);
        return exerciseConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExerciseConfig> createSpecification(ExerciseConfigCriteria criteria) {
        Specification<ExerciseConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ExerciseConfig_.id));
            }
            if (criteria.getPracticeBpm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPracticeBpm(), ExerciseConfig_.practiceBpm));
            }
            if (criteria.getTargetBpm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetBpm(), ExerciseConfig_.targetBpm));
            }
            if (criteria.getMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinutes(), ExerciseConfig_.minutes));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), ExerciseConfig_.note));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), ExerciseConfig_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), ExerciseConfig_.modifyDate));
            }
            if (criteria.getPlanId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanId(),
                    root -> root.join(ExerciseConfig_.plans, JoinType.LEFT).get(Plan_.id)));
            }
            if (criteria.getExerciseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseId(),
                    root -> root.join(ExerciseConfig_.exercise, JoinType.LEFT).get(Exercise_.id)));
            }
        }
        return specification;
    }
}
