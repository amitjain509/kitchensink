package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_EDIT')")
    public ResponseEntity<?> getPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

}
