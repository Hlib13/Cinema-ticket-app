package cinema.mapper;

import cinema.dto.request.MovieSessionRequestDto;
import cinema.dto.response.MovieSessionResponseDto;
import cinema.mapper.impl.MovieSessionMapperImpl;
import cinema.model.CinemaHall;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.service.CinemaHallService;
import cinema.service.MovieService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

public class MovieSessionMapperImplTest {

    private MovieSessionMapperImpl movieSessionMapper;

    @Mock
    private CinemaHallService cinemaHallService;

    @Mock
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movieSessionMapper = new MovieSessionMapperImpl(cinemaHallService, movieService);
    }

    @Test
    void mapToModel_ShouldMapRequestDtoToModel() {
        MovieSessionRequestDto requestDto = new MovieSessionRequestDto();
        requestDto.setMovieId(1L);
        requestDto.setCinemaHallId(1L);
        requestDto.setShowTime(LocalDateTime.now());

        Movie mockMovie = new Movie();
        mockMovie.setId(1L);
        Mockito.when(movieService.get(1L)).thenReturn(mockMovie);

        CinemaHall mockCinemaHall = new CinemaHall();
        mockCinemaHall.setId(1L);
        Mockito.when(cinemaHallService.get(1L)).thenReturn(mockCinemaHall);
        MovieSession movieSession = movieSessionMapper.mapToModel(requestDto);
        Assertions.assertEquals(1L, movieSession.getMovie().getId());
        Assertions.assertEquals(1L, movieSession.getCinemaHall().getId());
        Assertions.assertEquals(requestDto.getShowTime(), movieSession.getShowTime());
    }

    @Test
    void mapToDto_ShouldMapModelToResponseDto() {
        MovieSession movieSession = new MovieSession();
        movieSession.setId(1L);

        Movie mockMovie = new Movie();
        mockMovie.setId(2L);
        mockMovie.setTitle("Test Movie");
        movieSession.setMovie(mockMovie);

        CinemaHall mockCinemaHall = new CinemaHall();
        mockCinemaHall.setId(3L);
        movieSession.setCinemaHall(mockCinemaHall);

        movieSession.setShowTime(LocalDateTime.now());
        MovieSessionResponseDto responseDto = movieSessionMapper.mapToDto(movieSession);
        Assertions.assertEquals(1L, responseDto.getMovieSessionId());
        Assertions.assertEquals(3L, responseDto.getCinemaHallId());
        Assertions.assertEquals(2L, responseDto.getMovieId());
        Assertions.assertEquals("Test Movie", responseDto.getMovieTitle());
        Assertions.assertEquals(movieSession.getShowTime(), responseDto.getShowTime());
    }
}
