package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.PasswordDTO;
import com.quickstart.kitchensink.dto.response.UserDTO;
import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.exception.ApplicationErrorCode;
import com.quickstart.kitchensink.exception.KitchenSinkException;
import com.quickstart.kitchensink.mapper.UserMapper;
import com.quickstart.kitchensink.model.Role;
import com.quickstart.kitchensink.model.User;
import com.quickstart.kitchensink.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDTO createUser(UserDTO userDTO, List<Role> roles) {
        validateExistingUser(userDTO);

        User user = User.toEntity(userDTO);
        user.updateRoles(roles);
        return UserMapper.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = getUserByEmail(userDTO.getEmail());
        user.updateUserDetails(userDTO);
        return UserMapper.fromEntity(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public User updatePassword(String email, String newPassword) {
        User user = getUserByEmail(email);
        user.updatePassword(newPassword);
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User resetPassword(String email) {
        User user = getUserByEmail(email);
        user.resetPassword();
        userRepository.save(user);
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> KitchenSinkException
                        .builder(ApplicationErrorCode.USER_NOT_FOUND)
                        .referenceId(email)
                        .build());
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
                .orElseThrow(() -> KitchenSinkException
                        .builder(ApplicationErrorCode.INVALID_CREDENTIALS)
                        .referenceId(email)
                        .build());
        user.updatePassword(passwordDTO.getNewPassword());
        userRepository.save(user);
    }

    @Transactional
    public void assignRolesToUser(String userId, List<Role> roles) {
        User user = userRepository.findById(userId).orElseThrow(() -> KitchenSinkException
                .builder(ApplicationErrorCode.USER_NOT_FOUND)
                .referenceId(userId)
                .build());
        user.updateRoles(roles);
        userRepository.save(user);
    }

    public boolean isRoleAssociatedWithUser(String roleId) {
        if (!StringUtils.hasLength(roleId)) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.INVALID_ROLE)
                    .referenceId(roleId)
                    .build();
        }
        return userRepository.existsByRoles_Id(roleId);
    }

    public boolean isMemberExistByEmailId(String email) {
        return userRepository.existsByEmail(email);
    }

    private void validateExistingUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw KitchenSinkException
                    .builder(ApplicationErrorCode.USER_EMAIL_EXISTS)
                    .referenceId(userDTO.getEmail())
                    .build();
        }
    }
}
