package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import cinema.dao.CinemaHallDao;
import cinema.model.CinemaHall;
import cinema.service.impl.CinemaHallServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CinemaHallServiceImplTest {
    private CinemaHallDao cinemaHallDao = Mockito.mock(CinemaHallDao.class);
    private CinemaHallService cinemaHallService = new CinemaHallServiceImpl(cinemaHallDao);
    private CinemaHall cinemaHall;

    @BeforeEach
    void setUp() {
        cinemaHall = new CinemaHall();
        cinemaHall.setId(1L);
        cinemaHall.setCapacity(100);
        cinemaHall.setDescription("Hall 1");
    }

    @Test
    void add_Ok() {
        Mockito.when(cinemaHallDao.add(cinemaHall)).thenReturn(cinemaHall);
        CinemaHall actual = cinemaHallService.add(cinemaHall);
        assertNotNull(actual);
        assertEquals(cinemaHall, actual);
    }

    @Test
    void get_Ok() {
        Mockito.when(cinemaHallDao.get(1L)).thenReturn(Optional.of(cinemaHall));
        CinemaHall actual = cinemaHallService.get(1L);
        assertEquals(cinemaHall, actual);
    }

    @Test
    void get_Not_Ok() {
        Mockito.when(cinemaHallDao.get(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cinemaHallService.get(2L));
    }

    @Test
    void getAll_Ok() {
        List<CinemaHall> cinemaHalls = new ArrayList<>();
        cinemaHalls.add(cinemaHall);
        Mockito.when(cinemaHallDao.getAll()).thenReturn(cinemaHalls);
        List<CinemaHall> actual = cinemaHallService.getAll();
        assertEquals(cinemaHalls, actual);
    }
}
