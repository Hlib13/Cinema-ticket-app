package cinema.mapper;

import cinema.dto.response.OrderResponseDto;
import cinema.mapper.impl.OrderMapperImpl;
import cinema.model.Order;
import cinema.model.Ticket;
import cinema.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderMapperImplTest {

    private OrderMapperImpl orderMapper;

    @BeforeEach
    void setUp() {
        orderMapper = new OrderMapperImpl();
    }

    @Test
    void mapToDto_ShouldMapModelToResponseDto() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTime(LocalDateTime.now());
        User user = new User();
        user.setId(2L);
        order.setUser(user);
        Ticket ticket1 = new Ticket();
        ticket1.setId(3L);
        Ticket ticket2 = new Ticket();
        ticket2.setId(4L);
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);
        order.setTickets(tickets);
        OrderResponseDto responseDto = orderMapper.mapToDto(order);
        Assertions.assertEquals(1L, responseDto.getId());
        Assertions.assertEquals(2L, responseDto.getUserId());
        Assertions.assertEquals(order.getOrderTime(), responseDto.getOrderTime());
        List<Long> expectedTicketIds = List.of(3L, 4L);
        Assertions.assertEquals(expectedTicketIds, responseDto.getTicketIds());
    }
}
