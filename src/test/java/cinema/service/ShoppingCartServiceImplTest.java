package cinema.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cinema.dao.ShoppingCartDao;
import cinema.dao.TicketDao;
import cinema.model.Movie;
import cinema.model.MovieSession;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import cinema.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartServiceImplTest {

    private ShoppingCartDao shoppingCartDao;
    private TicketDao ticketDao;
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        shoppingCartDao = mock(ShoppingCartDao.class);
        ticketDao = mock(TicketDao.class);
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartDao, ticketDao);
    }

    @Test
    void addSession_Ok() {
        User user = createUser();
        MovieSession movieSession = createMovieSession(1L, createMovie());
        ShoppingCart shoppingCart = createShoppingCart(user);
        Ticket ticket = createTicket(movieSession, user);
        when(shoppingCartDao.getByUser(user)).thenReturn(shoppingCart);
        shoppingCartService.addSession(movieSession, user);
        verify(ticketDao, times(1)).add(ticket);
        assertTrue(shoppingCart.getTickets().contains(ticket));
        verify(shoppingCartDao, times(1)).update(shoppingCart);
    }

    @Test
    void getByUser_Ok() {
        User user = createUser();
        ShoppingCart expectedShoppingCart = createShoppingCart(user);
        when(shoppingCartDao.getByUser(user)).thenReturn(expectedShoppingCart);
        ShoppingCart result = shoppingCartService.getByUser(user);
        assertNotNull(result);
        assertEquals(expectedShoppingCart, result);
    }

    @Test
    void registerNewShoppingCart_Ok() {
        User user = createUser();
        shoppingCartService.registerNewShoppingCart(user);
        verify(shoppingCartDao, times(1)).add(any(ShoppingCart.class));
    }

    @Test
    void clear_Ok() {
        User user = createUser();
        List<Ticket> tickets = createTickets(3, createMovieSession(1L, createMovie()));
        ShoppingCart shoppingCart = createShoppingCart(user);
        shoppingCart.setTickets(tickets);
        shoppingCartService.clear(shoppingCart);
        assertNull(shoppingCart.getTickets());
        verify(shoppingCartDao, times(1)).update(shoppingCart);
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        return user;
    }

    private Movie createMovie() {
        return new Movie();
    }

    private MovieSession createMovieSession(Long id, Movie movie) {
        MovieSession movieSession = new MovieSession();
        movieSession.setId(id);
        movieSession.setMovie(movie);
        movieSession.setShowTime(LocalDateTime.now());
        return movieSession;
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        return shoppingCart;
    }

    private Ticket createTicket(MovieSession movieSession, User user) {
        Ticket ticket = new Ticket();
        ticket.setMovieSession(movieSession);
        ticket.setUser(user);
        return ticket;
    }

    private List<Ticket> createTickets(int count, MovieSession movieSession) {
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tickets.add(createTicket(movieSession, createUser()));
        }
        return tickets;
    }
}
