package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.service.ExcerciseTypeService;
import com.drumbitious.backend.web.rest.errors.BadRequestAlertException;
import com.drumbitious.backend.service.dto.ExcerciseTypeDTO;

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
 * REST controller for managing {@link com.drumbitious.backend.domain.ExcerciseType}.
 */
@RestController
@RequestMapping("/api")
public class ExcerciseTypeResource {

    private final Logger log = LoggerFactory.getLogger(ExcerciseTypeResource.class);

    private static final String ENTITY_NAME = "excerciseType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExcerciseTypeService excerciseTypeService;

    public ExcerciseTypeResource(ExcerciseTypeService excerciseTypeService) {
        this.excerciseTypeService = excerciseTypeService;
    }

    /**
     * {@code POST  /excercise-types} : Create a new excerciseType.
     *
     * @param excerciseTypeDTO the excerciseTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new excerciseTypeDTO, or with status {@code 400 (Bad Request)} if the excerciseType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/excercise-types")
    public ResponseEntity<ExcerciseTypeDTO> createExcerciseType(@Valid @RequestBody ExcerciseTypeDTO excerciseTypeDTO) throws URISyntaxException {
        log.debug("REST request to save ExcerciseType : {}", excerciseTypeDTO);
        if (excerciseTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new excerciseType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExcerciseTypeDTO result = excerciseTypeService.save(excerciseTypeDTO);
        return ResponseEntity.created(new URI("/api/excercise-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /excercise-types} : Updates an existing excerciseType.
     *
     * @param excerciseTypeDTO the excerciseTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excerciseTypeDTO,
     * or with status {@code 400 (Bad Request)} if the excerciseTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the excerciseTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/excercise-types")
    public ResponseEntity<ExcerciseTypeDTO> updateExcerciseType(@Valid @RequestBody ExcerciseTypeDTO excerciseTypeDTO) throws URISyntaxException {
        log.debug("REST request to update ExcerciseType : {}", excerciseTypeDTO);
        if (excerciseTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExcerciseTypeDTO result = excerciseTypeService.save(excerciseTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, excerciseTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /excercise-types} : get all the excerciseTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of excerciseTypes in body.
     */
    @GetMapping("/excercise-types")
    public List<ExcerciseTypeDTO> getAllExcerciseTypes() {
        log.debug("REST request to get all ExcerciseTypes");
        return excerciseTypeService.findAll();
    }

    /**
     * {@code GET  /excercise-types/:id} : get the "id" excerciseType.
     *
     * @param id the id of the excerciseTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the excerciseTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/excercise-types/{id}")
    public ResponseEntity<ExcerciseTypeDTO> getExcerciseType(@PathVariable Long id) {
        log.debug("REST request to get ExcerciseType : {}", id);
        Optional<ExcerciseTypeDTO> excerciseTypeDTO = excerciseTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(excerciseTypeDTO);
    }

    /**
     * {@code DELETE  /excercise-types/:id} : delete the "id" excerciseType.
     *
     * @param id the id of the excerciseTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/excercise-types/{id}")
    public ResponseEntity<Void> deleteExcerciseType(@PathVariable Long id) {
        log.debug("REST request to delete ExcerciseType : {}", id);
        excerciseTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
