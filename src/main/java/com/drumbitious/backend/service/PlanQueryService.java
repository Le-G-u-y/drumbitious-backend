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

import com.drumbitious.backend.domain.Plan;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.PlanRepository;
import com.drumbitious.backend.service.dto.PlanCriteria;
import com.drumbitious.backend.service.dto.PlanDTO;
import com.drumbitious.backend.service.mapper.PlanMapper;

/**
 * Service for executing complex queries for {@link Plan} entities in the database.
 * The main input is a {@link PlanCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlanDTO} or a {@link Page} of {@link PlanDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlanQueryService extends QueryService<Plan> {

    private final Logger log = LoggerFactory.getLogger(PlanQueryService.class);

    private final PlanRepository planRepository;

    private final PlanMapper planMapper;

    public PlanQueryService(PlanRepository planRepository, PlanMapper planMapper) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
    }

    /**
     * Return a {@link List} of {@link PlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlanDTO> findByCriteria(PlanCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Plan> specification = createSpecification(criteria);
        return planMapper.toDto(planRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlanDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanDTO> findByCriteria(PlanCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plan> specification = createSpecification(criteria);
        return planRepository.findAll(specification, page)
            .map(planMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlanCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Plan> specification = createSpecification(criteria);
        return planRepository.count(specification);
    }

    /**
     * Function to convert {@link PlanCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plan> createSpecification(PlanCriteria criteria) {
        Specification<Plan> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Plan_.id));
            }
            if (criteria.getPlanName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlanName(), Plan_.planName));
            }
            if (criteria.getPlanFocus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlanFocus(), Plan_.planFocus));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Plan_.description));
            }
            if (criteria.getMinutesPerSession() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinutesPerSession(), Plan_.minutesPerSession));
            }
            if (criteria.getSessionsPerWeek() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSessionsPerWeek(), Plan_.sessionsPerWeek));
            }
            if (criteria.getTargetDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetDate(), Plan_.targetDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), Plan_.active));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Plan_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), Plan_.modifyDate));
            }
            if (criteria.getOwnerId() != null) {
                specification = specification.and(buildSpecification(criteria.getOwnerId(),
                    root -> root.join(Plan_.owner, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(Plan_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getExcerciseConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getExcerciseConfigId(),
                    root -> root.join(Plan_.excerciseConfig, JoinType.LEFT).get(ExcerciseConfig_.id)));
            }
        }
        return specification;
    }
}
