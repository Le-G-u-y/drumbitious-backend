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

import com.drumbitious.backend.domain.FinishedSession;
import com.drumbitious.backend.domain.*; // for static metamodels
import com.drumbitious.backend.repository.FinishedSessionRepository;
import com.drumbitious.backend.service.dto.FinishedSessionCriteria;
import com.drumbitious.backend.service.dto.FinishedSessionDTO;
import com.drumbitious.backend.service.mapper.FinishedSessionMapper;

/**
 * Service for executing complex queries for {@link FinishedSession} entities in the database.
 * The main input is a {@link FinishedSessionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FinishedSessionDTO} or a {@link Page} of {@link FinishedSessionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinishedSessionQueryService extends QueryService<FinishedSession> {

    private final Logger log = LoggerFactory.getLogger(FinishedSessionQueryService.class);

    private final FinishedSessionRepository finishedSessionRepository;

    private final FinishedSessionMapper finishedSessionMapper;

    public FinishedSessionQueryService(FinishedSessionRepository finishedSessionRepository, FinishedSessionMapper finishedSessionMapper) {
        this.finishedSessionRepository = finishedSessionRepository;
        this.finishedSessionMapper = finishedSessionMapper;
    }

    /**
     * Return a {@link List} of {@link FinishedSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FinishedSessionDTO> findByCriteria(FinishedSessionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FinishedSession> specification = createSpecification(criteria);
        return finishedSessionMapper.toDto(finishedSessionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FinishedSessionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinishedSessionDTO> findByCriteria(FinishedSessionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinishedSession> specification = createSpecification(criteria);
        return finishedSessionRepository.findAll(specification, page)
            .map(finishedSessionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinishedSessionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinishedSession> specification = createSpecification(criteria);
        return finishedSessionRepository.count(specification);
    }

    /**
     * Function to convert {@link FinishedSessionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinishedSession> createSpecification(FinishedSessionCriteria criteria) {
        Specification<FinishedSession> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), FinishedSession_.id));
            }
            if (criteria.getMinutesTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMinutesTotal(), FinishedSession_.minutesTotal));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), FinishedSession_.note));
            }
            if (criteria.getCreateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateDate(), FinishedSession_.createDate));
            }
            if (criteria.getModifyDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifyDate(), FinishedSession_.modifyDate));
            }
            if (criteria.getPlanId() != null) {
                specification = specification.and(buildSpecification(criteria.getPlanId(),
                    root -> root.join(FinishedSession_.plan, JoinType.LEFT).get(Plan_.id)));
            }
            if (criteria.getExerciseId() != null) {
                specification = specification.and(buildSpecification(criteria.getExerciseId(),
                    root -> root.join(FinishedSession_.exercises, JoinType.LEFT).get(FinishedExercise_.id)));
            }
        }
        return specification;
    }
}
