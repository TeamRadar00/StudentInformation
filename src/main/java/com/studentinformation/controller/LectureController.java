package com.studentinformation.controller;

import com.studentinformation.domain.Member;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    private void addTestData(Model model, String name) {
        MemberForm professor = MemberForm.createMemberForm(memberService.findByMemberNum("123"));
        model.addAttribute(name, List.of(new LectureForm("c언어", professor,
                "2022/2", Week.MONDAY, OffsetTime.of(LocalTime.now(), ZoneOffset.ofHours(2)), 20)));
    }


    @GetMapping("/lectures/my")
    public String goMyLecture(Model model) {
        addTestData(model, "myLectures");
        return "lectures/myLecture";
    }

    @GetMapping("/lectures/opened")
    public String goOpenedLecture(@ModelAttribute SearchLectureForm form) { return "lectures/openedLecture"; }

    @PostMapping("/lectures/opened")
    public String searchLecture(@ModelAttribute SearchLectureForm form , Model model) {
        addTestData(model, "lectureList");
        log.info("SearchLectureForm = {}", form);
        return "lectures/openedLecture";
    }

    @GetMapping("/lecture/applicationPage")
    public String goApplicationPage() {
        return "lecture/applicationPage";
    }


    @GetMapping("/lecture/CRUDLecture")
    public String goCRUDLecture() {
        return "lecture/CRUDLecture";
    }

}
