package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.SkillTypeService;
import com.drumbitious.backend.domain.SkillType;
import com.drumbitious.backend.repository.SkillTypeRepository;
import com.drumbitious.backend.service.dto.SkillTypeDTO;
import com.drumbitious.backend.service.mapper.SkillTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SkillType}.
 */
@Service
@Transactional
public class SkillTypeServiceImpl implements SkillTypeService {

    private final Logger log = LoggerFactory.getLogger(SkillTypeServiceImpl.class);

    private final SkillTypeRepository skillTypeRepository;

    private final SkillTypeMapper skillTypeMapper;

    public SkillTypeServiceImpl(SkillTypeRepository skillTypeRepository, SkillTypeMapper skillTypeMapper) {
        this.skillTypeRepository = skillTypeRepository;
        this.skillTypeMapper = skillTypeMapper;
    }

    /**
     * Save a skillType.
     *
     * @param skillTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SkillTypeDTO save(SkillTypeDTO skillTypeDTO) {
        log.debug("Request to save SkillType : {}", skillTypeDTO);
        SkillType skillType = skillTypeMapper.toEntity(skillTypeDTO);
        skillType = skillTypeRepository.save(skillType);
        return skillTypeMapper.toDto(skillType);
    }

    /**
     * Get all the skillTypes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SkillTypeDTO> findAll() {
        log.debug("Request to get all SkillTypes");
        return skillTypeRepository.findAll().stream()
            .map(skillTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one skillType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SkillTypeDTO> findOne(Long id) {
        log.debug("Request to get SkillType : {}", id);
        return skillTypeRepository.findById(id)
            .map(skillTypeMapper::toDto);
    }

    /**
     * Delete the skillType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SkillType : {}", id);
        skillTypeRepository.deleteById(id);
    }
}
