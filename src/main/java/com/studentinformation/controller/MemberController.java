package com.studentinformation.controller;

import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.form.ChangePasswordForm;
import com.studentinformation.domain.form.LoginMemberForm;
import com.studentinformation.domain.form.MemberForm;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ModelAttribute("memberStates")
    public MemberState[] memberStates() { return MemberState.values(); }

    @GetMapping("/members/login")
    public String goLogin(@ModelAttribute LoginMemberForm form) { return "members/login"; }

    @PostMapping("/members/login")
    public String login(@ModelAttribute LoginMemberForm form) {
        log.info("MemberForm = {}", form);
        return "members/login";
    }

    @GetMapping("/members/password")
    public String goPassword(@ModelAttribute ChangePasswordForm form) {
        return "members/password";
    }

    @PostMapping("/members/password")
    public String changePassword(@ModelAttribute ChangePasswordForm form) {
        log.info("ChangePasswordForm = {}", form);
        return "members/password";
    }

    @GetMapping("/members/find-member")
    public String goFindPassword(@ModelAttribute MemberForm form) {
        return "members/findMember";
    }

    @PostMapping("/members/find-id")
    public String findId(@ModelAttribute MemberForm form) {
        log.info("MemberForm = {}", form);
        return "members/findMember";
    }

    @PostMapping("/members/find-password")
    public String findPassword(@ModelAttribute MemberForm form) {
        log.info("MemberForm = {}", form);
        return "members/findMember";
    }

    // admin 따로 뺄지 말진 잘 모르겠음
    @GetMapping("/admin")
    public String goRegister(@ModelAttribute MemberForm form) {
        return "members/admin";
    }

    @PostMapping("/admin")
    public String register(@ModelAttribute MemberForm form) {
        log.info("MemberForm = {}", form);
        return "members/admin";
    }
}
