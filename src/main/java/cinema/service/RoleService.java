package cinema.service;

import cinema.model.Role;
import java.util.Optional;

public interface RoleService {
    Role add(Role role);

    Role getByName(String roleName);

    Optional<Role> getByNameOptional(String roleName);
}
