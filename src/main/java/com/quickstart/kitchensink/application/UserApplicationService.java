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
        UserDTO userDTO = UserMapper.fromCreateRequest(request);
        List<String> roleIds = getRoleIdsIfExists(userDTO);
        List<Role> roles = roleService.validateAndGetRolesByIds(roleIds);

        String defaultPasswordHash = passwordEncoder.encode(defaultPassword);
        return userService.createUser(userDTO, roles, defaultPasswordHash);
    }

    @Transactional
    public UserDTO updateUser(UserUpdateRequest userRequest) {
        userService.validateExistingPhoneNumber(userRequest.getPhoneNumber(), userRequest.getEmail());
        RoleDTO roleDTO = roleService.getRole(userRequest.getRoleId());
        UserDTO userCreateDTO = UserMapper.fromUpdateRequest(userRequest, roleDTO);
        return userService.updateUser(userCreateDTO);
    }

    @Transactional
    public void deleteUser(String userId) {
        userService.deleteUser(userId);
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
        userService.lockUser(userId);
    }

    @Transactional
    public void unlockUser(String userId) {
        userService.unlockUser(userId);
    }

    @Transactional
    public void assignRolesToUser(String userId, List<String> roles) {
        List<Role> roleList = roleService.validateAndGetRoles(roles);
        userService.assignRolesToUser(userId, roleList);
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
