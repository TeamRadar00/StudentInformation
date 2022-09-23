package com.studentinformation.controller;

import com.studentinformation.domain.*;
import com.studentinformation.domain.form.*;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
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


    /**
     * model.addAttribute 여러번 사용하지말고 form으로 변환해서 한번에 보내기
     * 서비스에 session 걷어내기
     */
    @GetMapping("/grade/myGrade")
    public String goMyGrade(Model model, @Login Member student) {
        if(student.getState() != MemberState.inSchool ||student.getState() != MemberState.outSchool){
            // 접근 할 수 없음
            return "redirect:/home";
        }
        Member member = memberService.findById(student.getId());
        model.addAttribute("myGrade", GoMyGradeForm.of(member));
        return "grade/myGrade";
    }

    @GetMapping("/grade/objection")
    public String goObjection(Model model, @Login Member student) {
        if(student.getState() != MemberState.inSchool ||student.getState() != MemberState.outSchool){
            // 접근 할 수 없음
            return "redirect:/home";
        }

        Member member = memberService.findById(student.getId());
        model.addAttribute("gradeList",member.getGrades());
        return "grade/objection";
    }

    @PostMapping("/grade/objection")
    public String submitObjection(@ModelAttribute SubmitObjectionForm form, @Login Member student) {
        if(student.getState() != MemberState.inSchool ||student.getState() != MemberState.outSchool){
            // 접근 할 수 없음
            return "redirect:/home";
        }

        gradeService.editGradeOfObjection(form.getGradeId(),form.getGradeObjection());
        return "redirect:/grade/objection";
    }

    /**
     * 접근하는 gradeId가 objection이 없으면 돌아가기
     * gradeId로 접속하는 멤버가 grade를 가지고있는 교수여야함
     */
    @GetMapping("/grade/readObjection/{gradeId}")
    public String readObjection(@PathVariable("gradeId")Long id,Model model,
                                @Login Member professor){
        if (!professor.getState().equals(MemberState.professor)){
            // 접근 할 수 없음
            return "redirect:/home";
        }

        boolean access = professor.getGrades().stream()
                .anyMatch(grade -> grade.getId().equals(id));
        if(!access){
            return "redirect:/home";
        }

        Grade grade = gradeService.findGradeById(id);
        if (!StringUtils.hasText(grade.getObjection())){
            return "redirect:/home";
        }
        model.addAttribute("grade",grade);
        return "grade/readObjection";
    }

    @PostMapping("/grade/readObjection/{gradeId}")
    public String editScoreThroughObjectionList(@PathVariable("gradeId")Long id,
                                                @RequestParam("gradeScore") Score score,
                                                @Login Member professor){
        if (!professor.getState().equals(MemberState.professor)){
            // 접근 할 수 없음
            return "redirect:/home";
        }

        boolean access = professor.getGrades().stream()
                .anyMatch(grade -> grade.getId().equals(id));
        if(!access){
            return "redirect:/home";
        }

        gradeService.editGradeOfScore(id,score);
        return "redirect:/grade/objectionList";
    }

    @GetMapping("/grade/objectionList")
    public String goObjectionList(Model model, @Login Member professor,
                                  @RequestParam(required = false,value = "lectureId") Long id) {
        if (!professor.getState().equals(MemberState.professor)){
            // 접근 할 수 없음
            return "redirect:/home";
        }
        Member member = memberService.findById(professor.getId());
        model.addAttribute("getObjectionListForm", GradeObjectionListForm.of(member));
        if(id != null){

            boolean access = professor.getProfessorLectures().stream()
                    .anyMatch(lecture -> lecture.getId().equals(id));
            if(!access){
                return "redirect:/home";
            }

            return "redirect:/grade/objectionList/"+id;
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
                                   Model model,@Login Member professor){
        if (!professor.getState().equals(MemberState.professor)){
            // 접근 할 수 없음
            return "redirect:/home";
        }

        boolean access = professor.getProfessorLectures().stream()
                .anyMatch(lecture -> lecture.getId().equals(selectLectureId) || lecture.getId().equals(newLectureId));
        if(!access){
            return "redirect:/home";
        }

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
    public String goGiveGrade(Model model,@Login Member professor,
                              @RequestParam(value = "lectureId",required = false) Long lectureId) {
        if (!professor.getState().equals(MemberState.professor)){
            // 접근 할 수 없음
            return "redirect:/home";
        }


        Member member = memberService.findById(professor.getId());
        GradeGoGIveGradeForm form;
        if(lectureId != null){

            boolean access = professor.getProfessorLectures().stream()
                    .anyMatch(lecture -> lecture.getId().equals(lectureId));
            if(!access){
                return "redirect:/home";
            }

            Lecture lecture = lectureService.findByLectureId(lectureId);

            form = GradeGoGIveGradeForm.makeFormWithLectureId(member, lecture);
        }else{
            form = GradeGoGIveGradeForm.makeForm(member);
        }
        model.addAttribute("GradeGoGiveGradeForm",form);
        return "grade/giveGrade";
    }

    @PostMapping("/grade/giveGrade")
    public String submitGiveGrade(@RequestParam(value = "gradeScore",required = false) List<Score> scoreList,
                                  @RequestParam(value = "lectureId",required = false) Long lectureId,
                                  @Login Member professor){
        if (!professor.getState().equals(MemberState.professor)){
            // 접근 할 수 없음
            return "redirect:/home";
        }
        if(lectureId != null) {

            boolean access = professor.getProfessorLectures().stream()
                    .anyMatch(lecture -> lecture.getId().equals(lectureId));
            if(!access){
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
