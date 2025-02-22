package com.quickstart.kitchensink.application;

import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.request.role.RolePermissionUpdateRequest;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.mapper.RoleMapper;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.service.PermissionService;
import com.quickstart.kitchensink.service.RoleService;
import com.quickstart.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleApplicationService {
    private final RoleService roleService;
    private final UserService userService;
    private final PermissionService permissionService;

    public List<RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Transactional
    public RoleDTO createRole(RoleCreateRequest request) {
        RoleDTO roleDTO = RoleMapper.fromRoleCreateRequest(request);
        return roleService.createRole(roleDTO);
    }

    public RoleDTO getRole(String roleId) {
        return roleService.getRole(roleId);
    }

    @Transactional
    public void deleteRole(String roleId) {
        if (userService.isRoleAssociatedWithUser(roleId)) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.ROLE_ASSOCIATED)
                    .referenceId(roleId)
                    .build();
        }
        roleService.deleteRole(roleId);
    }

    @Transactional
    public RoleDTO assignPermissionsToRole(String roleId, RolePermissionUpdateRequest request) {
        List<Permission> permissionList = permissionService.validateAndGetPermissions(request.getPermissions());
        return roleService.assignPermissionsToRole(roleId, permissionList);
    }
}
