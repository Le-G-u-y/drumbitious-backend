package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.FinishedExerciseDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.FinishedExercise}.
 */
public interface FinishedExerciseService {

    /**
     * Save a finishedExercise.
     *
     * @param finishedExerciseDTO the entity to save.
     * @return the persisted entity.
     */
    FinishedExerciseDTO save(FinishedExerciseDTO finishedExerciseDTO);

    /**
     * Get all the finishedExercises.
     *
     * @return the list of entities.
     */
    List<FinishedExerciseDTO> findAll();


    /**
     * Get the "id" finishedExercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinishedExerciseDTO> findOne(Long id);

    /**
     * Delete the "id" finishedExercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
