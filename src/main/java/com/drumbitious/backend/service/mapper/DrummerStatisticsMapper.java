package com.drumbitious.backend.service.mapper;

import com.drumbitious.backend.domain.*;
import com.drumbitious.backend.service.dto.DrummerStatisticsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link DrummerStatistics} and its DTO {@link DrummerStatisticsDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DrummerStatisticsMapper extends EntityMapper<DrummerStatisticsDTO, DrummerStatistics> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    DrummerStatisticsDTO toDto(DrummerStatistics drummerStatistics);

    @Mapping(source = "userId", target = "user")
    DrummerStatistics toEntity(DrummerStatisticsDTO drummerStatisticsDTO);

    default DrummerStatistics fromId(Long id) {
        if (id == null) {
            return null;
        }
        DrummerStatistics drummerStatistics = new DrummerStatistics();
        drummerStatistics.setId(id);
        return drummerStatistics;
    }
}
