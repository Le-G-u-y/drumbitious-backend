package com.drumbitious.backend.service;

import com.drumbitious.backend.service.dto.DrummerStatisticsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.drumbitious.backend.domain.DrummerStatistics}.
 */
public interface DrummerStatisticsService {

    /**
     * Save a drummerStatistics.
     *
     * @param drummerStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    DrummerStatisticsDTO save(DrummerStatisticsDTO drummerStatisticsDTO);

    /**
     * Get all the drummerStatistics.
     *
     * @return the list of entities.
     */
    List<DrummerStatisticsDTO> findAll();


    /**
     * Get the "id" drummerStatistics.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DrummerStatisticsDTO> findOne(Long id);

    /**
     * Delete the "id" drummerStatistics.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
