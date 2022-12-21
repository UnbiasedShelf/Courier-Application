package by.bstu.vs.stpms.courier_application.model.repository.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

import by.bstu.vs.stpms.courier_application.model.database.entity.Order;
import by.bstu.vs.stpms.courier_application.model.network.dto.OrderDto;

@Mapper(uses = {OrderedMapper.class, UserMapper.class, DestinationMapper.class})
public interface OrderMapper {
    OrderDto entityToDto(Order entity);
    Order dtoToEntity(OrderDto dto);

    List<Order> dtosToEntities(List<OrderDto> dtos);

    @AfterMapping
    default void setIds(OrderDto source, @MappingTarget Order target) {
        target.setCustomerId(source.getCustomer().getId());
        if (source.getCourier() != null) {
            target.setCourierId(source.getCourier().getId());
        }
        target.setSenderId(source.getSender().getId());
        target.setRecipientId(source.getRecipient().getId());
    }
}
