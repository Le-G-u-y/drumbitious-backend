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

import com.drumbitious.backend.domain.FinishedExcercise;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.FinishedExcerciseRepository;
import com.drumbitious.backend.service.dto.FinishedExcerciseCriteria;
import com.drumbitious.backend.service.dto.FinishedExcerciseDTO;
import com.drumbitious.backend.service.mapper.FinishedExcerciseMapper;

/**
 * Service for executing complex queries for {@link FinishedExcercise} entities in the database.
 * The main input is a {@link FinishedExcerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FinishedExcerciseDTO} or a {@link Page} of {@link FinishedExcerciseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinishedExcerciseQueryService extends QueryService<FinishedExcercise> {

    private final Logger log = LoggerFactory.getLogger(FinishedExcerciseQueryService.class);

    private final FinishedExcerciseRepository finishedExcerciseRepository;

    private final FinishedExcerciseMapper finishedExcerciseMapper;

    public FinishedExcerciseQueryService(FinishedExcerciseRepository finishedExcerciseRepository, FinishedExcerciseMapper finishedExcerciseMapper) {
        this.finishedExcerciseRepository = finishedExcerciseRepository;
        this.finishedExcerciseMapper = finishedExcerciseMapper;
    }

    /**
     * Return a {@link List} of {@link FinishedExcerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FinishedExcerciseDTO> findByCriteria(FinishedExcerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FinishedExcercise> specification = createSpecification(criteria);
        return finishedExcerciseMapper.toDto(finishedExcerciseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FinishedExcerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinishedExcerciseDTO> findByCriteria(FinishedExcerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinishedExcercise> specification = createSpecification(criteria);
        return finishedExcerciseRepository.findAll(specification, page)
            .map(finishedExcerciseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinishedExcerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinishedExcercise> specification = createSpecification(criteria);
        return finishedExcerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link FinishedExcerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinishedExcercise> createSpecification(FinishedExcerciseCriteria criteria) {
        Specification<FinishedExcercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FinishedExcercise_.id));
            }
            if (criteria.getActualBpm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualBpm(), FinishedExcercise_.actualBpm));
            }
            if (criteria.getActualMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualMinutes(), FinishedExcercise_.actualMinutes));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), FinishedExcercise_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), FinishedExcercise_.modifyDate));
            }
            if (criteria.getFinishedSessionId() != null) {
                specification = specification.and(buildSpecification(criteria.getFinishedSessionId(),
                    root -> root.join(FinishedExcercise_.finishedSession, JoinType.LEFT).get(FinishedSession_.id)));
            }
            if (criteria.getExcerciseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExcerciseId(),
                    root -> root.join(FinishedExcercise_.excercise, JoinType.LEFT).get(Excercise_.id)));
            }
        }
        return specification;
    }
}
