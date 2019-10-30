package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.SkillTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link SkillType} and its DTO {@link SkillTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SkillTypeMapper extends EntityMapper<SkillTypeDTO, SkillType> {



    default SkillType fromId(Long id) {
        if (id == null) {
            return null;
        }
        SkillType skillType = new SkillType();
        skillType.setId(id);
        return skillType;
    }
}
