package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.ExcerciseTypeService;
import com.drumbitious.backend.domain.ExcerciseType;
import com.drumbitious.backend.repository.ExcerciseTypeRepository;
import com.drumbitious.backend.service.dto.ExcerciseTypeDTO;
import com.drumbitious.backend.service.mapper.ExcerciseTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ExcerciseType}.
 */
@Service
@Transactional
public class ExcerciseTypeServiceImpl implements ExcerciseTypeService {

    private final Logger log = LoggerFactory.getLogger(ExcerciseTypeServiceImpl.class);

    private final ExcerciseTypeRepository excerciseTypeRepository;

    private final ExcerciseTypeMapper excerciseTypeMapper;

    public ExcerciseTypeServiceImpl(ExcerciseTypeRepository excerciseTypeRepository, ExcerciseTypeMapper excerciseTypeMapper) {
        this.excerciseTypeRepository = excerciseTypeRepository;
        this.excerciseTypeMapper = excerciseTypeMapper;
    }

    /**
     * Save a excerciseType.
     *
     * @param excerciseTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ExcerciseTypeDTO save(ExcerciseTypeDTO excerciseTypeDTO) {
        log.debug("Request to save ExcerciseType : {}", excerciseTypeDTO);
        ExcerciseType excerciseType = excerciseTypeMapper.toEntity(excerciseTypeDTO);
        excerciseType = excerciseTypeRepository.save(excerciseType);
        return excerciseTypeMapper.toDto(excerciseType);
    }

    /**
     * Get all the excerciseTypes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExcerciseTypeDTO> findAll() {
        log.debug("Request to get all ExcerciseTypes");
        return excerciseTypeRepository.findAll().stream()
            .map(excerciseTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one excerciseType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExcerciseTypeDTO> findOne(Long id) {
        log.debug("Request to get ExcerciseType : {}", id);
        return excerciseTypeRepository.findById(id)
            .map(excerciseTypeMapper::toDto);
    }

    /**
     * Delete the excerciseType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExcerciseType : {}", id);
        excerciseTypeRepository.deleteById(id);
    }
}
