package cinema.controller;

import cinema.dto.response.OrderResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.Order;
import cinema.model.ShoppingCart;
import cinema.service.OrderService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @Test
    void completeOrder_ShouldReturnOrderResponseDto() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());

        cinema.model.User user = new cinema.model.User();
        user.setId(1L);
        user.setEmail(userDetails.getUsername());

        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(user);

        Order order = new Order();
        order.setId(1L);
        order.setUser(user);

        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.getByUser(user)).thenReturn(cart);

        OrderService orderService = mock(OrderService.class);
        when(orderService.completeOrder(cart)).thenReturn(order);

        ResponseDtoMapper<OrderResponseDto, Order> responseDtoMapper = mock(ResponseDtoMapper.class);
        OrderResponseDto expectedResponseDto = new OrderResponseDto();
        expectedResponseDto.setId(order.getId());
        when(responseDtoMapper.mapToDto(order)).thenReturn(expectedResponseDto);

        UserService userService = mock(UserService.class);
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        OrderController orderController =
                new OrderController(shoppingCartService, orderService, userService,
                        responseDtoMapper);

        OrderResponseDto actualResponseDto = orderController.completeOrder(authentication);

        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto.getId(), actualResponseDto.getId());

        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(shoppingCartService, times(1)).getByUser(user);
        verify(orderService, times(1)).completeOrder(cart);
        verify(responseDtoMapper, times(1)).mapToDto(order);
    }

    @Test
    void getOrderHistory_ShouldReturnListOfOrderResponseDto() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());

        cinema.model.User user = new cinema.model.User();
        user.setId(1L);
        user.setEmail(userDetails.getUsername());

        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setUser(user);
        orders.add(order1);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setUser(user);
        orders.add(order2);

        OrderService orderService = mock(OrderService.class);
        when(orderService.getOrdersHistory(user)).thenReturn(orders);

        ResponseDtoMapper<OrderResponseDto, Order> responseDtoMapper = mock(ResponseDtoMapper.class);
        OrderResponseDto expectedResponseDto1 = new OrderResponseDto();
        expectedResponseDto1.setId(order1.getId());
        OrderResponseDto expectedResponseDto2 = new OrderResponseDto();
        expectedResponseDto2.setId(order2.getId());
        when(responseDtoMapper.mapToDto(order1)).thenReturn(expectedResponseDto1);
        when(responseDtoMapper.mapToDto(order2)).thenReturn(expectedResponseDto2);

        UserService userService = mock(UserService.class);
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        OrderController orderController =
                new OrderController(null, orderService, userService,
                        responseDtoMapper);

        List<OrderResponseDto> actualResponseDtoList = orderController.getOrderHistory(authentication);

        assertNotNull(actualResponseDtoList);
        assertEquals(2, actualResponseDtoList.size());

        OrderResponseDto actualResponseDto1 = actualResponseDtoList.get(0);
        assertEquals(expectedResponseDto1.getId(), actualResponseDto1.getId());

        OrderResponseDto actualResponseDto2 = actualResponseDtoList.get(1);
        assertEquals(expectedResponseDto2.getId(), actualResponseDto2.getId());

        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(orderService, times(1)).getOrdersHistory(user);
        verify(responseDtoMapper, times(1)).mapToDto(order1);
        verify(responseDtoMapper, times(1)).mapToDto(order2);
    }
}
