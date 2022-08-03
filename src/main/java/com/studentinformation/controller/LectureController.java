package com.studentinformation.controller;

import com.studentinformation.service.LectureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @GetMapping("/lecture/myLecture")
    public String goMyLecture() {
        return "lecture/myLecture";
    }

    @GetMapping("/lecture/applicationPage")
    public String goApplicationPage() {
        return "lecture/applicationPage";
    }

    @GetMapping("/lecture/openedLecture")
    public String goOpenedLecture() {
        return "lecture/openedLecture";
    }

    @GetMapping("/lecture/CRUDLecture")
    public String goCRUDLecture() {
        return "lecture/CRUDLecture";
    }

}
