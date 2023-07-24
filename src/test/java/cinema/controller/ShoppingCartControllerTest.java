package cinema.controller;

import cinema.dto.response.ShoppingCartResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.service.MovieSessionService;
import cinema.service.ShoppingCartService;
import cinema.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShoppingCartControllerTest {

    @Test
    void addToCart_ShouldAddMovieSessionToUserCart() {
        Long movieSessionId = 1L;
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        cinema.model.User user = new cinema.model.User();
        user.setId(1L);
        user.setEmail(userDetails.getUsername());
        MovieSession movieSession = new MovieSession();
        movieSession.setId(movieSessionId);
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        UserService userService = mock(UserService.class);
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        MovieSessionService movieSessionService = mock(MovieSessionService.class);
        when(movieSessionService.get(movieSessionId)).thenReturn(movieSession);
        ResponseDtoMapper<ShoppingCartResponseDto, ShoppingCart> responseDtoMapper =
                mock(ResponseDtoMapper.class);
        ShoppingCartController shoppingCartController =
                new ShoppingCartController(shoppingCartService, userService, movieSessionService,
                        responseDtoMapper);
        shoppingCartController.addToCart(authentication, movieSessionId);
        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(movieSessionService, times(1)).get(movieSessionId);
        verify(shoppingCartService, times(1)).addSession(eq(movieSession),
                any(cinema.model.User.class));
    }

    @Test
    void getByUser_WhenUserExists_ShouldReturnShoppingCartResponseDto() {
        UserDetails userDetails = User.withUsername("test@example.com")
                .password("password")
                .roles("USER")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
        cinema.model.User user = new cinema.model.User();
        user.setId(1L);
        user.setEmail(userDetails.getUsername());
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        ShoppingCartResponseDto expectedResponseDto = new ShoppingCartResponseDto();
        expectedResponseDto.setUserId(user.getId());
        ShoppingCartService shoppingCartService = mock(ShoppingCartService.class);
        when(shoppingCartService.getByUser(user)).thenReturn(shoppingCart);
        ResponseDtoMapper<ShoppingCartResponseDto, ShoppingCart> responseDtoMapper =
                mock(ResponseDtoMapper.class);
        when(responseDtoMapper.mapToDto(shoppingCart)).thenReturn(expectedResponseDto);
        UserService userService = mock(UserService.class);
        when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        MovieSessionService movieSessionService = mock(MovieSessionService.class);
        ShoppingCartController shoppingCartController =
                new ShoppingCartController(shoppingCartService, userService, movieSessionService,
                        responseDtoMapper);
        ShoppingCartResponseDto actualResponseDto = shoppingCartController.getByUser(authentication);
        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto.getUserId(), actualResponseDto.getUserId());
        verify(userService, times(1)).findByEmail(user.getEmail());
        verify(shoppingCartService, times(1)).getByUser(user);
        verify(responseDtoMapper, times(1)).mapToDto(shoppingCart);
    }
}
