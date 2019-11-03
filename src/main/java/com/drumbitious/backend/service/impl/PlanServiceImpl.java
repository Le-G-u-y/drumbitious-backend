package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.PlanService;
import com.drumbitious.backend.domain.Plan;
import com.drumbitious.backend.repository.PlanRepository;
import com.drumbitious.backend.service.dto.PlanDTO;
import com.drumbitious.backend.service.mapper.PlanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Plan}.
 */
@Service
@Transactional
public class PlanServiceImpl implements PlanService {

    private final Logger log = LoggerFactory.getLogger(PlanServiceImpl.class);

    private final PlanRepository planRepository;

    private final PlanMapper planMapper;

    public PlanServiceImpl(PlanRepository planRepository, PlanMapper planMapper) {
        this.planRepository = planRepository;
        this.planMapper = planMapper;
    }

    /**
     * Save a plan.
     *
     * @param planDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PlanDTO save(PlanDTO planDTO) {
        log.debug("Request to save Plan : {}", planDTO);
        Plan plan = planMapper.toEntity(planDTO);
        plan = planRepository.save(plan);
        return planMapper.toDto(plan);
    }

    /**
     * Get all the plans.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PlanDTO> findAll() {
        log.debug("Request to get all Plans");
        return planRepository.findAll().stream()
            .map(planMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one plan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PlanDTO> findOne(Long id) {
        log.debug("Request to get Plan : {}", id);
        return planRepository.findById(id)
            .map(planMapper::toDto);
    }

    /**
     * Delete the plan by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Plan : {}", id);
        planRepository.deleteById(id);
    }
}
