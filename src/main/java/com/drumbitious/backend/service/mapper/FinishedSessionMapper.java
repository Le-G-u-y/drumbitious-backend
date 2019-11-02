package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.FinishedSessionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinishedSession} and its DTO {@link FinishedSessionDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlanMapper.class})
public interface FinishedSessionMapper extends EntityMapper<FinishedSessionDTO, FinishedSession> {

    @Mapping(source = "plan.id", target = "planId")
    @Mapping(source = "plan.planName", target = "planPlanName")
    FinishedSessionDTO toDto(FinishedSession finishedSession);

    @Mapping(source = "planId", target = "plan")
    @Mapping(target = "exercises", ignore = true)
    @Mapping(target = "removeExercise", ignore = true)
    FinishedSession toEntity(FinishedSessionDTO finishedSessionDTO);

    default FinishedSession fromId(Long id) {
        if (id == null) {
            return null;
        }
        FinishedSession finishedSession = new FinishedSession();
        finishedSession.setId(id);
        return finishedSession;
    }
}
