package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.service.ExerciseConfigService;
import com.drumbitious.backend.web.rest.errors.BadRequestAlertException;
import com.drumbitious.backend.service.dto.ExerciseConfigDTO;
import com.drumbitious.backend.service.dto.ExerciseConfigCriteria;
import com.drumbitious.backend.service.ExerciseConfigQueryService;

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
 * REST controller for managing {@link com.drumbitious.backend.domain.ExerciseConfig}.
 */
@RestController
@RequestMapping("/api")
public class ExerciseConfigResource {

    private final Logger log = LoggerFactory.getLogger(ExerciseConfigResource.class);

    private static final String ENTITY_NAME = "exerciseConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExerciseConfigService exerciseConfigService;

    private final ExerciseConfigQueryService exerciseConfigQueryService;

    public ExerciseConfigResource(ExerciseConfigService exerciseConfigService, ExerciseConfigQueryService exerciseConfigQueryService) {
        this.exerciseConfigService = exerciseConfigService;
        this.exerciseConfigQueryService = exerciseConfigQueryService;
    }

    /**
     * {@code POST  /exercise-configs} : Create a new exerciseConfig.
     *
     * @param exerciseConfigDTO the exerciseConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exerciseConfigDTO, or with status {@code 400 (Bad Request)} if the exerciseConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exercise-configs")
    public ResponseEntity<ExerciseConfigDTO> createExerciseConfig(@Valid @RequestBody ExerciseConfigDTO exerciseConfigDTO) throws URISyntaxException {
        log.debug("REST request to save ExerciseConfig : {}", exerciseConfigDTO);
        if (exerciseConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new exerciseConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExerciseConfigDTO result = exerciseConfigService.save(exerciseConfigDTO);
        return ResponseEntity.created(new URI("/api/exercise-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exercise-configs} : Updates an existing exerciseConfig.
     *
     * @param exerciseConfigDTO the exerciseConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exerciseConfigDTO,
     * or with status {@code 400 (Bad Request)} if the exerciseConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exerciseConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exercise-configs")
    public ResponseEntity<ExerciseConfigDTO> updateExerciseConfig(@Valid @RequestBody ExerciseConfigDTO exerciseConfigDTO) throws URISyntaxException {
        log.debug("REST request to update ExerciseConfig : {}", exerciseConfigDTO);
        if (exerciseConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExerciseConfigDTO result = exerciseConfigService.save(exerciseConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exerciseConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /exercise-configs} : get all the exerciseConfigs.
     *

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exerciseConfigs in body.
     */
    @GetMapping("/exercise-configs")
    public ResponseEntity<List<ExerciseConfigDTO>> getAllExerciseConfigs(ExerciseConfigCriteria criteria) {
        log.debug("REST request to get ExerciseConfigs by criteria: {}", criteria);
        List<ExerciseConfigDTO> entityList = exerciseConfigQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
    * {@code GET  /exercise-configs/count} : count all the exerciseConfigs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/exercise-configs/count")
    public ResponseEntity<Long> countExerciseConfigs(ExerciseConfigCriteria criteria) {
        log.debug("REST request to count ExerciseConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(exerciseConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exercise-configs/:id} : get the "id" exerciseConfig.
     *
     * @param id the id of the exerciseConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exerciseConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exercise-configs/{id}")
    public ResponseEntity<ExerciseConfigDTO> getExerciseConfig(@PathVariable Long id) {
        log.debug("REST request to get ExerciseConfig : {}", id);
        Optional<ExerciseConfigDTO> exerciseConfigDTO = exerciseConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exerciseConfigDTO);
    }

    /**
     * {@code DELETE  /exercise-configs/:id} : delete the "id" exerciseConfig.
     *
     * @param id the id of the exerciseConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exercise-configs/{id}")
    public ResponseEntity<Void> deleteExerciseConfig(@PathVariable Long id) {
        log.debug("REST request to delete ExerciseConfig : {}", id);
        exerciseConfigService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
