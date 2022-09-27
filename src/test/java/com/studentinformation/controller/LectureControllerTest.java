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
public class LectureControllerTest {

    @Autowired WebApplicationContext wac;
    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;

    private MockMvc mock;
    private static Member student;
    private static Member professor;
    private static Lecture lecture;


    @BeforeAll
    static void createTestData() {
        student = new Member("LCT_member", "LCT_member", "LCT_member",
                MemberState.inSchool, "LCT_member");
        professor = new Member("LCT_professor", "LCT_professor", "LCT_professor",
                MemberState.professor, "LCT_professor");
        lecture = new Lecture("LCT_lecture", professor, "202202", "~/12:00~13:50/~/13:00~14:50/~/~/~/", 1);
    }

    @BeforeEach
    void getMockObject() {
        mock = MockMvcBuilders.webAppContextSetup(wac).build();
        memberRepository.save(student);
        memberRepository.save(professor);
        lectureRepository.save(lecture);
    }

    @Test
    void goMyLecture_test() throws Exception {
        //given
        String url = "/lectures/my";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder professorSession = get(url);
        MockHttpServletRequestBuilder studentSession = get(url);

        //when
        professorSession.session(createSession(professor));
        studentSession.session(createSession(student));

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(professorSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goMyLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(studentSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goMyLecture"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(model().attributeExists("loginMemberName"));
    }

    @Test
    void goOpenedLecture_test() throws Exception {
        //given
        String url = "/lectures/opened";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder studentSession = get(url);

        //when
        studentSession.session(createSession(student));

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goOpenedLecture"))
                .andExpect(model().attributeExists("searchLectureForm"));
    }

    @Test
    void searchLecture_test() throws Exception {
        //given
        String url = "/lectures/opened";
        MockHttpServletRequestBuilder noSession = post(url);
        MockHttpServletRequestBuilder fieldError = post(url);
        MockHttpServletRequestBuilder lectureNotFound = post(url);
        MockHttpServletRequestBuilder findLecture = post(url);

        //when
        fieldError.session(createSession(student))
                .param("year", " ").param("semester", "2")
                .param("selectOne", "professor").param("content", "");

        lectureNotFound.session(createSession(student))
                .param("year", "notFound").param("semester", "2")
                .param("selectOne", "professor").param("content", "notFound");
        findLecture.session(createSession(student))
                .param("year", "2022").param("semester", "2")
                .param("selectOne", "professor").param("content", "LCT_professor");

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(fieldError)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("searchLecture"))
                .andExpect(model().attributeHasFieldErrors("searchLectureForm", "year"))
                .andExpect(model().attributeHasFieldErrors("searchLectureForm", "content"));
        mock.perform(lectureNotFound)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("searchLecture"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(model().hasErrors());
        mock.perform(findLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("searchLecture"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(model().hasNoErrors());
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
