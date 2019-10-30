package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.ExcerciseConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExcerciseConfig} and its DTO {@link ExcerciseConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {ExcerciseMapper.class})
public interface ExcerciseConfigMapper extends EntityMapper<ExcerciseConfigDTO, ExcerciseConfig> {

    @Mapping(source = "excercise.id", target = "excerciseId")
    @Mapping(source = "excercise.excerciseName", target = "excerciseExcerciseName")
    ExcerciseConfigDTO toDto(ExcerciseConfig excerciseConfig);

    @Mapping(target = "plans", ignore = true)
    @Mapping(target = "removePlan", ignore = true)
    @Mapping(source = "excerciseId", target = "excercise")
    ExcerciseConfig toEntity(ExcerciseConfigDTO excerciseConfigDTO);

    default ExcerciseConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExcerciseConfig excerciseConfig = new ExcerciseConfig();
        excerciseConfig.setId(id);
        return excerciseConfig;
    }
}
