package com.drumbitious.backend.web.rest;

import com.drumbitious.backend.service.SkillTypeService;
import com.drumbitious.backend.web.rest.errors.BadRequestAlertException;
import com.drumbitious.backend.service.dto.SkillTypeDTO;

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
 * REST controller for managing {@link com.drumbitious.backend.domain.SkillType}.
 */
@RestController
@RequestMapping("/api")
public class SkillTypeResource {

    private final Logger log = LoggerFactory.getLogger(SkillTypeResource.class);

    private static final String ENTITY_NAME = "skillType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SkillTypeService skillTypeService;

    public SkillTypeResource(SkillTypeService skillTypeService) {
        this.skillTypeService = skillTypeService;
    }

    /**
     * {@code POST  /skill-types} : Create a new skillType.
     *
     * @param skillTypeDTO the skillTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new skillTypeDTO, or with status {@code 400 (Bad Request)} if the skillType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/skill-types")
    public ResponseEntity<SkillTypeDTO> createSkillType(@Valid @RequestBody SkillTypeDTO skillTypeDTO) throws URISyntaxException {
        log.debug("REST request to save SkillType : {}", skillTypeDTO);
        if (skillTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new skillType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SkillTypeDTO result = skillTypeService.save(skillTypeDTO);
        return ResponseEntity.created(new URI("/api/skill-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /skill-types} : Updates an existing skillType.
     *
     * @param skillTypeDTO the skillTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated skillTypeDTO,
     * or with status {@code 400 (Bad Request)} if the skillTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the skillTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/skill-types")
    public ResponseEntity<SkillTypeDTO> updateSkillType(@Valid @RequestBody SkillTypeDTO skillTypeDTO) throws URISyntaxException {
        log.debug("REST request to update SkillType : {}", skillTypeDTO);
        if (skillTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SkillTypeDTO result = skillTypeService.save(skillTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, skillTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /skill-types} : get all the skillTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of skillTypes in body.
     */
    @GetMapping("/skill-types")
    public List<SkillTypeDTO> getAllSkillTypes() {
        log.debug("REST request to get all SkillTypes");
        return skillTypeService.findAll();
    }

    /**
     * {@code GET  /skill-types/:id} : get the "id" skillType.
     *
     * @param id the id of the skillTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the skillTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/skill-types/{id}")
    public ResponseEntity<SkillTypeDTO> getSkillType(@PathVariable Long id) {
        log.debug("REST request to get SkillType : {}", id);
        Optional<SkillTypeDTO> skillTypeDTO = skillTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(skillTypeDTO);
    }

    /**
     * {@code DELETE  /skill-types/:id} : delete the "id" skillType.
     *
     * @param id the id of the skillTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/skill-types/{id}")
    public ResponseEntity<Void> deleteSkillType(@PathVariable Long id) {
        log.debug("REST request to delete SkillType : {}", id);
        skillTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
