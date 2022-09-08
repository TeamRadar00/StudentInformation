package com.studentinformation.controller;

import com.studentinformation.domain.*;
import com.studentinformation.domain.form.*;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
     * 서비스에 session 걷어내기
     */
    @GetMapping("/grade/myGrade")
    public String goMyGrade(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member sessionAttributeMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long memberId = sessionAttributeMember.getId();
        Member member = memberService.findById(memberId);
        model.addAttribute("myGrade", GoMyGradeForm.of(member));
        return "grade/myGrade";
    }

    @GetMapping("/grade/objection")
    public String goObjection(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member sessionAttributeMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long memberId = sessionAttributeMember.getId();
        Member member = memberService.findById(memberId);
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
        Member sessionAttributeMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        Long memberId = sessionAttributeMember.getId();
        Member professor = memberService.findById(memberId);
        GradeGoGIveGradeForm form;
        if(lectureId != null){
            Lecture lecture = lectureService.findByLectureId(lectureId);

            form = GradeGoGIveGradeForm.makeFormWithLectureId(professor, lecture);
        }else{
            form = GradeGoGIveGradeForm.makeForm(professor);
        }
        model.addAttribute("GradeGoGiveGradeForm",form);
        return "grade/giveGrade";
    }

    @PostMapping("/grade/giveGrade")
    public String submitGiveGrade(@RequestParam(value = "gradeScore",required = false) List<Score> scoreList,
                                  @RequestParam(value = "lectureId",required = false) Long lectureId){
        if(lectureId != null) {
            gradeService.editGradeListOfScore(lectureId, scoreList);
        }
        return "redirect:/grade/giveGrade";
    }

    @GetMapping("/grade/graduateGrade")
    public String goGraduateGrade() {
        return "grade/graduateGrade";
    }
}
