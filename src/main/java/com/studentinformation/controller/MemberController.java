package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.form.ChangePasswordForm;
import com.studentinformation.domain.form.LoginMemberForm;
import com.studentinformation.domain.form.MemberForm;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
import com.studentinformation.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    public String login(@Validated @ModelAttribute LoginMemberForm form, BindingResult bindingResult,
                        @RequestParam(defaultValue = "/")String redirectURL,
                        HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return "members/login";
        }

        Member loginMember = memberService.login(form.getStudentNum(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "학번 또는 비밀번호가 맞지 않습니다.");
            return "members/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/members/password")
    public String goPassword(@ModelAttribute ChangePasswordForm form) {
        return "members/password";
    }

    //validation 추가해야됨
    @PostMapping("/members/password")
    public String changePassword(@Login Member member, @Validated @ModelAttribute ChangePasswordForm form,
                                 BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "members/password";
        }
        if (!form.isPasswordEqual()) {
            bindingResult.reject("incorrectPassword", "두 비밀번호가 서로 다릅니다.");
            return "members/password";
        }

        if (memberService.findPassword(member.getMemberName(), member.getStudentNum()).equals(form.getPrePassword())) {
            memberService.updatePassword(member.getId(), form.getNewPassword());
            ra.addFlashAttribute("msg", "비밀번호 변경이 완료됐습니다!");
            return "redirect:/home";
        } else {
            bindingResult.rejectValue("prePassword","passwordError","현재 비밀번호가 틀렸습니다.");
            return "members/password";
        }
    }

    @GetMapping("/members/find-member")
    public String goFindPassword(@ModelAttribute MemberForm form) {
        return "members/findMember";
    }

    @PostMapping("/members/find-id")
    public String findId(@ModelAttribute MemberForm form) {
        String studentNum = memberService.findStudentNum(form.getMemberName());
        if(StringUtils.hasText(studentNum)){
            form.updateMessage("학번: " + studentNum);
        }
        return "members/findMember";
    }

    @PostMapping("/members/find-password")
    public String findPassword(@ModelAttribute MemberForm form) {
        String password = memberService.findPassword(form.getMemberName(), form.getStudentNum());
        if(StringUtils.hasText(password)){
            form.updateMessage("비밀번호: "+ password);
        }
        return "members/findMember";
    }

    // admin 따로 뺄지 말진 잘 모르겠음
    @GetMapping("/admin")
    public String goRegister(@Login Member member, @ModelAttribute MemberForm form, RedirectAttributes ra) {
        if (member.getState().equals(MemberState.admin)) {
            return "members/admin";
        }else{
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }
    }

    @PostMapping("/admin")
    public String register(@ModelAttribute MemberForm form, RedirectAttributes ra) {
        ra.addFlashAttribute("msg", "회원가입이 완료됐습니다!");
        memberService.addMember(form.registerMember());
        return "redirect:/members/login";
    }

    @GetMapping("/members/logout")
    public String logout(HttpServletRequest request, RedirectAttributes ra) {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConst.LOGIN_MEMBER);
        ra.addFlashAttribute("msg", "로그아웃 되었습니다!");
        return "redirect:/home";
    }
}
