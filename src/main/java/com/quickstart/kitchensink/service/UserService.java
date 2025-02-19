package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.PasswordDTO;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.mapper.UserMapper;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        validateExistingUser(userDTO);

        User user = User.toEntity(userDTO);

        roleService.assignRoleToUser(user, getRoleIdIfExists(userDTO));
        return UserMapper.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void updateUser(UserDTO userDTO) {
        User user = getUserByEmail(userDTO.getEmail());
        user.updateUserDetails(userDTO);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public User resetPassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        user.updatePassword(newPassword);
        userRepository.save(user);
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exists"));
    }

    public List<UserDTO> findAllUsersByUserType(UserType userType) {
        return userRepository.findByUserType(userType).stream()
                .filter(u -> u.getUserType() != UserType.ADMIN)
                .map(UserMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void lockUser(String userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.lockUser();
            userRepository.save(user);
        });
    }

    @Transactional
    public void unlockUser(String userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.unlockUser();
            userRepository.save(user);
        });
    }

    @Transactional
    public void updatePassword(String email, PasswordDTO passwordDTO) {
        User user = userRepository.findByEmailAndPassword(email, passwordDTO.getOldPassword())
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("Invalid email or password"));
        user.updatePassword(passwordDTO.getNewPassword());
        userRepository.save(user);
    }

    @Transactional
    public void assignRolesToUser(String userId, List<String> roles) {
        List<Role> roleList = roleService.validateAndGetRoles(roles);
        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        user.updateRoles(roleList);
        userRepository.save(user);
    }

    public boolean isMemberExistByEmailId(String email) {
        return userRepository.existsByEmail(email);
    }

    private void validateExistingUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    private String getRoleIdIfExists(UserDTO userDTO) {
        if (CollectionUtils.isEmpty(userDTO.getRoles())) {
            return null;
        }
        return userDTO.getRoles().getFirst().getRoleId();
    }
}
