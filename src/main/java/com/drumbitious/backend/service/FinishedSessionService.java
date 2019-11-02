package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.FinishedSessionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.FinishedSession}.
 */
public interface FinishedSessionService {

    /**
     * Save a finishedSession.
     *
     * @param finishedSessionDTO the entity to save.
     * @return the persisted entity.
     */
    FinishedSessionDTO save(FinishedSessionDTO finishedSessionDTO);

    /**
     * Get all the finishedSessions.
     *
     * @return the list of entities.
     */
    List<FinishedSessionDTO> findAll();


    /**
     * Get the "id" finishedSession.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinishedSessionDTO> findOne(Long id);

    /**
     * Delete the "id" finishedSession.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
