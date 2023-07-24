package cinema.mapper;

import cinema.dto.request.CinemaHallRequestDto;
import cinema.dto.response.CinemaHallResponseDto;
import cinema.mapper.impl.CinemaHallMapperImpl;
import cinema.model.CinemaHall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CinemaHallMapperImplTest {

    private CinemaHallMapperImpl cinemaHallMapper;

    @BeforeEach
    void setUp() {
        cinemaHallMapper = new CinemaHallMapperImpl();
    }

    @Test
    void mapToModel_ShouldMapRequestDtoToModel() {
        CinemaHallRequestDto requestDto = new CinemaHallRequestDto();
        requestDto.setDescription("Test Description");
        requestDto.setCapacity(100);
        CinemaHall cinemaHall = cinemaHallMapper.mapToModel(requestDto);
        Assertions.assertEquals("Test Description", cinemaHall.getDescription());
        Assertions.assertEquals(100, cinemaHall.getCapacity());
    }

    @Test
    void mapToDto_ShouldMapModelToResponseDto() {
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setId(1L);
        cinemaHall.setDescription("Test Description");
        cinemaHall.setCapacity(100);
        CinemaHallResponseDto responseDto = cinemaHallMapper.mapToDto(cinemaHall);
        Assertions.assertEquals(1L, responseDto.getId());
        Assertions.assertEquals("Test Description", responseDto.getDescription());
        Assertions.assertEquals(100, responseDto.getCapacity());
    }
}
