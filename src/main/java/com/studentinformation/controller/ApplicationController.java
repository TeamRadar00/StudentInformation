package com.studentinformation.controller;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import com.studentinformation.web.form.lecture.LectureForm;
import com.studentinformation.service.ApplicationService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final MemberService memberService;
    private final LectureService lectureService;
    private final ApplicationService applicationService;


    @ModelAttribute("week")
    public Week[] week() {
        return Week.values();
    }

    private void addTestLectureList(Model model, String name) {
        Member professor = memberService.findByMemberNum("123");
        Lecture lecture1 = new Lecture("c언어", professor, "2022/2",
                "~/12:00~12:50/~/13:00~13:50/~/~/~/", 20);
        Lecture lecture2 = new Lecture("운영체제", professor, "2022/2",
                "14:00~14:50/~/12:00~12:50/~/~/~/~/", 25);
        lecture1 = lectureService.makeLecture(lecture1);
        lecture2 = lectureService.makeLecture(lecture2);
        model.addAttribute(name, List.of(LectureForm.of(lecture1),LectureForm.of(lecture2)));
    }

    @GetMapping("/applications")
    public String goApplicationPage(Model model) {
        addTestLectureList(model, "lectureList");
        return "applications/application";
    }

    @GetMapping("/applications/{lectureId}/new")
    public String application(@PathVariable Long lectureId, Model model) {
        addTestLectureList(model, "lectureList");
        return "applications/application";
    }

}
