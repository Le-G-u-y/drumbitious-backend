package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.ExcerciseService;
import com.drumbitious.backend.domain.Excercise;
import com.drumbitious.backend.repository.ExcerciseRepository;
import com.drumbitious.backend.service.dto.ExcerciseDTO;
import com.drumbitious.backend.service.mapper.ExcerciseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Excercise}.
 */
@Service
@Transactional
public class ExcerciseServiceImpl implements ExcerciseService {

    private final Logger log = LoggerFactory.getLogger(ExcerciseServiceImpl.class);

    private final ExcerciseRepository excerciseRepository;

    private final ExcerciseMapper excerciseMapper;

    public ExcerciseServiceImpl(ExcerciseRepository excerciseRepository, ExcerciseMapper excerciseMapper) {
        this.excerciseRepository = excerciseRepository;
        this.excerciseMapper = excerciseMapper;
    }

    /**
     * Save a excercise.
     *
     * @param excerciseDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ExcerciseDTO save(ExcerciseDTO excerciseDTO) {
        log.debug("Request to save Excercise : {}", excerciseDTO);
        Excercise excercise = excerciseMapper.toEntity(excerciseDTO);
        excercise = excerciseRepository.save(excercise);
        return excerciseMapper.toDto(excercise);
    }

    /**
     * Get all the excercises.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ExcerciseDTO> findAll() {
        log.debug("Request to get all Excercises");
        return excerciseRepository.findAllWithEagerRelationships().stream()
            .map(excerciseMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the excercises with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ExcerciseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return excerciseRepository.findAllWithEagerRelationships(pageable).map(excerciseMapper::toDto);
    }
    

    /**
     * Get one excercise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ExcerciseDTO> findOne(Long id) {
        log.debug("Request to get Excercise : {}", id);
        return excerciseRepository.findOneWithEagerRelationships(id)
            .map(excerciseMapper::toDto);
    }

    /**
     * Delete the excercise by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Excercise : {}", id);
        excerciseRepository.deleteById(id);
    }
}
