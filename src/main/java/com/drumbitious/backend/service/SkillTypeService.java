package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.SkillTypeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.SkillType}.
 */
public interface SkillTypeService {

    /**
     * Save a skillType.
     *
     * @param skillTypeDTO the entity to save.
     * @return the persisted entity.
     */
    SkillTypeDTO save(SkillTypeDTO skillTypeDTO);

    /**
     * Get all the skillTypes.
     *
     * @return the list of entities.
     */
    List<SkillTypeDTO> findAll();


    /**
     * Get the "id" skillType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SkillTypeDTO> findOne(Long id);

    /**
     * Delete the "id" skillType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
