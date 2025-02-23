package com.quickstart.kitchensink.application;

import com.quickstart.kitchensink.dto.request.role.RoleCreateRequest;
import com.quickstart.kitchensink.dto.request.role.RolePermissionUpdateRequest;
import com.quickstart.kitchensink.dto.response.BasicRoleDTO;
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

    public List<BasicRoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }

    @Transactional
    public BasicRoleDTO createRole(RoleCreateRequest request) {
        log.info(">>>[RoleApplicationService::createRole] creating role : {}", request.getRoleName());

        BasicRoleDTO roleDTO = RoleMapper.fromRoleCreateRequest(request);
        BasicRoleDTO basicRoleDTO = roleService.createRole(roleDTO);

        log.info("<<<[RoleApplicationService::createRole] role created : {}", request.getRoleName());
        return basicRoleDTO;
    }

    public RoleDTO getRole(String roleId) {
        return roleService.getRole(roleId);
    }

    @Transactional
    public void deleteRole(String roleId) {
        log.info(">>>[RoleApplicationService::deleteRole] deleting role : {}", roleId);

        if (userService.isRoleAssociatedWithUser(roleId)) {
            log.info("<<<[RoleApplicationService::deleteRole] role {} already exists", roleId);
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.ROLE_ASSOCIATED)
                    .referenceId(roleId)
                    .build();
        }
        roleService.deleteRole(roleId);
        log.info("<<<[RoleApplicationService::deleteRole] role {} deleted", roleId);
    }

    @Transactional
    public RoleDTO assignPermissionsToRole(String roleId, RolePermissionUpdateRequest request) {
        log.info(">>>[RoleApplicationService::assignPermissionsToRole] assigning permissions {} to role : {}",
                request.getPermissions(), roleId);

        List<Permission> permissionList = permissionService.validateAndGetPermissions(request.getPermissions());
        RoleDTO roleDTO = roleService.assignPermissionsToRole(roleId, permissionList);

        log.info("<<<[RoleApplicationService::assignPermissionsToRole] permissions {} linked to role : {} successfully",
                request.getPermissions(), roleId);
        return roleDTO;
    }
}
