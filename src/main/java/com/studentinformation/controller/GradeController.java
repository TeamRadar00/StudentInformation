package com.studentinformation.controller;

import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.form.GoMyGradeForm;
import com.studentinformation.domain.form.MemberForm;
import com.studentinformation.domain.form.SubmitObjectionForm;
import com.studentinformation.domain.form.TotalGradeForm;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.session.SessionConst;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;
    private final MemberService memberService;


    /**
     * model.addAttribute 여러번 사용하지말고 form으로 변환해서 한번에 보내기
     */
    @GetMapping("/grade/myGrade")
    public String goMyGrade(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = memberService.getMemberFromSession(session);
        model.addAttribute("myGrade", GoMyGradeForm.of(member));
        return "grade/myGrade";
    }

    @GetMapping("/grade/objection")
    public String goObjection(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = memberService.getMemberFromSession(session);
        model.addAttribute("gradeList",member.getGrades());
        return "grade/objection";
    }

    @PostMapping("/grade/objection")
    public String submitObjection(@ModelAttribute SubmitObjectionForm form) {
        Grade grade = gradeService.findGradeById(form.getGradeId());
        grade.updateObjection(form.getGradeObjection());
        return "redirect:/grade/objection";
    }

    @GetMapping("/grade/objectionList")
    public String goObjectionList() {
        return "grade/objectionList";
    }

    @GetMapping("/grade/giveGrade")
    public String goGiveGrade() {
        return "grade/giveGrade";
    }

    @GetMapping("/grade/graduateGrade")
    public String goGraduateGrade() {
        return "grade/graduateGrade";
    }

}
