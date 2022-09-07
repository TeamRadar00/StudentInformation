package com.studentinformation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.studentinformation.domain.*;
import com.studentinformation.domain.form.*;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.session.SessionConst;
import com.sun.net.httpserver.HttpServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.SessionIdGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.security.PrivateKey;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;
    private final MemberService memberService;
    private final LectureService lectureService;

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
    public String goGiveGrade(Model model,HttpServletRequest request,
                              @RequestParam(value = "lectureId",required = false) Long lectureId) {
        HttpSession session = request.getSession();
        Member professor = memberService.getMemberFromSession(session);
        if(lectureId != null){
            Lecture lecture = lectureService.findByLectureId(lectureId);
            List<Member> student = lecture.getGrades().stream()
                            .map(Grade::getStudent)
                            .collect(Collectors.toList());
            session.setAttribute(SessionConst.Grade_List,lecture.getGrades());
            model.addAttribute("studentList",student);
        }
        model.addAttribute("lectureList",professor.getProfessorLectures());
        return "grade/giveGrade";
    }

    @PostMapping("/grade/giveGrade")
    public String submitGiveGrade(HttpServletRequest request,
                                  @RequestParam("gradeScore") List<Score> scoreList) {
        HttpSession session = request.getSession();
        List<Grade> gradeList = (List<Grade>) session.getAttribute(SessionConst.Grade_List);
        session.removeAttribute(SessionConst.Grade_List);
        for(int i = 0 ; i < gradeList.size(); i++){
            Grade grade = gradeList.get(i);
            gradeService.editGradeOfScore(grade.getId(),scoreList.get(i));
        }
        return "grade/giveGrade";
    }

    @GetMapping("/grade/graduateGrade")
    public String goGraduateGrade() {
        return "grade/graduateGrade";
    }

}
