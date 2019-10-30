package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.ExcerciseConfigDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.ExcerciseConfig}.
 */
public interface ExcerciseConfigService {

    /**
     * Save a excerciseConfig.
     *
     * @param excerciseConfigDTO the entity to save.
     * @return the persisted entity.
     */
    ExcerciseConfigDTO save(ExcerciseConfigDTO excerciseConfigDTO);

    /**
     * Get all the excerciseConfigs.
     *
     * @return the list of entities.
     */
    List<ExcerciseConfigDTO> findAll();


    /**
     * Get the "id" excerciseConfig.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExcerciseConfigDTO> findOne(Long id);

    /**
     * Delete the "id" excerciseConfig.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
