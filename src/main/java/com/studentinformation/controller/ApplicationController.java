package com.studentinformation.controller;

import com.studentinformation.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;


    @GetMapping("/applications")
    public String goApplicationPage() {
        return "applications/application";
    }

}
