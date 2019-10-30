package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.ExcerciseTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.ExcerciseType}.
 */
public interface ExcerciseTypeService {

    /**
     * Save a excerciseType.
     *
     * @param excerciseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    ExcerciseTypeDTO save(ExcerciseTypeDTO excerciseTypeDTO);

    /**
     * Get all the excerciseTypes.
     *
     * @return the list of entities.
     */
    List<ExcerciseTypeDTO> findAll();


    /**
     * Get the "id" excerciseType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExcerciseTypeDTO> findOne(Long id);

    /**
     * Delete the "id" excerciseType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
