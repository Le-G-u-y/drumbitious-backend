package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.ExcerciseTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExcerciseType} and its DTO {@link ExcerciseTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExcerciseTypeMapper extends EntityMapper<ExcerciseTypeDTO, ExcerciseType> {



    default ExcerciseType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExcerciseType excerciseType = new ExcerciseType();
        excerciseType.setId(id);
        return excerciseType;
    }
}
