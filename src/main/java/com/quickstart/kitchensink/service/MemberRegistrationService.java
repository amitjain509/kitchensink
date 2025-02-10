package com.quickstart.kitchensink.service;

import com.quickstart.kitchensink.dto.MemberDTO;
import com.quickstart.kitchensink.model.Member;
import com.quickstart.kitchensink.repository.MemberRepository;
import com.quickstart.kitchensink.request.MemberRequest;
import com.quickstart.kitchensink.request.UpdateMemberRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InvalidObjectException;
import java.security.InvalidKeyException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberRegistrationService {
    private final MemberBuilder memberBuilder;
    private final MemberRepository memberRepository;

    @Transactional
    public void register(MemberRequest memberRequest) throws Exception {
        if (Objects.isNull(memberRequest)) {
            throw new InvalidObjectException("Invalid object received while registering member");
        }
        log.info("Registering with password : {}" + memberRequest.getName(), memberRequest.getPassword());
        Member member = Member.of(memberRequest);
        memberRepository.save(member);
    }

    public List<MemberDTO> getAllMembersOrderByNameAsc() {
        List<Member> members = memberRepository.findAllByOrderByNameAsc();
        return memberBuilder.convertMembers(members);
    }

    public MemberDTO getMemberByMemberId(String id) throws InvalidKeyException {
        if (!StringUtils.hasLength(id)) {
            throw new InvalidKeyException("Invalid memberId");
        }

        Optional<Member> memberOptional = memberRepository.findById(id);
        return memberOptional.map(MemberDTO::of).orElse(null);
    }

    public boolean deleteMemberByEmailId(String email) throws InvalidKeyException {
        if (!StringUtils.hasLength(email)) {
            throw new InvalidKeyException("Invalid memberId");
        }

        long deletedCount = memberRepository.deleteByEmail(email);
        return deletedCount > 0;
    }

    public void updateMemberDetails(String email, UpdateMemberRequest memberRequest) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        member.update(memberRequest);
        memberRepository.save(member);
    }

    public boolean isMemberExistByEmailId(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Member getMemberByEmail(String email) {
        return Objects.requireNonNull(memberRepository.findByEmail(email).orElseThrow());
    }
}
