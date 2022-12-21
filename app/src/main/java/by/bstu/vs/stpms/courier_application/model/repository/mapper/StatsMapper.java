package by.bstu.vs.stpms.courier_application.model.repository.mapper;

import org.mapstruct.Mapper;

import by.bstu.vs.stpms.courier_application.model.database.entity.Stats;
import by.bstu.vs.stpms.courier_application.model.network.dto.StatsDto;

@Mapper
public interface StatsMapper {
    StatsDto entityToDto(Stats entity);
    Stats dtoToEntity(StatsDto dto);
}