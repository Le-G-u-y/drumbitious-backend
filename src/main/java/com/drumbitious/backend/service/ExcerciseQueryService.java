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

import com.drumbitious.backend.domain.Excercise;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.ExcerciseRepository;
import com.drumbitious.backend.service.dto.ExcerciseCriteria;
import com.drumbitious.backend.service.dto.ExcerciseDTO;
import com.drumbitious.backend.service.mapper.ExcerciseMapper;

/**
 * Service for executing complex queries for {@link Excercise} entities in the database.
 * The main input is a {@link ExcerciseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExcerciseDTO} or a {@link Page} of {@link ExcerciseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExcerciseQueryService extends QueryService<Excercise> {

    private final Logger log = LoggerFactory.getLogger(ExcerciseQueryService.class);

    private final ExcerciseRepository excerciseRepository;

    private final ExcerciseMapper excerciseMapper;

    public ExcerciseQueryService(ExcerciseRepository excerciseRepository, ExcerciseMapper excerciseMapper) {
        this.excerciseRepository = excerciseRepository;
        this.excerciseMapper = excerciseMapper;
    }

    /**
     * Return a {@link List} of {@link ExcerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExcerciseDTO> findByCriteria(ExcerciseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Excercise> specification = createSpecification(criteria);
        return excerciseMapper.toDto(excerciseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExcerciseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExcerciseDTO> findByCriteria(ExcerciseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Excercise> specification = createSpecification(criteria);
        return excerciseRepository.findAll(specification, page)
            .map(excerciseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExcerciseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Excercise> specification = createSpecification(criteria);
        return excerciseRepository.count(specification);
    }

    /**
     * Function to convert {@link ExcerciseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Excercise> createSpecification(ExcerciseCriteria criteria) {
        Specification<Excercise> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Excercise_.id));
            }
            if (criteria.getExcerciseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getExcerciseName(), Excercise_.excerciseName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Excercise_.description));
            }
            if (criteria.getSourceUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSourceUrl(), Excercise_.sourceUrl));
            }
            if (criteria.getDefaultMinutes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultMinutes(), Excercise_.defaultMinutes));
            }
            if (criteria.getDefaultBpmMin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultBpmMin(), Excercise_.defaultBpmMin));
            }
            if (criteria.getDefaultBpmMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDefaultBpmMax(), Excercise_.defaultBpmMax));
            }
            if (criteria.getDeactivted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeactivted(), Excercise_.deactivted));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), Excercise_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), Excercise_.modifyDate));
            }
            if (criteria.getCreatorId() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatorId(),
                    root -> root.join(Excercise_.creator, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getSkillNameId() != null) {
                specification = specification.and(buildSpecification(criteria.getSkillNameId(),
                    root -> root.join(Excercise_.skillNames, JoinType.LEFT).get(SkillType_.id)));
            }
            if (criteria.getExcerciseTypeId() != null) {
                specification = specification.and(buildSpecification(criteria.getExcerciseTypeId(),
                    root -> root.join(Excercise_.excerciseTypes, JoinType.LEFT).get(ExcerciseType_.id)));
            }
        }
        return specification;
    }
}
