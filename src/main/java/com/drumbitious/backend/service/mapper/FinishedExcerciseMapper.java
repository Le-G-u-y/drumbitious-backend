package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.FinishedExcerciseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinishedExcercise} and its DTO {@link FinishedExcerciseDTO}.
 */
@Mapper(componentModel = "spring", uses = {FinishedSessionMapper.class, ExcerciseMapper.class})
public interface FinishedExcerciseMapper extends EntityMapper<FinishedExcerciseDTO, FinishedExcercise> {

    @Mapping(source = "finishedSession.id", target = "finishedSessionId")
    @Mapping(source = "excercise.id", target = "excerciseId")
    @Mapping(source = "excercise.excerciseName", target = "excerciseExcerciseName")
    FinishedExcerciseDTO toDto(FinishedExcercise finishedExcercise);

    @Mapping(source = "finishedSessionId", target = "finishedSession")
    @Mapping(source = "excerciseId", target = "excercise")
    FinishedExcercise toEntity(FinishedExcerciseDTO finishedExcerciseDTO);

    default FinishedExcercise fromId(Long id) {
        if (id == null) {
            return null;
        }
        FinishedExcercise finishedExcercise = new FinishedExcercise();
        finishedExcercise.setId(id);
        return finishedExcercise;
    }
}
