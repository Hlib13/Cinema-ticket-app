package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import cinema.dao.UserDao;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {
    private static final String TEST_EMAIL_OK = "gleb1234@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserDao userDao = Mockito.mock(UserDao.class);
    private PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private UserService userService = new UserServiceImpl(passwordEncoder, userDao);
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
        user = new User();
        user.setPassword(TEST_PASSWORD_OK);
        user.setEmail(TEST_EMAIL_OK);
        user.setRoles(Set.of(role));
    }

    @Test
    void save_Ok() {
        Mockito.when(userDao.add(user)).thenReturn(user);
        User actual = userService.add(user);
        assertNotNull(actual);
        assertEquals(user.getPassword(), actual.getPassword());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getRoles(), actual.getRoles());
    }

    @Test
    void getById_Ok() {
        Mockito.when(userDao.get(user.getId())).thenReturn(Optional.of(user));
        User actual = userService.get(user.getId());
        assertEquals(user, actual);
    }

    @Test
    void findById_Not_Ok() {
        Mockito.when(userDao.get(user.getId())).thenReturn(null);
        assertThrows(RuntimeException.class, () ->
                userService.get(user.getId()));
    }

    @Test
    void findByEmail_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findByEmail(user.getEmail());
        assertTrue(actual.isPresent());
        assertEquals(user, actual.get());
    }

    @Test
    void findByEmail_Not_Ok() {
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                userService.findByEmail(user.getEmail()).get());
    }
}
