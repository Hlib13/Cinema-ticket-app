package cinema.mapper;

import cinema.dto.request.MovieRequestDto;
import cinema.dto.response.MovieResponseDto;
import cinema.mapper.impl.MovieMapperImpl;
import cinema.model.Movie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MovieMapperImplTest {

    private MovieMapperImpl movieMapper;

    @BeforeEach
    void setUp() {
        movieMapper = new MovieMapperImpl();
    }

    @Test
    void mapToModel_ShouldMapRequestDtoToModel() {
        MovieRequestDto requestDto = new MovieRequestDto();
        requestDto.setTitle("Test Movie");
        requestDto.setDescription("Test Description");
        Movie movie = movieMapper.mapToModel(requestDto);
        Assertions.assertEquals("Test Movie", movie.getTitle());
        Assertions.assertEquals("Test Description", movie.getDescription());
    }

    @Test
    void mapToDto_ShouldMapModelToResponseDto() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        MovieResponseDto responseDto = movieMapper.mapToDto(movie);
        Assertions.assertEquals(1L, responseDto.getId());
        Assertions.assertEquals("Test Movie", responseDto.getTitle());
        Assertions.assertEquals("Test Description", responseDto.getDescription());
    }
}
