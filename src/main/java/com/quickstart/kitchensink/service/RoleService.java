package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.mapper.RoleMapper;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

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

    public List<Role> validateAndGetRolesByIds(List<String> roleIds) {
        List<Role> roles = roleRepository.findAllById(roleIds);
        if (CollectionUtils.isEmpty(roles)) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.ROLES_NOT_FOUND)
                    .addErrorInformation("roles", roleIds)
                    .build();
        }
        return roles;
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

    public RoleDTO assignPermissionsToRole(String roleId, List<Permission> permissions) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> KitchenSinkException
                        .builder(ApplicationErrorCode.ROLE_NOT_FOUND)
                        .referenceId(roleId)
                        .build());

        role.updatePermissions(permissions);
        roleRepository.save(role);
        return RoleMapper.fromEntity(role);
    }
}
