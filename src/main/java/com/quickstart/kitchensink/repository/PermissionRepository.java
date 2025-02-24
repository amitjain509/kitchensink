package com.quickstart.kitchensink.repository;

import com.quickstart.kitchensink.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByName(String name);

    List<Permission> findByNameIn(List<String> permissions);
}
