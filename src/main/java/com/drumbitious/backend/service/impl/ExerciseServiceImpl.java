package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.ExerciseService;
import com.drumbitious.backend.domain.Exercise;
import com.drumbitious.backend.repository.ExerciseRepository;
import com.drumbitious.backend.service.dto.ExerciseDTO;
import com.drumbitious.backend.service.mapper.ExerciseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Exercise}.
 */
@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

    private final Logger log = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository exerciseRepository;

    private final ExerciseMapper exerciseMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    /**
     * Save a exercise.
     *
     * @param exerciseDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ExerciseDTO save(ExerciseDTO exerciseDTO) {
        log.debug("Request to save Exercise : {}", exerciseDTO);
        Exercise exercise = exerciseMapper.toEntity(exerciseDTO);
        exercise = exerciseRepository.save(exercise);
        return exerciseMapper.toDto(exercise);
    }

    /**
     * Get all the exercises.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExerciseDTO> findAll() {
        log.debug("Request to get all Exercises");
        return exerciseRepository.findAll().stream()
            .map(exerciseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one exercise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseDTO> findOne(Long id) {
        log.debug("Request to get Exercise : {}", id);
        return exerciseRepository.findById(id)
            .map(exerciseMapper::toDto);
    }

    /**
     * Delete the exercise by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exercise : {}", id);
        exerciseRepository.deleteById(id);
    }
}
