package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cinema.model.Role;
import cinema.model.User;
import cinema.service.UserService;
import cinema.service.impl.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CustomUserDetailsServiceTest {
    private static final String TEST_EMAIL_OK = "gleb1234@gmail.com";
    private static final String TEST_PASSWORD_OK = "12345678";
    private UserService userService = Mockito.mock(UserService.class);
    private AuthenticationService authenticationService = Mockito.mock(AuthenticationService.class);
    private UserDetailsService userDetailsService = new CustomUserDetailsService(userService);
    private User user;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role(Role.RoleName.USER);
        user = new User();
        user.setEmail(TEST_EMAIL_OK);
        user.setPassword(TEST_PASSWORD_OK);
        user.setRoles(Set.of(userRole));
    }

    @Test
    void loadUserByUsername_Ok() {
        Mockito.when(userService.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth ->
                auth.getAuthority().contains(Role.RoleName.USER.name())));
    }

    @Test
    void loadUserByUsername_NotFound() {

        Mockito.when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("nonexistent@example.com"));
    }
}