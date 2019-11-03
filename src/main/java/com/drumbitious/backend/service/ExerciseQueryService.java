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

import com.drumbitious.backend.domain.Exercise;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.ExerciseRepository;
import com.drumbitious.backend.service.dto.ExerciseCriteria;
import com.drumbitious.backend.service.dto.ExerciseDTO;
import com.drumbitious.backend.service.mapper.ExerciseMapper;

/**
 * Service for executing complex queries for {@link Exercise} entities in the database.
 * The main input is a {@link ExerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExerciseDTO} or a {@link Page} of {@link ExerciseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExerciseQueryService extends QueryService<Exercise> {

    private final Logger log = LoggerFactory.getLogger(ExerciseQueryService.class);

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    public ExerciseQueryService(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    /**
     * Return a {@link List} of {@link ExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExerciseDTO> findByCriteria(ExerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseMapper.toDto(exerciseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExerciseDTO> findByCriteria(ExerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.findAll(specification, page)
            .map(exerciseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Exercise> specification = createSpecification(criteria);
        return exerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link ExerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Exercise> createSpecification(ExerciseCriteria criteria) {
        Specification<Exercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Exercise_.id));
            }
            if (criteria.getExerciseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExerciseName(), Exercise_.exerciseName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Exercise_.description));
            }
            if (criteria.getSourceUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceUrl(), Exercise_.sourceUrl));
            }
            if (criteria.getDefaultMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultMinutes(), Exercise_.defaultMinutes));
            }
            if (criteria.getDefaultBpmMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultBpmMin(), Exercise_.defaultBpmMin));
            }
            if (criteria.getDefaultBpmMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultBpmMax(), Exercise_.defaultBpmMax));
            }
            if (criteria.getDeactivted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeactivted(), Exercise_.deactivted));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Exercise_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), Exercise_.modifyDate));
            }
            if (criteria.getSkillType() != null) {
                specification = specification.and(buildSpecification(criteria.getSkillType(), Exercise_.skillType));
            }
            if (criteria.getExercise() != null) {
                specification = specification.and(buildSpecification(criteria.getExercise(), Exercise_.exercise));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(Exercise_.creator, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
