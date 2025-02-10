package com.quickstart.kitchensink.repository;

import com.quickstart.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Member> findAllByOrderByNameAsc();

    @Query(value = "{ 'email': ?0 }", delete = true)
    long deleteByEmail(String email);

    boolean existsByEmailAndPassword(String email, String password);
}