package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.mapper.RoleMapper;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.repository.RoleRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.management.relation.RoleNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (isRoleExists(roleDTO.getRoleName())) {
            throw new DuplicateRequestException(String.format("Role already exists : %s", roleDTO.getRoleName()));
        }
        Role role = Role.of(roleDTO.getRoleName(), roleDTO.getRoleDescription());
        role = roleRepository.save(role);
        return RoleMapper.fromEntity(role);
    }

    @Transactional
    public void deleteRole(String roleId) throws RoleNotFoundException {
        if (!isRoleExistsByRoleId(roleId)) {
            throw new RoleNotFoundException("Role does not exist");
        }
        roleRepository.deleteById(roleId);
    }

    public RoleDTO getRole(String roleName) throws RoleNotFoundException {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s not found", roleName)));

        return RoleMapper.fromEntity(role);
    }

    public List<Role> validateAndGetRoles(List<String> roleNames) {
        List<Role> roles = roleRepository.findByNameIn(roleNames);
        if (CollectionUtils.isEmpty(roles)) {
            throw new IllegalArgumentException("No valid roles found");
        }
        return roles;
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::fromEntityWithoutPermission)
                .toList();
    }

    public boolean isRoleExists(String roleName) {
        return roleRepository.existsByName(roleName);
    }

    public boolean isRoleExistsByRoleId(String roleId) {
        return roleRepository.existsById(roleId);
    }

    public RoleDTO assignPermissionsToRole(String roleName, List<String> permissions) throws RoleNotFoundException {
        List<Permission> permissionList = permissionService.validateAndGetPermissions(permissions);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        role.updatePermissions(permissionList);
        return RoleMapper.fromEntity(role);
    }
}
