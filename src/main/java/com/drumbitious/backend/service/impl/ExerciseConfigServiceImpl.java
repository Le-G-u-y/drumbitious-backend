package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.ExerciseConfigService;
import com.drumbitious.backend.domain.ExerciseConfig;
import com.drumbitious.backend.repository.ExerciseConfigRepository;
import com.drumbitious.backend.service.dto.ExerciseConfigDTO;
import com.drumbitious.backend.service.mapper.ExerciseConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ExerciseConfig}.
 */
@Service
@Transactional
public class ExerciseConfigServiceImpl implements ExerciseConfigService {

    private final Logger log = LoggerFactory.getLogger(ExerciseConfigServiceImpl.class);

    private final ExerciseConfigRepository exerciseConfigRepository;

    private final ExerciseConfigMapper exerciseConfigMapper;

    public ExerciseConfigServiceImpl(ExerciseConfigRepository exerciseConfigRepository, ExerciseConfigMapper exerciseConfigMapper) {
        this.exerciseConfigRepository = exerciseConfigRepository;
        this.exerciseConfigMapper = exerciseConfigMapper;
    }

    /**
     * Save a exerciseConfig.
     *
     * @param exerciseConfigDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ExerciseConfigDTO save(ExerciseConfigDTO exerciseConfigDTO) {
        log.debug("Request to save ExerciseConfig : {}", exerciseConfigDTO);
        ExerciseConfig exerciseConfig = exerciseConfigMapper.toEntity(exerciseConfigDTO);
        exerciseConfig = exerciseConfigRepository.save(exerciseConfig);
        return exerciseConfigMapper.toDto(exerciseConfig);
    }

    /**
     * Get all the exerciseConfigs.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExerciseConfigDTO> findAll() {
        log.debug("Request to get all ExerciseConfigs");
        return exerciseConfigRepository.findAll().stream()
            .map(exerciseConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one exerciseConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExerciseConfigDTO> findOne(Long id) {
        log.debug("Request to get ExerciseConfig : {}", id);
        return exerciseConfigRepository.findById(id)
            .map(exerciseConfigMapper::toDto);
    }

    /**
     * Delete the exerciseConfig by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExerciseConfig : {}", id);
        exerciseConfigRepository.deleteById(id);
    }
}
