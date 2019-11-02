package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.FinishedExerciseService;
import com.drumbitious.backend.domain.FinishedExercise;
import com.drumbitious.backend.repository.FinishedExerciseRepository;
import com.drumbitious.backend.service.dto.FinishedExerciseDTO;
import com.drumbitious.backend.service.mapper.FinishedExerciseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link FinishedExercise}.
 */
@Service
@Transactional
public class FinishedExerciseServiceImpl implements FinishedExerciseService {

    private final Logger log = LoggerFactory.getLogger(FinishedExerciseServiceImpl.class);

    private final FinishedExerciseRepository finishedExerciseRepository;

    private final FinishedExerciseMapper finishedExerciseMapper;

    public FinishedExerciseServiceImpl(FinishedExerciseRepository finishedExerciseRepository, FinishedExerciseMapper finishedExerciseMapper) {
        this.finishedExerciseRepository = finishedExerciseRepository;
        this.finishedExerciseMapper = finishedExerciseMapper;
    }

    /**
     * Save a finishedExercise.
     *
     * @param finishedExerciseDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FinishedExerciseDTO save(FinishedExerciseDTO finishedExerciseDTO) {
        log.debug("Request to save FinishedExercise : {}", finishedExerciseDTO);
        FinishedExercise finishedExercise = finishedExerciseMapper.toEntity(finishedExerciseDTO);
        finishedExercise = finishedExerciseRepository.save(finishedExercise);
        return finishedExerciseMapper.toDto(finishedExercise);
    }

    /**
     * Get all the finishedExercises.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FinishedExerciseDTO> findAll() {
        log.debug("Request to get all FinishedExercises");
        return finishedExerciseRepository.findAll().stream()
            .map(finishedExerciseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one finishedExercise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FinishedExerciseDTO> findOne(Long id) {
        log.debug("Request to get FinishedExercise : {}", id);
        return finishedExerciseRepository.findById(id)
            .map(finishedExerciseMapper::toDto);
    }

    /**
     * Delete the finishedExercise by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FinishedExercise : {}", id);
        finishedExerciseRepository.deleteById(id);
    }
}
