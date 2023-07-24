package cinema.controller;

import cinema.dto.response.UserResponseDto;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.User;
import cinema.service.UserService;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Test
    void findByEmail_WhenUserExists_ShouldReturnUserResponseDto() {
        String email = "test@example.com";

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        UserResponseDto expectedResponseDto = new UserResponseDto();
        expectedResponseDto.setId(1L);
        expectedResponseDto.setEmail(email);

        UserService userService = mock(UserService.class);
        when(userService.findByEmail(email)).thenReturn(Optional.of(user));

        ResponseDtoMapper<UserResponseDto, User> responseDtoMapper = mock(ResponseDtoMapper.class);
        when(responseDtoMapper.mapToDto(user)).thenReturn(expectedResponseDto);

        UserController userController = new UserController(userService, responseDtoMapper);

        UserResponseDto actualResponseDto = userController.findByEmail(email);

        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto.getId(), actualResponseDto.getId());
        assertEquals(expectedResponseDto.getEmail(), actualResponseDto.getEmail());

        verify(userService, times(1)).findByEmail(email);
        verify(responseDtoMapper, times(1)).mapToDto(user);
    }

    @Test
    void findByEmail_WhenUserDoesNotExist_ShouldThrowRuntimeException() {
        String email = "test@example.com";

        UserService userService = mock(UserService.class);
        when(userService.findByEmail(email)).thenReturn(Optional.empty());

        UserController userController = new UserController(userService, null);

        assertThrows(RuntimeException.class, () -> userController.findByEmail(email));

        verify(userService, times(1)).findByEmail(email);
    }
}
