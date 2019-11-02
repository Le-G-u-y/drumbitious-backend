package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.PlanDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plan} and its DTO {@link PlanDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ExerciseConfigMapper.class})
public interface PlanMapper extends EntityMapper<PlanDTO, Plan> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    @Mapping(source = "exerciseConfig.id", target = "exerciseConfigId")
    PlanDTO toDto(Plan plan);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "creatorId", target = "creator")
    @Mapping(source = "exerciseConfigId", target = "exerciseConfig")
    Plan toEntity(PlanDTO planDTO);

    default Plan fromId(Long id) {
        if (id == null) {
            return null;
        }
        Plan plan = new Plan();
        plan.setId(id);
        return plan;
    }
}
