package com.quickstart.kitchensink.dto;

import com.quickstart.kitchensink.model.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    String memberId;
    String name;
    String email;
    String phoneNumber;
    List<String> roles;

    public static MemberDTO of(Member member) {
        return MemberDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .roles(member.getRoles())
                .build();
    }
}
