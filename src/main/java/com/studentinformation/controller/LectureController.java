package com.studentinformation.controller;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.Week;
import com.studentinformation.security.PrincipalDetails;
import com.studentinformation.web.form.lecture.CRUDLectureForm;
import com.studentinformation.web.form.lecture.LectureForm;
import com.studentinformation.web.form.lecture.SearchLectureForm;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LectureController {

    private final MemberService memberService;
    private final LectureService lectureService;
    private final LectureRepository lectureRepository;

    @ModelAttribute("semesters")
    public Map<String, String> semester() { return Map.of("1","1학기","2","2학기"); }

    @ModelAttribute("selectOne")
    public Map<String, String> selectOne() {
        return Map.of("professor", "교수님 성함으로 검색",
                "lectureName", "강의이름으로 검색");
    }

    @ModelAttribute("week")
    public Week[] week() {
        return Week.values();
    }


    @GetMapping("/lectures/my")
    public String goMyLecture(@Login Member member, Model model) {
        List<Lecture> list = lectureRepository.findMyLectures(member, "202202");
        List<LectureForm> forms = list.stream().map(LectureForm::of).collect(Collectors.toList());
        model.addAttribute("lectureList", forms);
        model.addAttribute("loginMemberName", member.getMemberName());

        return "lectures/myLecture";
    }

    @GetMapping("/lectures/opened")
    public String goOpenedLecture(@ModelAttribute SearchLectureForm form) {
        return "lectures/openedLecture";
    }

    @PostMapping("/lectures/opened")
    public String searchLecture(@Validated @ModelAttribute SearchLectureForm form,
                                BindingResult bindingResult, Model model, Pageable pageable) {
        if (bindingResult.hasErrors()) {
            return "lectures/openedLecture";
        }

        Page<Lecture> findLectures = getLectures(form, pageable);
        Page<LectureForm> lectureFormList = findLectures.map(LectureForm::of);
        model.addAttribute("lectureList", lectureFormList);

        if (lectureFormList.isEmpty()) {
            bindingResult.reject("lectureNotFound","검색 조건에 해당하는 강의가 없습니다.");
        }
        return "lectures/openedLecture";
    }

    private Page<Lecture> getLectures(SearchLectureForm form, Pageable pageable) {
        if (form.getSelectOne().equals("professor")) {
            return lectureService.findByProfessorName(form.getContent(),
                    form.getYear() + "0" + form.getSemester(), pageable);
        } else if (form.getSelectOne().equals("lectureName")) {
            return lectureService.findByLectureName(form.getContent(),
                    form.getYear() + "0" + form.getSemester(), pageable);
        }
        return null;
    }

    @GetMapping("/lectures")
    public String goCRUDLecture(@Login Member professor, @ModelAttribute("createLectureForm") CRUDLectureForm form,
                                Model model, RedirectAttributes ra) {
        List<Lecture> lectures = lectureRepository.findLecturesByProfessor(professor);
        List<LectureForm> forms = lectures.stream().map(LectureForm::of).collect(Collectors.toList());
        model.addAttribute("lectureList", forms);
        return "lectures/CRUDLecture";
    }

    @PostMapping("/lectures/new")
    public String createLecture(@Validated @ModelAttribute("createLectureForm") CRUDLectureForm form,
                                @Login Member professor, BindingResult bindingResult, RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "lectures/CRUDLecture";
        }

        professor = memberService.findById(professor.getId()); //엔티티 메니저가 관리하는 professor를 가져온다.
        Lecture newLecture = form.convertEntity(professor);

        // 동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없다.
        if (lectureRepository.findByLectureNameAndProfessorAndSemester(
                newLecture.getLectureName(), newLecture.getProfessor(), newLecture.getSemester()
                ).isPresent()) {
            ra.addFlashAttribute("msg", "동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없습니다.");
            ra.addFlashAttribute("createLectureForm", form);
            return "redirect:/lectures";
        }

        lectureService.makeLecture(newLecture);
        ra.addFlashAttribute("msg", "강의 생성이 완료됐습니다!");
        return "redirect:/lectures";
    }

    @GetMapping("/lectures/{lectureId}/edit")
    public String getEditLecture(@PathVariable long lectureId, Model model, RedirectAttributes ra) {

        Lecture findLecture = lectureService.findByLectureId(lectureId);
        if (findLecture == null) {
            ra.addFlashAttribute("msg", "잘못된 접근입니다.");
            return "redirect:/home";
        }

        model.addAttribute("editLectureForm", CRUDLectureForm.of(findLecture));
        return "lectures/editLecture";
    }

    @PostMapping("/lectures/{lectureId}/edit")
    public String editLecture(@PathVariable long lectureId, RedirectAttributes ra,
                              @Validated @ModelAttribute("editLectureForm") CRUDLectureForm form,
                              BindingResult bindingResult, Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member professor = principal.getMember();

        if (professor.getState() != MemberState.professor && professor.getState() != MemberState.admin) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        if (bindingResult.hasErrors()) {
            return "lectures/" + String.valueOf(lectureId) + "/edit";
        }

        professor = memberService.findById(professor.getId());
        Lecture newLecture = form.convertEntity(professor);

        // 동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없다.
        if (lectureRepository.findByLectureNameAndProfessorAndSemester(
                newLecture.getLectureName(), newLecture.getProfessor(), newLecture.getSemester()).isPresent()) {
            ra.addFlashAttribute("msg", "동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없습니다.");
            ra.addFlashAttribute("createLectureForm", form);
            return "redirect:/lectures";
        }

        try {
            lectureService.editLecture(lectureId, newLecture);
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("msg", "잘못된 접근입니다.");
            return "redirect:/home";
        }

        ra.addFlashAttribute("msg", "강의 수정이 완료됐습니다.");
        return "redirect:/lectures";
    }

}
