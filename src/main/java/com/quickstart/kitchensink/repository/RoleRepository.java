package com.quickstart.kitchensink.repository;

import com.quickstart.kitchensink.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    @Query(value = "{ 'name': ?0 }", delete = true)
    long deleteByName(String name);

    List<Role> findByNameIn(List<String> roles);
}
