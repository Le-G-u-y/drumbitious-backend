package com.drumbitious.backend.service.impl;

import com.drumbitious.backend.service.FinishedSessionService;
import com.drumbitious.backend.domain.FinishedSession;
import com.drumbitious.backend.repository.FinishedSessionRepository;
import com.drumbitious.backend.service.dto.FinishedSessionDTO;
import com.drumbitious.backend.service.mapper.FinishedSessionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link FinishedSession}.
 */
@Service
@Transactional
public class FinishedSessionServiceImpl implements FinishedSessionService {

    private final Logger log = LoggerFactory.getLogger(FinishedSessionServiceImpl.class);

    private final FinishedSessionRepository finishedSessionRepository;

    private final FinishedSessionMapper finishedSessionMapper;

    public FinishedSessionServiceImpl(FinishedSessionRepository finishedSessionRepository, FinishedSessionMapper finishedSessionMapper) {
        this.finishedSessionRepository = finishedSessionRepository;
        this.finishedSessionMapper = finishedSessionMapper;
    }

    /**
     * Save a finishedSession.
     *
     * @param finishedSessionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public FinishedSessionDTO save(FinishedSessionDTO finishedSessionDTO) {
        log.debug("Request to save FinishedSession : {}", finishedSessionDTO);
        FinishedSession finishedSession = finishedSessionMapper.toEntity(finishedSessionDTO);
        finishedSession = finishedSessionRepository.save(finishedSession);
        return finishedSessionMapper.toDto(finishedSession);
    }

    /**
     * Get all the finishedSessions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<FinishedSessionDTO> findAll() {
        log.debug("Request to get all FinishedSessions");
        return finishedSessionRepository.findAll().stream()
            .map(finishedSessionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one finishedSession by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<FinishedSessionDTO> findOne(Long id) {
        log.debug("Request to get FinishedSession : {}", id);
        return finishedSessionRepository.findById(id)
            .map(finishedSessionMapper::toDto);
    }

    /**
     * Delete the finishedSession by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete FinishedSession : {}", id);
        finishedSessionRepository.deleteById(id);
    }
}
