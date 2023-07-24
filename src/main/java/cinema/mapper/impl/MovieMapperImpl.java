package cinema.mapper.impl;

import cinema.dto.request.MovieRequestDto;
import cinema.dto.response.MovieResponseDto;
import cinema.mapper.RequestDtoMapper;
import cinema.mapper.ResponseDtoMapper;
import cinema.model.Movie;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements RequestDtoMapper<MovieRequestDto, Movie>,
        ResponseDtoMapper<MovieResponseDto, Movie> {
    @Override
    public Movie mapToModel(MovieRequestDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        return movie;
    }

    @Override
    public MovieResponseDto mapToDto(Movie movie) {
        MovieResponseDto responseDto = new MovieResponseDto();
        responseDto.setId(movie.getId());
        responseDto.setTitle(movie.getTitle());
        responseDto.setDescription(movie.getDescription());
        return responseDto;
    }
}
