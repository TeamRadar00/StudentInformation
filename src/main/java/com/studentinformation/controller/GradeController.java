package com.studentinformation.controller;

import com.studentinformation.domain.*;
import com.studentinformation.domain.form.*;
import com.studentinformation.service.GradeService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
//        Grade grade = gradeService.findGradeById(form.getGradeId());
//        grade.updateObjection(form.getGradeObjection());
        gradeService.editGradeOfObjection(form.getGradeId(),form.getGradeObjection());
        return "redirect:/grade/objection";
    }

    @GetMapping("/grade/readObjection/{gradeId}")
    public String readObjection(@PathVariable("gradeId")Long id,Model model){
        Grade grade = gradeService.findGradeById(id);
        model.addAttribute("grade",grade);
        return "grade/readObjection";
    }

    @PostMapping("/grade/readObjection/{gradeId}")
    public String editScoreThroughObjectionList(@PathVariable("gradeId")Long id,
                                                @RequestParam("gradeScore") Score score){
        log.info("score={}",score);
        gradeService.editGradeOfScore(id,score);
        return "redirect:/grade/objectionList";
    }

    @GetMapping("/grade/objectionList")
    public String goObjectionList(Model model,HttpServletRequest request,
                                  @RequestParam(required = false,value = "lectureId") Long id) {

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Member professor = memberService.findById(member.getId());
        model.addAttribute("getObjectionListForm",GradeGetObjectionListForm.of(professor));
        if(id != null){
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
                                   Model model,HttpServletRequest request){
        if(gradeId != null){
            return "redirect:/grade/readObjection/"+gradeId;
        }
        if(newLectureId !=null){
            return "redirect:/grade/objectionList/"+ newLectureId;
        }
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        Page<Grade> allExistObjection = gradeService.findAllExistObjection(selectLectureId, pageable);
        Lecture selectLecture = lectureService.findByLectureId(selectLectureId);
        model.addAttribute("getObjectionListForm",GradeGetObjectionListForm.of(selectLecture,allExistObjection));
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
