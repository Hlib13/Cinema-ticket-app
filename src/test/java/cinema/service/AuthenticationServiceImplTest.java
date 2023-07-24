package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Set;
import cinema.model.Role;
import cinema.model.User;
import cinema.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AuthenticationServiceImplTest {
    private static final String TEST_EMAIL_OK = "gleb1234@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserService userService = Mockito.mock(UserService.class);
    private ShoppingCartService shoppingCartService = Mockito.mock(ShoppingCartService.class);
    private RoleService roleService = Mockito.mock(RoleService.class);
    private AuthenticationService authenticationService = new
            AuthenticationServiceImpl(userService,shoppingCartService,roleService);
    private Role userRole;
    private User user;

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void register_Ok() {
        Mockito.when(roleService.getByName(Role.RoleName.USER.name())).thenReturn(userRole);
        User registeredUser = authenticationService.register(TEST_EMAIL_OK, TEST_PASSWORD_OK);
        assertNotNull(registeredUser);
        assertEquals(TEST_EMAIL_OK, registeredUser.getEmail());
        assertEquals(TEST_PASSWORD_OK, registeredUser.getPassword());
        assertEquals(1, registeredUser.getRoles().size());
        assertTrue(registeredUser.getRoles().contains(userRole));
        verify(userService, times(1)).add(registeredUser);
        verify(shoppingCartService, times(1)).registerNewShoppingCart(registeredUser);
    }

    @Test
    void register_NullEmail() {
        assertThrows(NullPointerException.class, () -> authenticationService.register(null, TEST_PASSWORD_OK));
        verify(userService, never()).add(any());
        verify(shoppingCartService, never()).registerNewShoppingCart(any());
    }

    @Test
    void register_NullPassword() {
        assertThrows(NullPointerException.class, () -> authenticationService.register(TEST_EMAIL_OK, null));
        verify(userService, never()).add(any());
        verify(shoppingCartService, never()).registerNewShoppingCart(any());
    }

    @Test
    void register_RoleNotFound() {
        when(roleService.getByName(Role.RoleName.USER.name())).thenReturn(null);
        assertThrows(NullPointerException.class, () -> authenticationService.register(TEST_EMAIL_OK, TEST_PASSWORD_OK));
        verify(userService, never()).add(any());
        verify(shoppingCartService, never()).registerNewShoppingCart(any());
    }
}
