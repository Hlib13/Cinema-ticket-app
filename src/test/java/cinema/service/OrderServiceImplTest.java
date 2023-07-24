package cinema.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import cinema.dao.OrderDao;
import cinema.model.Order;
import cinema.model.ShoppingCart;
import cinema.model.Ticket;
import cinema.model.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import cinema.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceImplTest {

    private OrderDao orderDao;
    private ShoppingCartService shoppingCartService;
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderDao = mock(OrderDao.class);
        shoppingCartService = mock(ShoppingCartService.class);
        orderService = new OrderServiceImpl(orderDao, shoppingCartService);
    }

    @Test
    void completeOrder_Ok() {
        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart(user);
        List<Ticket> tickets = createTickets();
        shoppingCart.setTickets(tickets);
        Order orderToAdd = new Order();
        orderToAdd.setOrderTime(LocalDateTime.now());
        orderToAdd.setTickets(tickets);
        orderToAdd.setUser(user);
        when(orderDao.add(any(Order.class))).thenReturn(orderToAdd);
        Order result = orderService.completeOrder(shoppingCart);
        assertNotNull(result);
        assertEquals(orderToAdd.getOrderTime().withNano(0), result.getOrderTime().withNano(0));
        assertEquals(orderToAdd.getTickets(), result.getTickets());
        assertEquals(orderToAdd.getUser(), result.getUser());
        verify(orderDao, times(1)).add(any(Order.class));
        verify(shoppingCartService, times(1)).clear(shoppingCart);
    }

    @Test
    void getOrdersHistory_Ok() {
        User user = createUser();
        List<Order> expectedOrders = createOrders(user);
        when(orderDao.getOrdersHistory(user)).thenReturn(expectedOrders);
        List<Order> result = orderService.getOrdersHistory(user);
        assertNotNull(result);
        assertEquals(expectedOrders.size(), result.size());
        assertIterableEquals(expectedOrders, result);
        verify(orderDao, times(1)).getOrdersHistory(user);
    }
    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("testpassword");
        return user;
    }

    private List<Ticket> createTickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        tickets.add(new Ticket());
        return tickets;
    }

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCart.setTickets(new ArrayList<>());
        return shoppingCart;
    }

    private List<Order> createOrders(User user) {
        List<Order> orders = new ArrayList<>();
        orders.add(createOrder(user));
        orders.add(createOrder(user));
        return orders;
    }

    private Order createOrder(User user) {
        Order order = new Order();
        order.setId(1L);
        order.setUser(user);
        order.setOrderTime(LocalDateTime.now());
        order.setTickets(new ArrayList<>());
        return order;
    }
}
