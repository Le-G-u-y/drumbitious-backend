package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.ExerciseConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExerciseConfig} and its DTO {@link ExerciseConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExerciseMapper.class})
public interface ExerciseConfigMapper extends EntityMapper<ExerciseConfigDTO, ExerciseConfig> {

    @Mapping(source = "exercise.id", target = "exerciseId")
    @Mapping(source = "exercise.exerciseName", target = "exerciseExerciseName")
    ExerciseConfigDTO toDto(ExerciseConfig exerciseConfig);

    @Mapping(target = "plans", ignore = true)
    @Mapping(target = "removePlan", ignore = true)
    @Mapping(source = "exerciseId", target = "exercise")
    ExerciseConfig toEntity(ExerciseConfigDTO exerciseConfigDTO);

    default ExerciseConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExerciseConfig exerciseConfig = new ExerciseConfig();
        exerciseConfig.setId(id);
        return exerciseConfig;
    }
}
