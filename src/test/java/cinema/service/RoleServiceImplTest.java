package cinema.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.NoSuchElementException;
import java.util.Optional;
import cinema.dao.RoleDao;
import cinema.model.Role;
import cinema.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class RoleServiceImplTest {
    private RoleDao roleDao = Mockito.mock(RoleDao.class);
    private RoleService roleService = new RoleServiceImpl(roleDao);
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(Role.RoleName.USER);
    }

    @Test
    void save_Ok() {
        Mockito.when(roleDao.add(role)).thenReturn(role);
        Role actual = roleService.add(role);
        assertNotNull(actual);
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Ok() {
        Mockito.when(roleDao.getByName(Role.RoleName.USER.name()))
                .thenReturn(Optional.of(role));
        Role actual = roleService.getByName(Role.RoleName.USER.name());
        assertEquals(role, actual);
    }

    @Test
    void getRoleByName_Not_Ok() {
        assertThrows(NoSuchElementException.class, () ->
                roleService.getByName("Wrong roleName"));
    }
}
