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

    @GetMapping("/")
    public String goHome() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, @Login Member loginMember) {

        MemberForm form = MemberForm.of(loginMember);
        model.addAttribute("form", form);
        return "home";
    }


}
