package com.studentinformation.controller;

import com.studentinformation.domain.Week;
import com.studentinformation.domain.form.LectureForm;
import com.studentinformation.domain.form.MemberForm;
import com.studentinformation.domain.form.SearchLectureForm;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LectureController {

    private final MemberService memberService; //테스트용. 나중에 뺄거임
    private final LectureService lectureService;

    @ModelAttribute("semesters")
    public Map<String, String> semester() { return Map.of("1","1학기","2","2학기"); }

    @ModelAttribute("selectOne")
    public Map<String, String> selectOne() {
        return Map.of("professor", "교수님 성함으로 검색", "lecture", "강의이름으로 검색");
    }

    private void addTestLectureList(Model model, String name) {
        MemberForm professor = MemberForm.createMemberForm(memberService.findByMemberNum("123"));
        model.addAttribute(name, List.of(new LectureForm(1L,"c언어", professor,
                "2022/2", Week.MONDAY, OffsetTime.of(LocalTime.now(), ZoneOffset.ofHours(2)), 20)));
    }
    private void addTestLecture(Model model, String name) {
        MemberForm professor = MemberForm.createMemberForm(memberService.findByMemberNum("123"));
        model.addAttribute(name, new LectureForm(1L,"c언어", professor,
                "2022/2", Week.MONDAY, OffsetTime.of(LocalTime.now(), ZoneOffset.ofHours(2)), 20));
    }

    @GetMapping("/lectures/my")
    public String goMyLecture(Model model) {
        //List<Lecture> list = lectureService.findByStudentName("choi")로 자신의 lectureList를 받고
        //list.stream().map(LectureForm::createLectureForm).collect(Collectors.toList())로 변환 어떰?

        addTestLectureList(model, "lectureList");
        return "lectures/myLecture";
    }

    @GetMapping("/lectures/opened")
    public String goOpenedLecture(@ModelAttribute SearchLectureForm form) { return "lectures/openedLecture"; }

    @PostMapping("/lectures/opened")
    public String searchLecture(@ModelAttribute SearchLectureForm form , Model model) {
        //lectureService.search(form)로 폼을 넘길까 아님 여기서 폼을 까내서 안에 로직에 따라 findByProfessorName이나
        //findByLectureName을 호출할까?

        addTestLectureList(model, "lectureList");
        log.info("SearchLectureForm = {}", form);
        return "lectures/openedLecture";
    }

    @GetMapping("/lectures")
    public String goCRUDLecture(Model model) {
        //세션 넣으면 파라미터에 @Login Member professor 넣고 해당 교수에 따라 개설된 강의 보여주기
        addTestLectureList(model,"lectureList");
        return "lectures/CRUDLecture";
    }

    @GetMapping("/lectures/{lectureId}/edit")
    public String getEditLecture(@PathVariable long lectureId, Model model) {
//        model.addAttribute("editLecture", LectureForm.createLectureForm(lectureService.findByLectureId(lectureId)));
        addTestLecture(model, "editLecture");
        return "lectures/editLecture";
    }

    @GetMapping("/lecture/applicationPage")
    public String goApplicationPage() {
        return "lecture/applicationPage";
    }


}
