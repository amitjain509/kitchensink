package com.quickstart.kitchensink.model;

import com.quickstart.kitchensink.request.MemberRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "members")
public class Member {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    public static Member of(MemberRequest memberRequest) {
        return Member.builder()
                .email(memberRequest.getEmail())
                .phoneNumber(memberRequest.getPhoneNumber())
                .name(memberRequest.getName())
                .password(memberRequest.getPassword())
                .build();
    }
}
