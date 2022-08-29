package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.form.LoginMemberForm;
import com.studentinformation.domain.form.MemberForm;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @PostConstruct
    public void addTestEntity() {
        memberService.addMember(new Member("123", "password", "choi", MemberState.inSchool, "공대"));
    }

    @GetMapping("/")
    public String goHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(@Login Member loginMember, Model model) {
        if(loginMember == null) return "members/login";

        //세션 로직 추가 (만약 로그인이 되었으면 화면에 뿌리고 안 되었으면 로그인 창으로 이동)
        Member findMember = memberService.findByMemberNum("123");
        MemberForm form = findMember.getMemberForm();
        model.addAttribute("form", form);
        return "home";
    }


}
