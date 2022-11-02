package com.studentinformation.controller;

import com.studentinformation.domain.*;
import com.studentinformation.web.argumentResolver.Login;
import com.studentinformation.web.form.lecture.LectureForm;
import com.studentinformation.service.ApplicationService;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    @GetMapping("/applications")
    public String goApplicationPage(@Login Member member, RedirectAttributes ra, Pageable pageable, Model model) {
        Page<Lecture> remainLecture = lectureService.findRemainLecture(member, pageable);
        Page<LectureForm> lectureFormList = remainLecture.map(LectureForm::of);
        model.addAttribute("lectureList", lectureFormList);
        return "applications/application";
    }

    @GetMapping("/applications/{lectureId}/new")
    public String application(@Login Member member, RedirectAttributes ra, @PathVariable Long lectureId) {
        Member findMember = memberService.findById(member.getId());
        Lecture selectLecture = lectureService.findByLectureId(lectureId);
        if (selectLecture == null) {
            ra.addFlashAttribute("msg", "잘못된 접근입니다!");
            return "redirect:/home";
        } else {
            try {
                applicationService.saveApplication(new Application(findMember, selectLecture));
                ra.addFlashAttribute("msg", selectLecture.getLectureName()+" 강좌 신청이 완료됐습니다.");
            } catch (IllegalArgumentException e) {
                ra.addFlashAttribute("msg", "수강 제한 인원이 초과되었습니다.");
            }
        }
        return "redirect:/applications";
    }

}
