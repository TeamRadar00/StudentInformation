package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.repository.MemberRepository;
import com.studentinformation.security.PrincipalDetails;
import com.studentinformation.web.form.member.MemberForm;
import com.studentinformation.web.argumentResolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private MemberRepository memberRepository;

//    이거 지우는게 좋겠지?
//    private final MemberService memberService;
//
//    @PostConstruct
//    public void addTestEntity() {
//        memberService.addMember(new Member("123", "password", "choi", MemberState.admin, "공대"));
//    }

    @GetMapping("/")
    public String goHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model,Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member loginMember = principal.getMember();

        MemberForm form = MemberForm.of(loginMember);
        model.addAttribute("form", form);
        return "home";
    }


}
