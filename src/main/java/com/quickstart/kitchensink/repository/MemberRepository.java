package com.quickstart.kitchensink.repository;

import com.quickstart.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    boolean existsByEmail(String email);

    List<Member> findAllByOrderByNameAsc();

    boolean existsByEmailAndPassword(String email, String password);
}