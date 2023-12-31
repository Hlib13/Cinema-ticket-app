package cinema.mapper.impl;

import cinema.dto.response.OrderResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.Order;
import cinema.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements ResponseDtoMapper<OrderResponseDto, Order> {
    @Override
    public OrderResponseDto mapToDto(Order order) {
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setUserId(order.getUser().getId());
        responseDto.setOrderTime(order.getOrderTime());
        responseDto.setTicketIds(order.getTickets()
                .stream()
                .map(Ticket::getId)
                .toList());
        return responseDto;
    }
}
