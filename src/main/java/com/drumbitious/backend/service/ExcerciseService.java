package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.ExcerciseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.Excercise}.
 */
public interface ExcerciseService {

    /**
     * Save a excercise.
     *
     * @param excerciseDTO the entity to save.
     * @return the persisted entity.
     */
    ExcerciseDTO save(ExcerciseDTO excerciseDTO);

    /**
     * Get all the excercises.
     *
     * @return the list of entities.
     */
    List<ExcerciseDTO> findAll();

    /**
     * Get all the excercises with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<ExcerciseDTO> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" excercise.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ExcerciseDTO> findOne(Long id);

    /**
     * Delete the "id" excercise.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
