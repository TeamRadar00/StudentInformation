package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.form.LoginMemberForm;
import com.studentinformation.service.MemberService;
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
    public String home(Model model) {
        //세션 로직 추가 (만약 로그인이 되었으면 화면에 뿌리고 안 되었으면 로그인 창으로 이동)
        Member findMember = memberService.findByMemberNum("choi");
        LoginMemberForm form = findMember.getLoginMemberForm();
        model.addAttribute("form", form);
        return "home";
    }
}
