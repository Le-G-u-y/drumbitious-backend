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

import com.drumbitious.backend.domain.ExcerciseConfig;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.ExcerciseConfigRepository;
import com.drumbitious.backend.service.dto.ExcerciseConfigCriteria;
import com.drumbitious.backend.service.dto.ExcerciseConfigDTO;
import com.drumbitious.backend.service.mapper.ExcerciseConfigMapper;

/**
 * Service for executing complex queries for {@link ExcerciseConfig} entities in the database.
 * The main input is a {@link ExcerciseConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExcerciseConfigDTO} or a {@link Page} of {@link ExcerciseConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExcerciseConfigQueryService extends QueryService<ExcerciseConfig> {

    private final Logger log = LoggerFactory.getLogger(ExcerciseConfigQueryService.class);

    private final ExcerciseConfigRepository excerciseConfigRepository;

    private final ExcerciseConfigMapper excerciseConfigMapper;

    public ExcerciseConfigQueryService(ExcerciseConfigRepository excerciseConfigRepository, ExcerciseConfigMapper excerciseConfigMapper) {
        this.excerciseConfigRepository = excerciseConfigRepository;
        this.excerciseConfigMapper = excerciseConfigMapper;
    }

    /**
     * Return a {@link List} of {@link ExcerciseConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExcerciseConfigDTO> findByCriteria(ExcerciseConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExcerciseConfig> specification = createSpecification(criteria);
        return excerciseConfigMapper.toDto(excerciseConfigRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExcerciseConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExcerciseConfigDTO> findByCriteria(ExcerciseConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExcerciseConfig> specification = createSpecification(criteria);
        return excerciseConfigRepository.findAll(specification, page)
            .map(excerciseConfigMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExcerciseConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExcerciseConfig> specification = createSpecification(criteria);
        return excerciseConfigRepository.count(specification);
    }

    /**
     * Function to convert {@link ExcerciseConfigCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExcerciseConfig> createSpecification(ExcerciseConfigCriteria criteria) {
        Specification<ExcerciseConfig> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ExcerciseConfig_.id));
            }
            if (criteria.getPracticeBpm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPracticeBpm(), ExcerciseConfig_.practiceBpm));
            }
            if (criteria.getTargetBpm() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTargetBpm(), ExcerciseConfig_.targetBpm));
            }
            if (criteria.getMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinutes(), ExcerciseConfig_.minutes));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), ExcerciseConfig_.note));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), ExcerciseConfig_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), ExcerciseConfig_.modifyDate));
            }
            if (criteria.getPlanId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanId(),
                    root -> root.join(ExcerciseConfig_.plans, JoinType.LEFT).get(Plan_.id)));
            }
            if (criteria.getExcerciseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExcerciseId(),
                    root -> root.join(ExcerciseConfig_.excercise, JoinType.LEFT).get(Excercise_.id)));
            }
        }
        return specification;
    }
}
