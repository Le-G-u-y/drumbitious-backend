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

import com.drumbitious.backend.domain.FinishedExercise;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.FinishedExerciseRepository;
import com.drumbitious.backend.service.dto.FinishedExerciseCriteria;
import com.drumbitious.backend.service.dto.FinishedExerciseDTO;
import com.drumbitious.backend.service.mapper.FinishedExerciseMapper;

/**
 * Service for executing complex queries for {@link FinishedExercise} entities in the database.
 * The main input is a {@link FinishedExerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FinishedExerciseDTO} or a {@link Page} of {@link FinishedExerciseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinishedExerciseQueryService extends QueryService<FinishedExercise> {

    private final Logger log = LoggerFactory.getLogger(FinishedExerciseQueryService.class);

    private final FinishedExerciseRepository finishedExerciseRepository;

    private final FinishedExerciseMapper finishedExerciseMapper;

    public FinishedExerciseQueryService(FinishedExerciseRepository finishedExerciseRepository, FinishedExerciseMapper finishedExerciseMapper) {
        this.finishedExerciseRepository = finishedExerciseRepository;
        this.finishedExerciseMapper = finishedExerciseMapper;
    }

    /**
     * Return a {@link List} of {@link FinishedExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FinishedExerciseDTO> findByCriteria(FinishedExerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FinishedExercise> specification = createSpecification(criteria);
        return finishedExerciseMapper.toDto(finishedExerciseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FinishedExerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinishedExerciseDTO> findByCriteria(FinishedExerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinishedExercise> specification = createSpecification(criteria);
        return finishedExerciseRepository.findAll(specification, page)
            .map(finishedExerciseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinishedExerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinishedExercise> specification = createSpecification(criteria);
        return finishedExerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link FinishedExerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinishedExercise> createSpecification(FinishedExerciseCriteria criteria) {
        Specification<FinishedExercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FinishedExercise_.id));
            }
            if (criteria.getActualBpm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualBpm(), FinishedExercise_.actualBpm));
            }
            if (criteria.getActualMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualMinutes(), FinishedExercise_.actualMinutes));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), FinishedExercise_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), FinishedExercise_.modifyDate));
            }
            if (criteria.getFinishedSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getFinishedSessionId(),
                    root -> root.join(FinishedExercise_.finishedSession, JoinType.LEFT).get(FinishedSession_.id)));
            }
            if (criteria.getExerciseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseId(),
                    root -> root.join(FinishedExercise_.exercise, JoinType.LEFT).get(Exercise_.id)));
            }
        }
        return specification;
    }
}
