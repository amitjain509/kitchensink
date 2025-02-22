package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.mapper.RoleMapper;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (isRoleExists(roleDTO.getRoleName())) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.DUPLICATE_ROLE)
                    .referenceId(roleDTO.getRoleName())
                    .build();
        }
        Role role = Role.of(roleDTO.getRoleName(), roleDTO.getRoleDescription());
        role = roleRepository.save(role);
        return RoleMapper.fromEntity(role);
    }

    @Transactional
    public void deleteRole(String roleId) {
        if (!isRoleExistsByRoleId(roleId)) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.ROLE_NOT_FOUND)
                    .referenceId(roleId)
                    .build();
        }
        roleRepository.deleteById(roleId);
    }

    public RoleDTO getRole(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> KitchenSinkException
                        .builder(ApplicationErrorCode.ROLE_NOT_FOUND)
                        .referenceId(roleId)
                        .build());

        return RoleMapper.fromEntity(role);
    }

    public List<Role> validateAndGetRoles(List<String> roleNames) {
        List<Role> roles = roleRepository.findByNameIn(roleNames);
        if (CollectionUtils.isEmpty(roles)) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.ROLES_NOT_FOUND)
                    .addErrorInformation("roles", roleNames)
                    .build();
        }
        return roles;
    }

    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream().filter(r -> !r.getName().equals("ADMIN"))
                .map(RoleMapper::fromEntityWithoutPermission)
                .toList();
    }

    public boolean isRoleExists(String roleName) {
        return roleRepository.existsByName(roleName);
    }

    public boolean isRoleExistsByRoleId(String roleId) {
        return roleRepository.existsById(roleId);
    }

    public RoleDTO assignPermissionsToRole(String roleId, List<String> permissions) {
        List<Permission> permissionList = permissionService.validateAndGetPermissions(permissions);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> KitchenSinkException
                        .builder(ApplicationErrorCode.ROLE_NOT_FOUND)
                        .referenceId(roleId)
                        .build());

        role.updatePermissions(permissionList);
        roleRepository.save(role);
        return RoleMapper.fromEntity(role);
    }

    @Transactional
    public void assignRoleToUser(User user, String roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            return;
        }
        user.updateRoles(List.of(role.get()));
    }
}
