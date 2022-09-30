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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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
        lecture = new Lecture("LCT_lecture", professor, "202202", "~/12:00~13:50/~/~/~/~/~/", 1);
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

        lectureNotFound.session(createSession(professor))
                .param("year", "notFound").param("semester", "2")
                .param("selectOne", "professor").param("content", "notFound");
        findLecture.session(createSession(professor))
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

    @Test
    void goCRUDLecture_test() throws Exception {
        //given
        String url = "/lectures";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder studentSession = get(url);
        MockHttpServletRequestBuilder professorSession = get(url);

        //when
        studentSession.session(createSession(student));
        professorSession.session(createSession(professor));

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goCRUDLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(professorSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goCRUDLecture"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(view().name("lectures/CRUDLecture"));
    }

    @Test
    void createLecture_test() throws Exception {
        //given
        String url = "/lectures/new";
        MockHttpServletRequestBuilder noSession = post(url);
        MockHttpServletRequestBuilder studentSession = post(url);
        MockHttpServletRequestBuilder sameLecture = post(url);
        MockHttpServletRequestBuilder differentLecture = post(url);
        //when
        studentSession.session(createSession(student));
        sameLecture.session(createSession(professor))
                .param("lectureName", "LCT_lecture").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");
        differentLecture.session(createSession(professor))
                .param("lectureName", "LCT_lecture2").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50")
                .param("lectureTimeList[1].startTime", "").param("lectureTimeList[1].endTime", "")
                .param("lectureTimeList[2].startTime", "").param("lectureTimeList[2].endTime", "")
                .param("lectureTimeList[3].startTime", "").param("lectureTimeList[3].endTime", "")
                .param("lectureTimeList[4].startTime", "").param("lectureTimeList[4].endTime", "")
                .param("lectureTimeList[5].startTime", "").param("lectureTimeList[5].endTime", "")
                .param("lectureTimeList[6].startTime", "").param("lectureTimeList[6].endTime", "")
                .param("limitNum", "12");
        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("createLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(sameLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("createLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(flash().attributeExists("createLectureForm"))
                .andExpect(redirectedUrl("/lectures"));
        mock.perform(differentLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("createLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(flash().attributeCount(1))
                .andExpect(redirectedUrl("/lectures"));
    }

    @Test
    void getEditLecture_test() throws Exception {
        //given
        String url = "/lectures/" + lecture.getId() + "/edit";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder studentSession = get(url);
        MockHttpServletRequestBuilder notFoundLecture = get("/lectures/123456789/edit");
        MockHttpServletRequestBuilder foundLecture = get(url);

        //when
        studentSession.session(createSession(student));
        notFoundLecture.session(createSession(professor));
        foundLecture.session(createSession(professor));

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("getEditLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(notFoundLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("getEditLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(foundLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("getEditLecture"))
                .andExpect(model().attributeExists("editLectureForm"))
                .andExpect(view().name("lectures/editLecture"));
    }

    @Test
    void editLecture_test() throws Exception {
        //given
        String url = "/lectures/" + lecture.getId() + "/edit";
        MockHttpServletRequestBuilder noSession = post(url);
        MockHttpServletRequestBuilder studentSession = post(url);
        MockHttpServletRequestBuilder sameLecture = post(url);
        MockHttpServletRequestBuilder notFoundLecture = post("/lectures/123456789/edit");
        MockHttpServletRequestBuilder foundLecture = post(url);

        //when
        studentSession.session(createSession(student));
        sameLecture.session(createSession(professor))
                .param("lectureName", "LCT_lecture").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");
        notFoundLecture.session(createSession(professor))
                .param("lectureName", "LCT_lecture3").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");
        foundLecture.session(createSession(professor))
                .param("lectureName", "LCT_lecture3").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().methodName("editLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(sameLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("editLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(flash().attributeExists("createLectureForm"))
                .andExpect(redirectedUrl("/lectures"));
        mock.perform(notFoundLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("editLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(flash().attributeCount(1))
                .andExpect(redirectedUrl("/home"));
        mock.perform(foundLecture)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("editLecture"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/lectures"));
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
