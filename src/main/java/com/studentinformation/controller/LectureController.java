package com.studentinformation.controller;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import com.studentinformation.domain.form.LectureForm;
import com.studentinformation.domain.form.SearchLectureForm;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.service.LectureService;
import com.studentinformation.service.MemberService;
import com.studentinformation.web.argumentResolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                "lectureName", "강의이름으로 검색",
                "lectureId","강의번호로 검색");
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
    private void addTestLecture(Model model, String name) {
        Member professor = memberService.findByMemberNum("123");
        Lecture lecture = new Lecture("c언어", professor, "2022/2",
                "~/12:00~12:50/~/13:00~13:50/~/~/~/", 20);
        model.addAttribute(name, LectureForm.of(lecture));
    }

    @GetMapping("/lectures/my")
    public String goMyLecture(@Login Member member, Model model) {
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

        addTestLectureList(model, "lectureList");
        log.info("SearchLectureForm = {}", form);
        return "lectures/openedLecture";
    }

    @GetMapping("/lectures")
    public String goCRUDLecture(Model model) {
        //세션 넣으면 파라미터에 @Login Member professor 넣고 해당 교수에 따라 개설된 강의 보여주기
        addTestLectureList(model,"lectureList");
        model.addAttribute("createLecture", new LectureForm());
        return "lectures/CRUDLecture";
    }

    @PostMapping("/lectures/new")
    public String createLecture(@ModelAttribute LectureForm lectureForm) {
//        Member professor = memberService.findById()
//        lectureService.makeLecture(lectureForm.convertEntity(professor));
        log.info("lectureForm = {}", lectureForm);
        return "redirect:/lectures";
    }

    @GetMapping("/lectures/{lectureId}/edit")
    public String getEditLecture(@PathVariable long lectureId, Model model) {
//        model.addAttribute("editLecture", LectureForm.createLectureForm(lectureService.findByLectureId(lectureId)));
        addTestLecture(model, "editLecture");
        return "lectures/editLecture";
    }

    @PostMapping("/lectures/{lectureId}/edit")
    public String editLecture(@PathVariable long lectureId, @ModelAttribute("editLecture") LectureForm lectureForm, Model model) {
//        for (LectureForm.LectureTime lectureTime : lectureForm.getLectureTimeList()) {
//            log.info("lectureTime = {}",lectureTime);
//        }
//        Member professor = lectureService.findByLectureId(lectureId).getProfessor();
//        lectureService.editLecture(lectureId, lectureForm.convertEntity(professor));
        model.addAttribute("week", Week.values());
        return "redirect:/lectures";
    }



}
