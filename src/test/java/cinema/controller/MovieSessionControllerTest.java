package cinema.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import cinema.dto.request.MovieSessionRequestDto;
import cinema.dto.response.MovieSessionResponseDto;
import cinema.mapper.RequestDtoMapper;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.MovieSession;
import cinema.service.MovieSessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieSessionControllerTest {

    private MovieSessionService movieSessionService = Mockito.mock(MovieSessionService.class);
    private RequestDtoMapper<MovieSessionRequestDto, MovieSession> requestDtoMapper = Mockito.mock(RequestDtoMapper.class);
    private ResponseDtoMapper<MovieSessionResponseDto, MovieSession> responseDtoMapper = Mockito.mock(ResponseDtoMapper.class);

    private MovieSessionController movieSessionController;

    @BeforeEach
    void setUp() {
        movieSessionController = new MovieSessionController(movieSessionService, requestDtoMapper, responseDtoMapper);
    }

    @Test
    void add_ShouldReturnMovieSessionResponseDto() {
        MovieSessionRequestDto requestDto = new MovieSessionRequestDto();
        MovieSession movieSession = new MovieSession();
        MovieSessionResponseDto responseDto = new MovieSessionResponseDto();
        when(requestDtoMapper.mapToModel(requestDto)).thenReturn(movieSession);
        when(movieSessionService.add(movieSession)).thenReturn(movieSession);
        when(responseDtoMapper.mapToDto(movieSession)).thenReturn(responseDto);
        MovieSessionResponseDto result = movieSessionController.add(requestDto);
        assertEquals(responseDto, result);
        verify(requestDtoMapper, times(1)).mapToModel(requestDto);
        verify(movieSessionService, times(1)).add(movieSession);
        verify(responseDtoMapper, times(1)).mapToDto(movieSession);
    }

    @Test
    void findAvailableSessions_ShouldReturnListOfMovieSessionResponseDto() {
        Long movieId = 1L;
        LocalDate date = LocalDate.of(2023, 7, 23);
        List<MovieSession> movieSessions = new ArrayList<>();
        movieSessions.add(new MovieSession());
        List<MovieSessionResponseDto> responseDtos = new ArrayList<>();
        responseDtos.add(new MovieSessionResponseDto());
        when(movieSessionService.findAvailableSessions(movieId, date)).thenReturn(movieSessions);
        when(responseDtoMapper.mapToDto(any(MovieSession.class))).thenReturn(new MovieSessionResponseDto());
        List<MovieSessionResponseDto> result = movieSessionController.findAvailableSessions(movieId, date);
        assertEquals(responseDtos.size(), result.size());
        verify(movieSessionService, times(1)).findAvailableSessions(movieId, date);
        verify(responseDtoMapper, times(movieSessions.size())).mapToDto(any(MovieSession.class));
    }
}