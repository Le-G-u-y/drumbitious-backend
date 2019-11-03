package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.FinishedExerciseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link FinishedExercise} and its DTO {@link FinishedExerciseDTO}.
 */
@Mapper(componentModel = "spring", uses = {FinishedSessionMapper.class, ExerciseMapper.class})
public interface FinishedExerciseMapper extends EntityMapper<FinishedExerciseDTO, FinishedExercise> {

    @Mapping(source = "finishedSession.id", target = "finishedSessionId")
    @Mapping(source = "exercise.id", target = "exerciseId")
    @Mapping(source = "exercise.exerciseName", target = "exerciseExerciseName")
    FinishedExerciseDTO toDto(FinishedExercise finishedExercise);

    @Mapping(source = "finishedSessionId", target = "finishedSession")
    @Mapping(source = "exerciseId", target = "exercise")
    FinishedExercise toEntity(FinishedExerciseDTO finishedExerciseDTO);

    default FinishedExercise fromId(Long id) {
        if (id == null) {
            return null;
        }
        FinishedExercise finishedExercise = new FinishedExercise();
        finishedExercise.setId(id);
        return finishedExercise;
    }
}
