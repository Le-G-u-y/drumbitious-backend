package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.service.ExcerciseService;
import com.drumbitious.backend.web.rest.errors.BadRequestAlertException;
import com.drumbitious.backend.service.dto.ExcerciseDTO;
import com.drumbitious.backend.service.dto.ExcerciseCriteria;
import com.drumbitious.backend.service.ExcerciseQueryService;

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
 * REST controller for managing {@link com.drumbitious.backend.domain.Excercise}.
 */
@RestController
@RequestMapping("/api")
public class ExcerciseResource {

    private final Logger log = LoggerFactory.getLogger(ExcerciseResource.class);

    private static final String ENTITY_NAME = "excercise";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExcerciseService excerciseService;

    private final ExcerciseQueryService excerciseQueryService;

    public ExcerciseResource(ExcerciseService excerciseService, ExcerciseQueryService excerciseQueryService) {
        this.excerciseService = excerciseService;
        this.excerciseQueryService = excerciseQueryService;
    }

    /**
     * {@code POST  /excercises} : Create a new excercise.
     *
     * @param excerciseDTO the excerciseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new excerciseDTO, or with status {@code 400 (Bad Request)} if the excercise has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/excercises")
    public ResponseEntity<ExcerciseDTO> createExcercise(@Valid @RequestBody ExcerciseDTO excerciseDTO) throws URISyntaxException {
        log.debug("REST request to save Excercise : {}", excerciseDTO);
        if (excerciseDTO.getId() != null) {
            throw new BadRequestAlertException("A new excercise cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExcerciseDTO result = excerciseService.save(excerciseDTO);
        return ResponseEntity.created(new URI("/api/excercises/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /excercises} : Updates an existing excercise.
     *
     * @param excerciseDTO the excerciseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excerciseDTO,
     * or with status {@code 400 (Bad Request)} if the excerciseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the excerciseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/excercises")
    public ResponseEntity<ExcerciseDTO> updateExcercise(@Valid @RequestBody ExcerciseDTO excerciseDTO) throws URISyntaxException {
        log.debug("REST request to update Excercise : {}", excerciseDTO);
        if (excerciseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExcerciseDTO result = excerciseService.save(excerciseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, excerciseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /excercises} : get all the excercises.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of excercises in body.
     */
    @GetMapping("/excercises")
    public ResponseEntity<List<ExcerciseDTO>> getAllExcercises(ExcerciseCriteria criteria) {
        log.debug("REST request to get Excercises by criteria: {}", criteria);
        List<ExcerciseDTO> entityList = excerciseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /excercises/count} : count all the excercises.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/excercises/count")
    public ResponseEntity<Long> countExcercises(ExcerciseCriteria criteria) {
        log.debug("REST request to count Excercises by criteria: {}", criteria);
        return ResponseEntity.ok().body(excerciseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /excercises/:id} : get the "id" excercise.
     *
     * @param id the id of the excerciseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the excerciseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/excercises/{id}")
    public ResponseEntity<ExcerciseDTO> getExcercise(@PathVariable Long id) {
        log.debug("REST request to get Excercise : {}", id);
        Optional<ExcerciseDTO> excerciseDTO = excerciseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(excerciseDTO);
    }

    /**
     * {@code DELETE  /excercises/:id} : delete the "id" excercise.
     *
     * @param id the id of the excerciseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/excercises/{id}")
    public ResponseEntity<Void> deleteExcercise(@PathVariable Long id) {
        log.debug("REST request to delete Excercise : {}", id);
        excerciseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
