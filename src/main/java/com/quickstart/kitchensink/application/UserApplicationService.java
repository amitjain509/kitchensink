package com.quickstart.kitchensink.application;

import com.quickstart.kitchensink.dto.request.user.UserCreateRequest;
import com.quickstart.kitchensink.dto.request.user.UserUpdateRequest;
import com.quickstart.kitchensink.dto.response.BasicRoleDTO;
import com.quickstart.kitchensink.dto.response.RoleDTO;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.mapper.UserMapper;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.service.RoleService;
import com.quickstart.kitchensink.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserApplicationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.password}")
    private String defaultPassword;

    @Transactional
    public UserDTO createUser(UserCreateRequest request) {
        log.info(">>>[UserApplicationService::createUser] creating user with email : {}", request.getEmail());

        UserDTO userDTO = UserMapper.fromCreateRequest(request);
        List<String> roleIds = getRoleIdsIfExists(userDTO);
        List<Role> roles = roleService.validateAndGetRolesByIds(roleIds);

        String defaultPasswordHash = passwordEncoder.encode(defaultPassword);
        UserDTO createdUserDTO = userService.createUser(userDTO, roles, defaultPasswordHash);

        log.info(">>>[UserApplicationService::createUser] created user with email : {}", request.getEmail());

        return createdUserDTO;
    }

    @Transactional
    public UserDTO updateUser(UserUpdateRequest userRequest) {
        log.info(">>>[UserApplicationService::updateUser] received user update request for user with email : {}", userRequest.getEmail());

        userService.validateExistingPhoneNumber(userRequest.getPhoneNumber(), userRequest.getEmail());
        log.info("[UserApplicationService::updateUser] validated user details : {}", userRequest.getEmail());

        RoleDTO roleDTO = roleService.getRole(userRequest.getRoleId());
        UserDTO userCreateDTO = UserMapper.fromUpdateRequest(userRequest, roleDTO);

        UserDTO userUpdatedDTO = userService.updateUser(userCreateDTO);

        log.info("<<<[UserApplicationService::updateUser] user details updated successfully for  : {}", userRequest.getEmail());

        return userUpdatedDTO;
    }

    @Transactional
    public void deleteUser(String userId) {
        log.info(">>>[UserApplicationService::deleteUser] deleting user : {}", userId);

        userService.deleteUser(userId);

        log.info("<<<[UserApplicationService::deleteUser] user deleted : {}", userId);
    }

    public UserDTO getUserByEmail(String email) {
        User user = userService.getUserByEmail(email);
        return UserMapper.fromEntity(user);
    }

    public List<UserDTO> findAllUsersByUserType(UserType userType) {
        return userService.findAllUsersByUserType(userType);
    }

    @Transactional
    public void lockUser(String userId) {
        log.info(">>>[UserApplicationService::lockUser] locking user : {}", userId);

        userService.lockUser(userId);

        log.info("<<<[UserApplicationService::lockUser] user locked : {}", userId);
    }

    @Transactional
    public void unlockUser(String userId) {
        log.info(">>>[UserApplicationService::lockUser] unlocking user : {}", userId);

        userService.unlockUser(userId);

        log.info(">>>[UserApplicationService::lockUser] user unlocked : {}", userId);
    }

    @Transactional
    public void assignRolesToUser(String userId, List<String> roles) {
        log.info(">>>[UserApplicationService::assignRolesToUser] assigning roles {} to user : {}", roles, userId);

        List<Role> roleList = roleService.validateAndGetRoles(roles);

        log.info("[UserApplicationService::assignRolesToUser] validated roles");
        userService.assignRolesToUser(userId, roleList);

        log.info("<<<[UserApplicationService::assignRolesToUser] roles {} assigned to user : {}", roles, userId);
    }

    private List<String> getRoleIdsIfExists(UserDTO userDTO) {
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            return List.of();
        }
        return userDTO.getRoles().stream()
                .map(BasicRoleDTO::getRoleId)
                .collect(Collectors.toList());
    }
}
