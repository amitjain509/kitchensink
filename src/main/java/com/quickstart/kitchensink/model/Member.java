package com.quickstart.kitchensink.model;

import com.quickstart.kitchensink.request.MemberRequest;
import com.quickstart.kitchensink.request.UpdateMemberRequest;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

@Getter
@Builder
@Document(collection = "members")
public class Member implements UserDetails {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String phoneNumber;

    private String password;

    private List<String> roles;

    public static Member of(MemberRequest memberRequest) {
        return Member.builder()
                .email(memberRequest.getEmail())
                .phoneNumber(memberRequest.getPhoneNumber())
                .name(memberRequest.getName())
                .password(memberRequest.getPassword())
                .build();
    }

    public void update(UpdateMemberRequest memberRequest) {
        this.name = memberRequest.getName();
        this.phoneNumber = memberRequest.getPhoneNumber();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(CollectionUtils.isEmpty(roles)) {
            return List.of();
        }
        return roles.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
