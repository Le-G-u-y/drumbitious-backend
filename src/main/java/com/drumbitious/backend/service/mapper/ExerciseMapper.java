package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.ExerciseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Exercise} and its DTO {@link ExerciseDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ExerciseMapper extends EntityMapper<ExerciseDTO, Exercise> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    ExerciseDTO toDto(Exercise exercise);

    @Mapping(source = "creatorId", target = "creator")
    Exercise toEntity(ExerciseDTO exerciseDTO);

    default Exercise fromId(Long id) {
        if (id == null) {
            return null;
        }
        Exercise exercise = new Exercise();
        exercise.setId(id);
        return exercise;
    }
}
