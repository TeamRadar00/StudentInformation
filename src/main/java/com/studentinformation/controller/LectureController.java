package com.studentinformation.controller;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.domain.Week;
import com.studentinformation.domain.form.LectureForm;
import com.studentinformation.domain.form.SearchLectureForm;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LectureController {

    private final MemberService memberService; //테스트용. 나중에 뺄거임
    private final LectureService lectureService;
    private final LectureRepository lectureRepository;  //https://www.inflearn.com/questions/15024 단순 엔티티 참고는 넣을까..?

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

    @GetMapping("/lectures/my")
    public String goMyLecture(@Login Member member, Model model, RedirectAttributes ra) {
        //재학생만 가능하도록 바꿀까?
        if (!(member.getState() != MemberState.inSchool || member.getState() != MemberState.outSchool)) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        List<Lecture> list = lectureRepository.findLecturesByStudentId(member.getId());
        List<LectureForm> forms = list.stream().map(LectureForm::of).collect(Collectors.toList());
        model.addAttribute("lectureList", forms);

        return "lectures/myLecture";
    }

    @GetMapping("/lectures/opened")
    public String goOpenedLecture(@ModelAttribute SearchLectureForm form) {

        return "lectures/openedLecture";
    }

    @PostMapping("/lectures/opened")
    public String searchLecture(@ModelAttribute SearchLectureForm form , Model model) {
        //lectureService.search(form)로 폼을 넘길까 아님 여기서 폼을 까내서 안에 로직에 따라 findByProfessorName이나
        //findByLectureName을 호출할까?
        Page<Lecture> findLectures = null;

        if (form.getSelectOne().equals("professor")) {
            findLectures = lectureService.findByProfessorName(form.getContent(),
                    form.getYear() + "0" + form.getSemester(), PageRequest.of(0,10));
        } else if (form.getSelectOne().equals("lectureName")) {
            findLectures = lectureService.findByLectureName(form.getContent(),
                    form.getYear() + "0" + form.getSemester(), PageRequest.of(0,10));
        }
        Page<LectureForm> lectureFormList = findLectures.map(LectureForm::of);
        model.addAttribute("lectureList", lectureFormList);
        return "lectures/openedLecture";
    }

    @GetMapping("/lectures")
    public String goCRUDLecture(@Login Member professor, @ModelAttribute("createLecture") LectureForm form,
                                Model model, RedirectAttributes ra) {
        if (professor.getState() != MemberState.professor) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        List<Lecture> lectures = lectureRepository.findLecturesByProfessor(professor);
        List<LectureForm> forms = lectures.stream().map(LectureForm::of).collect(Collectors.toList());
        model.addAttribute("lectureList", forms);
        return "lectures/CRUDLecture";
    }

    @PostMapping("/lectures/new")
    public String createLecture(@Login Member professor, @ModelAttribute("createLecture") LectureForm lectureForm,
                                RedirectAttributes ra) {
        if (professor.getState() != MemberState.professor) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        professor = memberService.findById(professor.getId()); //엔티티 메니저가 관리하는 professor를 가져온다.
        Lecture newLecture = lectureForm.convertEntity(professor);

        // 동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없다.
        if (lectureRepository.findByLectureNameAndProfessorAndSemester(
                newLecture.getLectureName(), newLecture.getProfessor(), newLecture.getSemester()).isPresent()) {
            ra.addFlashAttribute("msg", "동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없습니다.");
            ra.addFlashAttribute("createLecture", lectureForm);
            return "redirect:/lectures";
        }

        lectureService.makeLecture(newLecture);
        ra.addFlashAttribute("msg", "강의 생성이 완료됐습니다!");
        return "redirect:/lectures";
    }

    @GetMapping("/lectures/{lectureId}/edit")
    public String getEditLecture(@Login Member professor, @PathVariable long lectureId,
                                 Model model, RedirectAttributes ra) {
        if (professor.getState() != MemberState.professor) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        model.addAttribute("editLecture", LectureForm.of(lectureService.findByLectureId(lectureId)));
        return "lectures/editLecture";
    }

    @PostMapping("/lectures/{lectureId}/edit")
    public String editLecture(@Login Member professor, @PathVariable long lectureId,
                              @ModelAttribute("editLecture") LectureForm lectureForm, RedirectAttributes ra) {
        if (professor.getState() != MemberState.professor) {
            ra.addFlashAttribute("msg", "권한이 없습니다!");
            return "redirect:/home";
        }

        professor = memberService.findById(professor.getId());
        Lecture newLecture = lectureForm.convertEntity(professor);

        // 동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없다.
        if (lectureRepository.findByLectureNameAndProfessorAndSemester(
                newLecture.getLectureName(), newLecture.getProfessor(), newLecture.getSemester()).isPresent()) {
            ra.addFlashAttribute("msg", "동일한 교수가 같은 학기에 같은 이름의 수업을 개강할 순 없습니다.");
            ra.addFlashAttribute("createLecture", lectureForm);
            return "redirect:/lectures";
        }

        lectureService.editLecture(lectureId, newLecture);
        ra.addFlashAttribute("msg", "강의 수정이 완료됐습니다.");
        return "redirect:/lectures";
    }


}
