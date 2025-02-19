package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.PermissionDTO;
import com.quickstart.kitchensink.model.Permission;
import com.quickstart.kitchensink.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll()
                .stream()
                .filter(p -> !p.getName().equals("ALL"))
                .map(p -> PermissionDTO.of(p.getId(), p.getName(), p.getDescription()))
                .collect(Collectors.toList());
    }

    public List<Permission> validateAndGetPermissions(List<String> permissions) {
        if (CollectionUtils.isEmpty(permissions)) {
            return List.of();
        }
        List<Permission> permissionList = permissionRepository.findByNameIn(permissions);
        if (CollectionUtils.isEmpty(permissionList)) {
            throw new IllegalArgumentException("No valid permissions found");
        }
        return permissionList;
    }
}
