package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.security.PrincipalDetails;
import com.studentinformation.web.form.member.ChangePasswordForm;
import com.studentinformation.web.form.member.LoginMemberForm;
import com.studentinformation.web.form.member.MemberForm;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
import com.studentinformation.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
    public String goLogin(@ModelAttribute LoginMemberForm form) {

        return "members/login";
    }

//    @PostMapping("/members/login")
//    public String login(@Validated @ModelAttribute LoginMemberForm form, BindingResult bindingResult,
//                        @RequestParam(defaultValue = "/")String redirectURL,
//                        HttpServletRequest request) {
//
//        if (bindingResult.hasErrors()) {
//            return "members/login";
//        }
//
//        Member loginMember = memberService.login(form.getStudentNum(), form.getPassword());
//
//        if (loginMember == null) {
//            bindingResult.reject("loginFail", "학번 또는 비밀번호가 맞지 않습니다.");
//            return "members/login";
//        }
//
//        HttpSession session = request.getSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//        return "redirect:" + redirectURL;
//    }

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
    public String goFindPassword(Model model) {
        model.addAttribute("findIdForm", new MemberForm());
        model.addAttribute("findPasswordForm", new MemberForm());
        return "members/findMember";
    }

    @PostMapping("/members/find-id")
    public String findId(@ModelAttribute("findIdForm") MemberForm form, BindingResult bindingResult, Model model) {
        model.addAttribute("findPasswordForm", new MemberForm());
        if (!StringUtils.hasText(form.getMemberName())) {
            bindingResult.rejectValue("memberName","NotBlack");
            return "members/findMember";
        }

        try {
            String studentNum = memberService.findStudentNum(form.getMemberName());
            form.updateMessage("학번: " + studentNum);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("findIdFail","존재하지 않는 이름입니다.");
        }
        return "members/findMember";
    }

    @PostMapping("/members/find-password")
    public String findPassword(@ModelAttribute("findPasswordForm") MemberForm form, BindingResult bindingResult,
                               Model model) {
        model.addAttribute("findIdForm", new MemberForm());
        if (!StringUtils.hasText(form.getMemberName())) {
            bindingResult.rejectValue("memberName","NotBlack");
        }
        if (!StringUtils.hasText(form.getStudentNum())) {
            bindingResult.rejectValue("studentNum","NotBlack");
        }
        if (bindingResult.hasErrors()) {
            return "members/findMember";
        }
        try {
            String password = memberService.findPassword(form.getMemberName(), form.getStudentNum());
            form.updateMessage("비밀번호: " + password);
        } catch (IllegalArgumentException e) {
            bindingResult.reject("findPasswordFail", "이름이나 학번이 올바르지 않습니다.");
        }
        return "members/findMember";
    }

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
    public String register(@Login Member member, @ModelAttribute MemberForm form,
                           BindingResult bindingResult, RedirectAttributes ra) {
        if (!member.getState().equals(MemberState.admin)) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        if (validateRegister(form, bindingResult)) {
            return "members/admin";
        }
        // 학번이 동일한 경우 같은 학생으로 간주한다.
        try {
            memberService.findByMemberNum(form.getStudentNum());
            bindingResult.reject("duplicateMember", "이미 존재하는 회원입니다.");
            return "members/admin";
        } catch (IllegalArgumentException e) {}


        ra.addFlashAttribute("msg", "회원가입이 완료됐습니다!");
        memberService.addMember(form.registerMember());
        return "redirect:/members/login";
    }

//    @GetMapping("/members/logout")
    public String logout(HttpServletRequest request, RedirectAttributes ra) {
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConst.LOGIN_MEMBER);
        ra.addFlashAttribute("msg", "로그아웃 되었습니다!");
        return "redirect:/home";
    }

    private boolean validateRegister(MemberForm form, BindingResult bindingResult) {
        if (!StringUtils.hasText(form.getStudentNum())) {
            bindingResult.rejectValue("studentNum","NotBlack");
        }
        if (!StringUtils.hasText(form.getMemberName())) {
            bindingResult.rejectValue("memberName","NotBlack");
        }
        if (!StringUtils.hasText(form.getCollegeName())) {
            bindingResult.rejectValue("collegeName","NotBlack");
        }
        return bindingResult.hasErrors();
    }
}
