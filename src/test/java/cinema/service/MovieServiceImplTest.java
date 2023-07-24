package cinema.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import cinema.dao.MovieDao;
import cinema.model.Movie;
import cinema.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MovieServiceImplTest {
    private MovieDao movieDao = Mockito.mock(MovieDao.class);
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieServiceImpl(movieDao);
    }

    @Test
    void addMovie() {
        Movie movieToAdd = new Movie();
        movieToAdd.setTitle("Test Movie");
        movieToAdd.setDescription("Test Director");

        Movie savedMovie = new Movie();
        savedMovie.setId(1L);
        savedMovie.setTitle("Test Movie");
        savedMovie.setDescription("Test Director");
        Mockito.when(movieDao.add(movieToAdd)).thenReturn(savedMovie);
        Movie result = movieService.add(movieToAdd);
        assertNotNull(result);
        assertEquals(savedMovie.getId(), result.getId());
        assertEquals(movieToAdd.getTitle(), result.getTitle());
        assertEquals(movieToAdd.getDescription(), result.getDescription());
        verify(movieDao, times(1)).add(movieToAdd);
    }

    @Test
    void getMovieById_Ok() {
        long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Director");
        Mockito.when(movieDao.get(movieId)).thenReturn(Optional.of(movie));
        Movie result = movieService.get(movieId);
        assertNotNull(result);
        assertEquals(movieId, result.getId());
        assertEquals(movie.getTitle(), result.getTitle());
        assertEquals(movie.getDescription(), result.getDescription());

        verify(movieDao, times(1)).get(movieId);
    }

    @Test
    void getMovieById_NotFound() {
        long movieId = 1L;
        Mockito.when(movieDao.get(movieId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> movieService.get(movieId));

        verify(movieDao, times(1)).get(movieId);
    }

    @Test
    void getAllMovies() {
        Movie movie1 = new Movie();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");
        movie1.setDescription("Director 1");

        Movie movie2 = new Movie();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        movie2.setDescription("Director 2");

        List<Movie> movies = Arrays.asList(movie1, movie2);

        Mockito.when(movieDao.getAll()).thenReturn(movies);

        List<Movie> result = movieService.getAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(movies.get(0).getId(), result.get(0).getId());
        assertEquals(movies.get(0).getTitle(), result.get(0).getTitle());
        assertEquals(movies.get(0).getDescription(), result.get(0).getDescription());

        assertEquals(movies.get(1).getId(), result.get(1).getId());
        assertEquals(movies.get(1).getTitle(), result.get(1).getTitle());
        assertEquals(movies.get(1).getDescription(), result.get(1).getDescription());

        verify(movieDao, times(1)).getAll();
    }

    @Test
    void getAllMovies_EmptyList() {
        Mockito.when(movieDao.getAll()).thenReturn(Collections.emptyList());

        List<Movie> result = movieService.getAll();

        assertNotNull(result);
        assertEquals(0, result.size());

        verify(movieDao, times(1)).getAll();
    }
}
