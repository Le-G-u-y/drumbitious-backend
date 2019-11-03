package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.ExerciseConfigDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.ExerciseConfig}.
 */
public interface ExerciseConfigService {

    /**
     * Save a exerciseConfig.
     *
     * @param exerciseConfigDTO the entity to save.
     * @return the persisted entity.
     */
    ExerciseConfigDTO save(ExerciseConfigDTO exerciseConfigDTO);

    /**
     * Get all the exerciseConfigs.
     *
     * @return the list of entities.
     */
    List<ExerciseConfigDTO> findAll();


    /**
     * Get the "id" exerciseConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExerciseConfigDTO> findOne(Long id);

    /**
     * Delete the "id" exerciseConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
