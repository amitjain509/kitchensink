package com.quickstart.kitchensink.controller;

import com.quickstart.kitchensink.dto.MemberDTO;
import com.quickstart.kitchensink.request.MemberRequest;
import com.quickstart.kitchensink.service.MemberRegistrationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/members")
@Validated
public class MemberController {

    @Autowired
    private MemberRegistrationService memberRegistrationService;

    @GetMapping
    @ResponseBody
    ResponseEntity<List<MemberDTO>> listAllMembers() {
        List<MemberDTO> members = memberRegistrationService.getAllMembersOrderByNameAsc();
        if (members.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(members);
    }

    @GetMapping("/{id}")
    @ResponseBody
    ResponseEntity<MemberDTO> getMemberById(@NotBlank @PathVariable("id") String id) {
        try {
            MemberDTO member = memberRegistrationService.getMemberByMemberId(id);
            if (Objects.isNull(member)) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}