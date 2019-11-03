package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.service.FinishedExerciseService;
import com.drumbitious.backend.web.rest.errors.BadRequestAlertException;
import com.drumbitious.backend.service.dto.FinishedExerciseDTO;
import com.drumbitious.backend.service.dto.FinishedExerciseCriteria;
import com.drumbitious.backend.service.FinishedExerciseQueryService;

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
 * REST controller for managing {@link com.drumbitious.backend.domain.FinishedExercise}.
 */
@RestController
@RequestMapping("/api")
public class FinishedExerciseResource {

    private final Logger log = LoggerFactory.getLogger(FinishedExerciseResource.class);

    private static final String ENTITY_NAME = "finishedExercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinishedExerciseService finishedExerciseService;

    private final FinishedExerciseQueryService finishedExerciseQueryService;

    public FinishedExerciseResource(FinishedExerciseService finishedExerciseService, FinishedExerciseQueryService finishedExerciseQueryService) {
        this.finishedExerciseService = finishedExerciseService;
        this.finishedExerciseQueryService = finishedExerciseQueryService;
    }

    /**
     * {@code POST  /finished-exercises} : Create a new finishedExercise.
     *
     * @param finishedExerciseDTO the finishedExerciseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new finishedExerciseDTO, or with status {@code 400 (Bad Request)} if the finishedExercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/finished-exercises")
    public ResponseEntity<FinishedExerciseDTO> createFinishedExercise(@Valid @RequestBody FinishedExerciseDTO finishedExerciseDTO) throws URISyntaxException {
        log.debug("REST request to save FinishedExercise : {}", finishedExerciseDTO);
        if (finishedExerciseDTO.getId() != null) {
            throw new BadRequestAlertException("A new finishedExercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinishedExerciseDTO result = finishedExerciseService.save(finishedExerciseDTO);
        return ResponseEntity.created(new URI("/api/finished-exercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /finished-exercises} : Updates an existing finishedExercise.
     *
     * @param finishedExerciseDTO the finishedExerciseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated finishedExerciseDTO,
     * or with status {@code 400 (Bad Request)} if the finishedExerciseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the finishedExerciseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/finished-exercises")
    public ResponseEntity<FinishedExerciseDTO> updateFinishedExercise(@Valid @RequestBody FinishedExerciseDTO finishedExerciseDTO) throws URISyntaxException {
        log.debug("REST request to update FinishedExercise : {}", finishedExerciseDTO);
        if (finishedExerciseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FinishedExerciseDTO result = finishedExerciseService.save(finishedExerciseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, finishedExerciseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /finished-exercises} : get all the finishedExercises.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of finishedExercises in body.
     */
    @GetMapping("/finished-exercises")
    public ResponseEntity<List<FinishedExerciseDTO>> getAllFinishedExercises(FinishedExerciseCriteria criteria) {
        log.debug("REST request to get FinishedExercises by criteria: {}", criteria);
        List<FinishedExerciseDTO> entityList = finishedExerciseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /finished-exercises/count} : count all the finishedExercises.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/finished-exercises/count")
    public ResponseEntity<Long> countFinishedExercises(FinishedExerciseCriteria criteria) {
        log.debug("REST request to count FinishedExercises by criteria: {}", criteria);
        return ResponseEntity.ok().body(finishedExerciseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /finished-exercises/:id} : get the "id" finishedExercise.
     *
     * @param id the id of the finishedExerciseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the finishedExerciseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/finished-exercises/{id}")
    public ResponseEntity<FinishedExerciseDTO> getFinishedExercise(@PathVariable Long id) {
        log.debug("REST request to get FinishedExercise : {}", id);
        Optional<FinishedExerciseDTO> finishedExerciseDTO = finishedExerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(finishedExerciseDTO);
    }

    /**
     * {@code DELETE  /finished-exercises/:id} : delete the "id" finishedExercise.
     *
     * @param id the id of the finishedExerciseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/finished-exercises/{id}")
    public ResponseEntity<Void> deleteFinishedExercise(@PathVariable Long id) {
        log.debug("REST request to delete FinishedExercise : {}", id);
        finishedExerciseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
