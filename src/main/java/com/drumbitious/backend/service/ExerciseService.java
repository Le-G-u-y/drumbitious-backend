package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.ExerciseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.Exercise}.
 */
public interface ExerciseService {

    /**
     * Save a exercise.
     *
     * @param exerciseDTO the entity to save.
     * @return the persisted entity.
     */
    ExerciseDTO save(ExerciseDTO exerciseDTO);

    /**
     * Get all the exercises.
     *
     * @return the list of entities.
     */
    List<ExerciseDTO> findAll();


    /**
     * Get the "id" exercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseDTO> findOne(Long id);

    /**
     * Delete the "id" exercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
