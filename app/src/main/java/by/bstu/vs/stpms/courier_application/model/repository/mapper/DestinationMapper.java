package by.bstu.vs.stpms.courier_application.model.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import by.bstu.vs.stpms.courier_application.model.database.entity.Destination;
import by.bstu.vs.stpms.courier_application.model.network.dto.DestinationDto;

@Mapper
public interface DestinationMapper {
    @Mapping(target = "rangeStart", source = "rangeStart", qualifiedByName = "timestampToString")
    @Mapping(target = "rangeEnd", source = "rangeEnd", qualifiedByName = "timestampToString")
    DestinationDto entityToDto(Destination entity);
    @Mapping(target = "rangeStart", source = "rangeStart", qualifiedByName = "stringToTimeStamp")
    @Mapping(target = "rangeEnd", source = "rangeEnd", qualifiedByName = "stringToTimeStamp")
    Destination dtoToEntity(DestinationDto dto);

    @Named("timestampToString")
    default String timestampToString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(timestamp);
    }

    @Named("stringToTimeStamp")
    default Timestamp stringToTimeStamp(String str) {
        if (str == null) {
            return null;
        }
        Timestamp timestamp = null;
        try {
            timestamp = new Timestamp(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").parse(str).getTime());
        } catch (Exception e) { }
        return timestamp;
    }
}
