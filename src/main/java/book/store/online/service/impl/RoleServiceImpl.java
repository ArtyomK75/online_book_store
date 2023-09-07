package book.store.online.service.impl;

import book.store.online.model.Role;
import book.store.online.repository.role.RoleRepository;
import book.store.online.service.RoleService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getByName(String name) {
        Role.RoleName existRole = null;
        for (Role.RoleName currentRole : Role.RoleName.values()) {
            if (currentRole.name().equals(name)) {
                existRole = currentRole;
                break;
            }
        }
        if (existRole == null) {
            throw new RuntimeException("Send incorrect list of roles");
        }

        Optional<Role> optionalRoleUser = roleRepository.findRoleByName(existRole);
        if (optionalRoleUser.isPresent()) {
            return optionalRoleUser.get();
        }
        return roleRepository.save(new Role(existRole));
    }
}
