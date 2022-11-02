package com.studentinformation.controller;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.repository.MemberRepository;
import com.studentinformation.web.session.SessionConst;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

@SpringBootTest
public class ApplicationControllerTest {

    static final String BASE_URL = "/applications";

    static final String TEST_STUDENT_NUM = "student";
    static final String TEST_PROFESSOR_NUM = "professor";

    @Autowired WebApplicationContext wac;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;
    private MockMvc mock;

    @BeforeEach
    void getMockObject() {
        mock = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    @WithUserDetails(TEST_PROFESSOR_NUM)
    void goApplicationPage_fail_test() throws Exception {
        //given
        MockHttpServletRequestBuilder professorSession = get(BASE_URL);

        //when
        //then
        mock.perform(professorSession)
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/home"));
    }

    @Test
    @WithUserDetails(TEST_STUDENT_NUM)
    void goApplicationPage_test() throws Exception {
        //given
        MockHttpServletRequestBuilder studentSession = get(BASE_URL);

        //when
        //then
        mock.perform(studentSession)
                .andExpect(handler().handlerType(ApplicationController.class))
                .andExpect(handler().methodName("goApplicationPage"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(view().name("applications/application"));
    }

    @Test
    @Transactional
    @WithUserDetails(TEST_STUDENT_NUM)
    void application_test() throws Exception {
        //given
        Member professor = memberRepository.findByMemberName("professor").get();
        Lecture lecture = lectureRepository.findLecturesByProfessor(professor).get(0);
        String url = BASE_URL + "/" + lecture.getId() + "/new";
        MockHttpServletRequestBuilder notExistLecture = get(BASE_URL + "/9875467/new");
        MockHttpServletRequestBuilder application = get(url);

        //when
        //then
        mock.perform(notExistLecture)
                .andExpect(handler().handlerType(ApplicationController.class))
                .andExpect(handler().methodName("application"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(application)
                .andExpect(handler().handlerType(ApplicationController.class))
                .andExpect(handler().methodName("application"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/applications"));
    }

}
