package com.studentinformation.controller;

import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/login")
    public String goLogin() {
        return "member/login";
    }

    @GetMapping("/member/password")
    public String goPassword() {
        return "member/password";
    }

    @GetMapping("/member/register")
    public String goRegister() {
        return "member/register";
    }

    @GetMapping("/member/findPassword")
    public String goFindPassword() {
        return "member/findPassword";
    }
}
