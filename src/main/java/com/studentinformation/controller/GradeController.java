package com.studentinformation.controller;

import com.studentinformation.domain.*;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;

import com.studentinformation.web.form.grade.GoMyGradeForm;
import com.studentinformation.web.form.grade.GradeGoGIveGradeForm;
import com.studentinformation.web.form.grade.GradeObjectionListForm;
import com.studentinformation.web.form.grade.SubmitObjectionForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;
    private final MemberService memberService;
    private final LectureService lectureService;

    @GetMapping("/grade/myGrade")
    public String goMyGrade(Model model, @Login Member student) {
        Member member = memberService.findById(student.getId());
        model.addAttribute("myGrade", GoMyGradeForm.of(member));
        return "grade/myGrade";
    }

    @GetMapping("/grade/objection")
    public String goObjection(Model model, @Login Member student) {
        Member member = memberService.findById(student.getId());
        model.addAttribute("gradeList",member.getGrades());
        return "grade/objection";
    }

    @PostMapping("/grade/objection")
    public String submitObjection(@ModelAttribute SubmitObjectionForm form) {

        gradeService.editGradeOfObjection(form.getGradeId(),form.getGradeObjection());
        return "redirect:/grade/objection";
    }

    /**
     * 접근하는 gradeId가 objection이 없으면 돌아가기
     * gradeId로 접속하는 멤버가 grade를 가지고있는 교수여야함
     */
    @GetMapping("/grade/readObjection/{gradeId}")
    public String readObjection(@PathVariable("gradeId")Long gradeId,Model model){
        Grade grade = gradeService.findGradeById(gradeId);
        if (!StringUtils.hasText(grade.getObjection())){
            return "redirect:/home";
        }
        model.addAttribute("grade",grade);
        return "grade/readObjection";
    }

    @PostMapping("/grade/readObjection/{gradeId}")
    public String editScoreThroughObjectionList(@PathVariable("gradeId")Long gradeId,
                                                @RequestParam("gradeScore") Score score){
        gradeService.editGradeOfScore(gradeId,score);
        return "redirect:/grade/objectionList";
    }

    @GetMapping("/grade/objectionList")
    public String goObjectionList(Model model, @Login Member professor,
                                  @RequestParam(required = false,value = "lectureId") Long lectureId) {
        Member member = memberService.findById(professor.getId());
        model.addAttribute("getObjectionListForm", GradeObjectionListForm.of(member));
        if(lectureId != null){
            if(lectureService.checkInaccessibleLectureWithProfessor(member, lectureId)){
                return "redirect:/home";
            }

            return "redirect:/grade/objectionList/"+ lectureId;
        }
        return "grade/objectionList";
    }

    /**
     * 나중에 기능들 분리할 수 있으면 분리할 예정
     */
    @GetMapping("/grade/objectionList/{lectureId}")
    public String getObjectionList(@PathVariable("lectureId")Long selectLectureId,
                                   @RequestParam(value = "gradeId",required = false) Long gradeId,
                                   @RequestParam(value = "lectureId",required = false) Long newLectureId,
                                   Pageable pageable,
                                   Model model){
        if(gradeId != null){
            return "redirect:/grade/readObjection/"+gradeId;
        }
        if(newLectureId !=null){
            return "redirect:/grade/objectionList/"+ newLectureId;
        }
        Page<Grade> allExistObjection = gradeService.findAllExistObjection(selectLectureId, pageable);
        Lecture selectLecture = lectureService.findByLectureId(selectLectureId);
        model.addAttribute("getObjectionListForm", GradeObjectionListForm.of(selectLecture,allExistObjection));
        return "grade/objectionList";
    }

    @GetMapping("/grade/giveGrade")
    public String goGiveGrade(Model model,
                              @RequestParam(value = "lectureId",required = false) Long lectureId,
                              @Login Member member) {

        Member professor = memberService.findById(member.getId());
        GradeGoGIveGradeForm form;
        if(lectureId != null){
            if(lectureService.checkInaccessibleLectureWithProfessor(professor,lectureId)){
                return "redirect:/home";
            }

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
                                  @RequestParam(value = "lectureId",required = false) Long lectureId,
                                  @Login Member member){

        Member professor = memberService.findById(member.getId());

        if(lectureId != null) {
            if(lectureService.checkInaccessibleLectureWithProfessor(professor,lectureId)){
                return "redirect:/home";
            }

            gradeService.editGradeListOfScore(lectureId, scoreList);
        }
        return "redirect:/grade/giveGrade";
    }

    @GetMapping("/grade/graduateGrade")
    public String goGraduateGrade() {
        return "grade/graduateGrade";
    }
}
