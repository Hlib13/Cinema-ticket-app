package cinema.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cinema.dto.request.CinemaHallRequestDto;
import cinema.dto.response.CinemaHallResponseDto;
import cinema.mapper.RequestDtoMapper;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.CinemaHall;
import cinema.service.CinemaHallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CinemaHallControllerTest {

    private CinemaHallService cinemaHallService;
    private RequestDtoMapper<CinemaHallRequestDto, CinemaHall> cinemaHallRequestDtoMapper;
    private ResponseDtoMapper<CinemaHallResponseDto, CinemaHall> cinemaHallResponseDtoMapper;
    private CinemaHallController cinemaHallController;

    @BeforeEach
    void setUp() {
        cinemaHallService = mock(CinemaHallService.class);
        cinemaHallRequestDtoMapper = mock(RequestDtoMapper.class);
        cinemaHallResponseDtoMapper = mock(ResponseDtoMapper.class);
        cinemaHallController = new CinemaHallController(
                cinemaHallService, cinemaHallRequestDtoMapper, cinemaHallResponseDtoMapper);
    }

    @Test
    void add_ShouldReturnCinemaHallResponseDto() {
        CinemaHallRequestDto requestDto = new CinemaHallRequestDto();
        requestDto.setDescription("Test Cinema Hall");
        requestDto.setCapacity(100);
        CinemaHall cinemaHall = new CinemaHall();
        cinemaHall.setId(1L);
        cinemaHall.setDescription("Test Cinema Hall");
        cinemaHall.setCapacity(100);
        CinemaHallResponseDto expectedResponseDto = new CinemaHallResponseDto();
        expectedResponseDto.setId(1L);
        expectedResponseDto.setDescription("Test Cinema Hall");
        expectedResponseDto.setCapacity(100);
        when(cinemaHallRequestDtoMapper.mapToModel(requestDto)).thenReturn(cinemaHall);
        when(cinemaHallService.add(cinemaHall)).thenReturn(cinemaHall);
        when(cinemaHallResponseDtoMapper.mapToDto(cinemaHall)).thenReturn(expectedResponseDto);

        CinemaHallResponseDto actualResponseDto = cinemaHallController.add(requestDto);

        assertNotNull(actualResponseDto);
        assertEquals(expectedResponseDto.getId(), actualResponseDto.getId());
        assertEquals(expectedResponseDto.getDescription(), actualResponseDto.getDescription());
        assertEquals(expectedResponseDto.getCapacity(), actualResponseDto.getCapacity());
    }

    @Test
    void getAll_ShouldReturnListOfCinemaHallResponseDto() {
        CinemaHall cinemaHall1 = new CinemaHall();
        cinemaHall1.setId(1L);
        cinemaHall1.setDescription("Cinema Hall 1");
        cinemaHall1.setCapacity(150);

        CinemaHall cinemaHall2 = new CinemaHall();
        cinemaHall2.setId(2L);
        cinemaHall2.setDescription("Cinema Hall 2");
        cinemaHall2.setCapacity(120);

        List<CinemaHall> cinemaHalls = new ArrayList<>();
        cinemaHalls.add(cinemaHall1);
        cinemaHalls.add(cinemaHall2);

        when(cinemaHallService.getAll()).thenReturn(cinemaHalls);

        CinemaHallResponseDto cinemaHallResponseDto1 = new CinemaHallResponseDto();
        cinemaHallResponseDto1.setId(1L);
        cinemaHallResponseDto1.setDescription("Cinema Hall 1");
        cinemaHallResponseDto1.setCapacity(150);

        CinemaHallResponseDto cinemaHallResponseDto2 = new CinemaHallResponseDto();
        cinemaHallResponseDto2.setId(2L);
        cinemaHallResponseDto2.setDescription("Cinema Hall 2");
        cinemaHallResponseDto2.setCapacity(120);

        when(cinemaHallResponseDtoMapper.mapToDto(cinemaHall1)).thenReturn(cinemaHallResponseDto1);
        when(cinemaHallResponseDtoMapper.mapToDto(cinemaHall2)).thenReturn(cinemaHallResponseDto2);

        List<CinemaHallResponseDto> actualResponseDtos = cinemaHallController.getAll();

        assertNotNull(actualResponseDtos);
        assertEquals(2, actualResponseDtos.size());

        CinemaHallResponseDto actualDto1 = actualResponseDtos.get(0);
        assertEquals(1L, actualDto1.getId());
        assertEquals("Cinema Hall 1", actualDto1.getDescription());
        assertEquals(150, actualDto1.getCapacity());

        CinemaHallResponseDto actualDto2 = actualResponseDtos.get(1);
        assertEquals(2L, actualDto2.getId());
        assertEquals("Cinema Hall 2", actualDto2.getDescription());
        assertEquals(120, actualDto2.getCapacity());
    }
}
