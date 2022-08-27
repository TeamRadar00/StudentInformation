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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.PostConstruct;

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
        Member member = createMember();
        for(int i=0;i<6;i++){
            Member professor = new Member("test" + i, "test" + i, "professor" + i, MemberState.professor, "test");
            Lecture lecture = new Lecture("test" + i, professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 20);
            Grade emptyGrade = Grade.createEmptyGrade(member, lecture);
            gradeService.saveGrade(emptyGrade);
        }
        MemberForm form = MemberForm.of(member);

        model.addAttribute("student",form);
        model.addAttribute("total", new TotalGradeForm(member));
        model.addAttribute("gradeList",member.getGrades());
        return "grade/myGrade";
    }

    private Member createMember() {
        Member member = new Member("test","test","test", MemberState.inSchool,"test");
        member.changeCreateDate();
        memberService.addMember(member);
        return member;
    }

    @GetMapping("/grade/objection")
    public String goObjection() {
        return "grade/objection";
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
