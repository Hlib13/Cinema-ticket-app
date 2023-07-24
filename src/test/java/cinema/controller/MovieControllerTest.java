package cinema.controller;

import cinema.dto.request.MovieRequestDto;
import cinema.dto.response.MovieResponseDto;
import cinema.mapper.RequestDtoMapper;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.Movie;
import cinema.service.MovieService;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MovieControllerTest {

    @Test
    void add_ShouldReturnMovieResponseDto() {
        MovieRequestDto requestDto = new MovieRequestDto();
        requestDto.setTitle("Test Movie");
        requestDto.setDescription("Test Description");
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        MovieService movieService = mock(MovieService.class);
        when(movieService.add(any(Movie.class))).thenReturn(movie);
        RequestDtoMapper<MovieRequestDto, Movie> requestDtoMapper = mock(RequestDtoMapper.class);
        when(requestDtoMapper.mapToModel(any(MovieRequestDto.class))).thenReturn(movie);
        ResponseDtoMapper<MovieResponseDto, Movie> responseDtoMapper = mock(ResponseDtoMapper.class);
        MovieResponseDto responseDto = new MovieResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Test Movie");
        responseDto.setDescription("Test Description");
        when(responseDtoMapper.mapToDto(any(Movie.class))).thenReturn(responseDto);
        MovieController movieController = new MovieController(movieService, requestDtoMapper, responseDtoMapper);
        MovieResponseDto actualResponseDto = movieController.add(requestDto);
        assertNotNull(actualResponseDto);
        assertEquals(responseDto.getId(), actualResponseDto.getId());
        assertEquals(responseDto.getTitle(), actualResponseDto.getTitle());
        assertEquals(responseDto.getDescription(), actualResponseDto.getDescription());
        verify(movieService, times(1)).add(any(Movie.class));
        verify(requestDtoMapper, times(1)).mapToModel(any(MovieRequestDto.class));
        verify(responseDtoMapper, times(1)).mapToDto(any(Movie.class));
    }

    @Test
    void getAll_ShouldReturnListOfMovieResponseDto() {
        List<Movie> movies = new ArrayList<>();
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");
        movie1.setDescription("Description 1");
        movies.add(movie1);
        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        movie2.setDescription("Description 2");
        movies.add(movie2);
        MovieService movieService = mock(MovieService.class);
        when(movieService.getAll()).thenReturn(movies);
        ResponseDtoMapper<MovieResponseDto, Movie> responseDtoMapper = mock(ResponseDtoMapper.class);
        MovieResponseDto responseDto1 = new MovieResponseDto();
        responseDto1.setId(1L);
        responseDto1.setTitle("Movie 1");
        responseDto1.setDescription("Description 1");
        when(responseDtoMapper.mapToDto(movie1)).thenReturn(responseDto1);
        MovieResponseDto responseDto2 = new MovieResponseDto();
        responseDto2.setId(2L);
        responseDto2.setTitle("Movie 2");
        responseDto2.setDescription("Description 2");
        when(responseDtoMapper.mapToDto(movie2)).thenReturn(responseDto2);
        MovieController movieController = new MovieController(movieService, null, responseDtoMapper);
        List<MovieResponseDto> actualResponseDtos = movieController.getAll();
        assertNotNull(actualResponseDtos);
        assertEquals(2, actualResponseDtos.size());
        MovieResponseDto actualResponseDto1 = actualResponseDtos.get(0);
        assertEquals(responseDto1.getId(), actualResponseDto1.getId());
        assertEquals(responseDto1.getTitle(), actualResponseDto1.getTitle());
        assertEquals(responseDto1.getDescription(), actualResponseDto1.getDescription());
        MovieResponseDto actualResponseDto2 = actualResponseDtos.get(1);
        assertEquals(responseDto2.getId(), actualResponseDto2.getId());
        assertEquals(responseDto2.getTitle(), actualResponseDto2.getTitle());
        assertEquals(responseDto2.getDescription(), actualResponseDto2.getDescription());
        verify(movieService, times(1)).getAll();
        verify(responseDtoMapper, times(1)).mapToDto(movie1);
        verify(responseDtoMapper, times(1)).mapToDto(movie2);
    }
}
