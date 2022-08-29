package com.studentinformation.controller;

import com.studentinformation.domain.Grade;
import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.form.MemberForm;
import com.studentinformation.domain.form.TotalGradeForm;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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
     * 세션처리 완료되면 쿠키에 있는 정보를 이용해서 모델에 뿌려주기
     */
    @GetMapping("/grade/myGrade")
    public String goMyLecture(Model model) {
        Member member = memberService.findByMemberNum("test");
        MemberForm form = MemberForm.of(member);

        model.addAttribute("student",form);
        model.addAttribute("total", TotalGradeForm.of(member));
        model.addAttribute("gradeList",member.getGrades());
        return "grade/myGrade";
    }

    @GetMapping("/grade/objection")
    public String goObjection(Model model) {
        Member test = memberService.findByMemberNum("test");
        List<Grade> grades = test.getGrades();
        model.addAttribute("gradeList",grades);

        return "grade/objection";
    }
    @PostMapping("/grade/objection")
    public String submitObjection(@RequestParam(value = "gradeId")Long id,
                                  @RequestParam(value = "gradeObjection") String objection) {
        Grade grade = gradeService.findGradeById(id);
        grade.updateObjection(objection);
        log.info("update grade objection = {}",grade);
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
