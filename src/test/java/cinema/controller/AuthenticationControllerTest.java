package cinema.controller;

import cinema.dto.request.UserRequestDto;
import cinema.dto.response.UserResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.User;
import cinema.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @Test
    void register_ShouldReturnUserResponseDto() {
        AuthenticationService authService = Mockito.mock(AuthenticationService.class);
        ResponseDtoMapper<UserResponseDto, User> userDtoResponseMapper = Mockito.mock(ResponseDtoMapper.class);
        AuthenticationController authenticationController = new AuthenticationController(authService, userDtoResponseMapper);
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail("test@example.com");
        requestDto.setPassword("password");
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setEmail(user.getEmail());
        when(authService.register(requestDto.getEmail(), requestDto.getPassword())).thenReturn(user);
        when(userDtoResponseMapper.mapToDto(user)).thenReturn(responseDto);
        UserResponseDto result = authenticationController.register(requestDto);
        assertEquals(responseDto.getEmail(), result.getEmail());
        verify(authService, times(1)).register(requestDto.getEmail(), requestDto.getPassword());
        verify(userDtoResponseMapper, times(1)).mapToDto(user);
    }
}


