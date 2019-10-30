package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.ExcerciseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Excercise} and its DTO {@link ExcerciseDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SkillTypeMapper.class, ExcerciseTypeMapper.class})
public interface ExcerciseMapper extends EntityMapper<ExcerciseDTO, Excercise> {

    @Mapping(source = "creator.id", target = "creatorId")
    @Mapping(source = "creator.login", target = "creatorLogin")
    ExcerciseDTO toDto(Excercise excercise);

    @Mapping(source = "creatorId", target = "creator")
    @Mapping(target = "removeSkillName", ignore = true)
    @Mapping(target = "removeExcerciseType", ignore = true)
    Excercise toEntity(ExcerciseDTO excerciseDTO);

    default Excercise fromId(Long id) {
        if (id == null) {
            return null;
        }
        Excercise excercise = new Excercise();
        excercise.setId(id);
        return excercise;
    }
}
