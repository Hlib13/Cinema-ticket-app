package cinema.config;

import cinema.model.Role;
import cinema.model.User;
import cinema.service.RoleService;
import cinema.service.UserService;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final UserService userService;
    private final RoleService roleService;

    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void inject() {
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);
        injectRole(adminRole);
        Role userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);
        injectRole(userRole);
        injectUser(Set.of(adminRole));
    }

    private void injectUser(Set<Role> roles) {
        User user = new User();
        user.setEmail("admin@admin.com");
        user.setPassword("admin123");
        user.setRoles(roles);
        if (userService.findByEmail(user.getEmail()).isEmpty()) {
            userService.add(user);
        }
    }

    private void injectRole(Role role) {
        if (roleService.getByNameOptional(String.valueOf(role
                .getRoleName())).isEmpty()) {
            roleService.add(role);
        }
    }
}
