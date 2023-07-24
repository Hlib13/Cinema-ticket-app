package cinema.mapper.impl;

import cinema.dto.response.ShoppingCartResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class ShoppingCartMapperImpl implements
        ResponseDtoMapper<ShoppingCartResponseDto, ShoppingCart> {

    @Override
    public ShoppingCartResponseDto mapToDto(ShoppingCart shoppingCart) {
        ShoppingCartResponseDto responseDto = new ShoppingCartResponseDto();
        responseDto.setUserId(shoppingCart.getUser().getId());
        responseDto.setTicketIds(shoppingCart.getTickets()
                .stream()
                .map(Ticket::getId)
                .toList());
        return responseDto;
    }
}
