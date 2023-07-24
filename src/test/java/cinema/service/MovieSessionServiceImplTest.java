package cinema.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cinema.dao.MovieSessionDao;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.CinemaHall;
import cinema.service.impl.MovieSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovieSessionServiceImplTest {

    private MovieSessionDao movieSessionDao;
    private MovieSessionServiceImpl movieSessionService;

    @BeforeEach
    void setUp() {
        movieSessionDao = mock(MovieSessionDao.class);
        movieSessionService = new MovieSessionServiceImpl(movieSessionDao);
    }

    @Test
    void findAvailableSessions_Ok() {
        Long movieId = 1L;
        LocalDateTime date = LocalDateTime.now();
        List<MovieSession> expectedSessions = new ArrayList<>();
        expectedSessions.add(createMovieSession(1L, createMovie(), createDateWithTime(2023, 7, 23, 18, 0)));
        expectedSessions.add(createMovieSession(2L, createMovie(), createDateWithTime(2023, 7, 23, 21, 0)));
        when(movieSessionDao.findAvailableSessions(movieId, LocalDate.from(LocalDateTime.from(date)))).thenReturn(expectedSessions);
        List<MovieSession> result = movieSessionService.findAvailableSessions(movieId, LocalDate.from(LocalDateTime.from(date)));
        assertNotNull(result);
        assertEquals(expectedSessions.size(), result.size());
        assertIterableEquals(expectedSessions, result);
        verify(movieSessionDao, times(1)).findAvailableSessions(movieId, LocalDate.from(LocalDateTime.from(date)));
    }

    @Test
    void addMovieSession() {
        MovieSession sessionToAdd = createMovieSession(null, createMovie(), createDateWithTime(2023, 7, 23, 18, 0));
        MovieSession savedSession = createMovieSession(1L, createMovie(), createDateWithTime(2023, 7, 23, 18, 0));
        when(movieSessionDao.add(sessionToAdd)).thenReturn(savedSession);
        MovieSession result = movieSessionService.add(sessionToAdd);
        assertNotNull(result);
        assertEquals(savedSession.getId(), result.getId());
        assertEquals(sessionToAdd.getMovie(), result.getMovie());
        assertEquals(sessionToAdd.getCinemaHall(), result.getCinemaHall());
        assertEquals(sessionToAdd.getShowTime(), result.getShowTime());
        verify(movieSessionDao, times(1)).add(sessionToAdd);
    }

    @Test
    void getMovieSessionById_Ok() {
        Long sessionId = 1L;
        MovieSession expectedSession = createMovieSession(sessionId, createMovie(), createDateWithTime(2023, 7, 23, 18, 0));
        when(movieSessionDao.get(sessionId)).thenReturn(java.util.Optional.of(expectedSession));
        MovieSession result = movieSessionService.get(sessionId);
        assertNotNull(result);
        assertEquals(expectedSession, result);
        verify(movieSessionDao, times(1)).get(sessionId);
    }

    @Test
    void getMovieSessionById_NotFound() {
        Long sessionId = 1L;
        when(movieSessionDao.get(sessionId)).thenReturn(java.util.Optional.empty());
        assertThrows(RuntimeException.class, () -> movieSessionService.get(sessionId));
        verify(movieSessionDao, times(1)).get(sessionId);
    }

    private Movie createMovie() {
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("This is a test movie");
        return movie;
    }

    private MovieSession createMovieSession(Long id, Movie movie, LocalDateTime showTime) {
        MovieSession session = new MovieSession();
        session.setId(id);
        session.setMovie(movie);
        session.setCinemaHall(new CinemaHall());
        session.setShowTime(showTime);
        return session;
    }

    private LocalDateTime createDateWithTime(int year, int month, int day, int hour, int minute) {
        return LocalDateTime.of(year, month, day, hour, minute);
    }
}
