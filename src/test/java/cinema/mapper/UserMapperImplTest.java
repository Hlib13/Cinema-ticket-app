package cinema.mapper;

import cinema.dto.response.UserResponseDto;
import cinema.mapper.impl.UserMapperImpl;
import cinema.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void mapToDto_ShouldMapModelToResponseDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        UserResponseDto responseDto = userMapper.mapToDto(user);
        Assertions.assertEquals(1L, responseDto.getId());
        Assertions.assertEquals("john@example.com", responseDto.getEmail());
    }
}
