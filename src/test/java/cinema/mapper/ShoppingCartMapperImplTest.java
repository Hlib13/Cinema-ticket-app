package cinema.mapper;

import java.util.ArrayList;
import java.util.List;
import cinema.dto.response.ShoppingCartResponseDto;
import cinema.mapper.impl.ShoppingCartMapperImpl;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShoppingCartMapperImplTest {

    private ShoppingCartMapperImpl shoppingCartMapper;

    @BeforeEach
    void setUp() {
        shoppingCartMapper = new ShoppingCartMapperImpl();
    }

    @Test
    void mapToDto_ShouldMapModelToResponseDto() {
        ShoppingCart shoppingCart = new ShoppingCart();

        User user = new User();
        user.setId(1L);
        shoppingCart.setUser(user);

        Ticket ticket1 = new Ticket();
        ticket1.setId(2L);

        Ticket ticket2 = new Ticket();
        ticket2.setId(3L);

        List<Ticket> tickets = new ArrayList<>();
        tickets.add(ticket1);
        tickets.add(ticket2);
        shoppingCart.setTickets(tickets);
        ShoppingCartResponseDto responseDto = shoppingCartMapper.mapToDto(shoppingCart);
        Assertions.assertEquals(1L, responseDto.getUserId());

        List<Long> expectedTicketIds = List.of(2L, 3L);
        Assertions.assertEquals(expectedTicketIds, responseDto.getTicketIds());
    }
}
