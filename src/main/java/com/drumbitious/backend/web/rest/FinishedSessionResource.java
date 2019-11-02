package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.service.FinishedSessionService;
import com.drumbitious.backend.web.rest.errors.BadRequestAlertException;
import com.drumbitious.backend.service.dto.FinishedSessionDTO;
import com.drumbitious.backend.service.dto.FinishedSessionCriteria;
import com.drumbitious.backend.service.FinishedSessionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.drumbitious.backend.domain.FinishedSession}.
 */
@RestController
@RequestMapping("/api")
public class FinishedSessionResource {

    private final Logger log = LoggerFactory.getLogger(FinishedSessionResource.class);

    private static final String ENTITY_NAME = "finishedSession";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinishedSessionService finishedSessionService;

    private final FinishedSessionQueryService finishedSessionQueryService;

    public FinishedSessionResource(FinishedSessionService finishedSessionService, FinishedSessionQueryService finishedSessionQueryService) {
        this.finishedSessionService = finishedSessionService;
        this.finishedSessionQueryService = finishedSessionQueryService;
    }

    /**
     * {@code POST  /finished-sessions} : Create a new finishedSession.
     *
     * @param finishedSessionDTO the finishedSessionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finishedSessionDTO, or with status {@code 400 (Bad Request)} if the finishedSession has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/finished-sessions")
    public ResponseEntity<FinishedSessionDTO> createFinishedSession(@Valid @RequestBody FinishedSessionDTO finishedSessionDTO) throws URISyntaxException {
        log.debug("REST request to save FinishedSession : {}", finishedSessionDTO);
        if (finishedSessionDTO.getId() != null) {
            throw new BadRequestAlertException("A new finishedSession cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinishedSessionDTO result = finishedSessionService.save(finishedSessionDTO);
        return ResponseEntity.created(new URI("/api/finished-sessions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /finished-sessions} : Updates an existing finishedSession.
     *
     * @param finishedSessionDTO the finishedSessionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finishedSessionDTO,
     * or with status {@code 400 (Bad Request)} if the finishedSessionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finishedSessionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/finished-sessions")
    public ResponseEntity<FinishedSessionDTO> updateFinishedSession(@Valid @RequestBody FinishedSessionDTO finishedSessionDTO) throws URISyntaxException {
        log.debug("REST request to update FinishedSession : {}", finishedSessionDTO);
        if (finishedSessionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FinishedSessionDTO result = finishedSessionService.save(finishedSessionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finishedSessionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /finished-sessions} : get all the finishedSessions.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of finishedSessions in body.
     */
    @GetMapping("/finished-sessions")
    public ResponseEntity<List<FinishedSessionDTO>> getAllFinishedSessions(FinishedSessionCriteria criteria) {
        log.debug("REST request to get FinishedSessions by criteria: {}", criteria);
        List<FinishedSessionDTO> entityList = finishedSessionQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /finished-sessions/count} : count all the finishedSessions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/finished-sessions/count")
    public ResponseEntity<Long> countFinishedSessions(FinishedSessionCriteria criteria) {
        log.debug("REST request to count FinishedSessions by criteria: {}", criteria);
        return ResponseEntity.ok().body(finishedSessionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /finished-sessions/:id} : get the "id" finishedSession.
     *
     * @param id the id of the finishedSessionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finishedSessionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/finished-sessions/{id}")
    public ResponseEntity<FinishedSessionDTO> getFinishedSession(@PathVariable Long id) {
        log.debug("REST request to get FinishedSession : {}", id);
        Optional<FinishedSessionDTO> finishedSessionDTO = finishedSessionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(finishedSessionDTO);
    }

    /**
     * {@code DELETE  /finished-sessions/:id} : delete the "id" finishedSession.
     *
     * @param id the id of the finishedSessionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/finished-sessions/{id}")
    public ResponseEntity<Void> deleteFinishedSession(@PathVariable Long id) {
        log.debug("REST request to delete FinishedSession : {}", id);
        finishedSessionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
