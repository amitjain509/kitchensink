package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.MemberDTO;
import com.quickstart.kitchensink.model.Member;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MemberBuilder {

    public List<MemberDTO> convertMembers(List<Member> memberList) {
        if (CollectionUtils.isEmpty(memberList)) {
            return List.of();
        }

        return memberList.stream().map(MemberDTO::of).collect(Collectors.toList());
    }
}
