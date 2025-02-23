package com.quickstart.kitchensink.repository;

import com.quickstart.kitchensink.enums.UserType;
import com.quickstart.kitchensink.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByPhoneNumberAndEmailNot(String phoneNumber, String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findByUserType(UserType userType);

    boolean existsByRoles_Id(String roleId);
}
