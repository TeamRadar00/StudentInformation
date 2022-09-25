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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class ApplicationControllerTest {

    @Autowired WebApplicationContext wac;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;
    private MockMvc mock;
    private static Member student;
    private static Member professor;
    private static Lecture lecture;

    @BeforeAll
    static void createTestData() {
        student = new Member("memberTest", "memberTest", "memberTest",
                MemberState.inSchool, "memberTest");
        professor = new Member("professorTest", "professorTest", "professorTest",
                MemberState.professor, "professorTest");
        lecture = new Lecture("lectureTest1", professor, "202202", "/12:00~12:50/////", 1);
    }

    @BeforeEach
    void getMockObject() {
        mock = MockMvcBuilders.webAppContextSetup(wac).build();
        memberRepository.save(student);
        memberRepository.save(professor);
        lectureRepository.save(lecture);
    }

    @Test
    void goApplicationPage_test() throws Exception {
        //given
        String url = "/applications";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder professorSession = get(url);
        MockHttpServletRequestBuilder studentSession = get(url);

        //when
        professorSession.session(createSession(professor));
        studentSession.session(createSession(student));

        //then
        ifNoSessionThenRedirect(noSession,url);
        mock.perform(professorSession)
                .andExpect(handler().handlerType(ApplicationController.class))
                .andExpect(handler().methodName("goApplicationPage"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(studentSession)
                .andExpect(handler().handlerType(ApplicationController.class))
                .andExpect(handler().methodName("goApplicationPage"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(view().name("applications/application"));
    }

    @Test
    void application_test() throws Exception {
        //given
        String url = "/applications/" + lecture.getId() + "/new";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder professorSession = get(url);
        MockHttpServletRequestBuilder notExistLecture = get("/applications/9875467/new");
        MockHttpServletRequestBuilder application = get(url);

        //when
        professorSession.session(createSession(professor));
        notExistLecture.session(createSession(student));
        application.session(createSession(student));

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(professorSession)
                .andExpect(handler().handlerType(ApplicationController.class))
                .andExpect(handler().methodName("application"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
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

    private void ifNoSessionThenRedirect(MockHttpServletRequestBuilder noSession, String url) throws Exception {
        mock.perform(noSession)
                .andExpect(redirectedUrl("/members/login?redirectURL="+url));
    }
    private MockHttpSession createSession(Member member) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return session;
    }
}
