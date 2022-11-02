package com.studentinformation.controller;

import com.studentinformation.domain.Lecture;
import com.studentinformation.domain.Member;
import com.studentinformation.repository.LectureRepository;
import com.studentinformation.repository.MemberRepository;
import com.studentinformation.util.WithCustomMember;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LectureControllerTest extends ControllerTestSetup{

    static final String BASE_URL = "/lectures";

    @Autowired MemberRepository memberRepository;
    @Autowired LectureRepository lectureRepository;


    @Test
//    @WithMockUser
//    @WithUserDetails(TEST_PROFESSOR_NUM)  전부 사용 가능
    @WithCustomMember(TEST_PROFESSOR_NUM)
    void goMyLecture_fail_test() throws Exception {
        //given
        MockHttpServletRequestBuilder professorSession = get(BASE_URL + "/my");

        //when
        //then
        mock.perform(professorSession).andDo(print());
    }

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
    void goMyLecture_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/my");

        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goMyLecture"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(model().attributeExists("loginMemberName"));
    }

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
    void goOpenedLecture_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/opened");

        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goOpenedLecture"))
                .andExpect(model().attributeExists("searchLectureForm"));
    }

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
    void searchLecture_test() throws Exception {
        //given
        String url = BASE_URL + "/opened";
        MockHttpServletRequestBuilder fieldError = post(url);
        MockHttpServletRequestBuilder lectureNotFound = post(url);
        MockHttpServletRequestBuilder findLecture = post(url);

        //when
        fieldError
                .param("year", " ").param("semester", "2")
                .param("selectOne", "professor").param("content", "");
        lectureNotFound
                .param("year", "notFound").param("semester", "2")
                .param("selectOne", "professor").param("content", "notFound");
        findLecture
                .param("year", "2022").param("semester", "2")
                .param("selectOne", "professor").param("content", "professor");

        //then
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
    @WithCustomMember(TEST_STUDENT_NUM)
    void goCRUDLecture_student_test() throws Exception {
        //given
        MockHttpServletRequestBuilder studentSession = get(BASE_URL);

        //when
        //then
        mock.perform(studentSession)
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/home"));
    }

    @Test
    @WithCustomMember(TEST_PROFESSOR_NUM)
    void goCRUDLecture_professor_test() throws Exception {
        //given
        MockHttpServletRequestBuilder professorSession = get(BASE_URL);

        //when
        //then
        mock.perform(professorSession)
                .andExpect(handler().handlerType(LectureController.class))
                .andExpect(handler().methodName("goCRUDLecture"))
                .andExpect(model().attributeExists("lectureList"))
                .andExpect(view().name("lectures/CRUDLecture"));
    }

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
    void createLecture_studentSession_test() throws Exception {
        //given
        MockHttpServletRequestBuilder studentSession = post(BASE_URL + "/new");
        //when
        //then
        mock.perform(studentSession)
                .andExpect(status().isForbidden())
                .andExpect(forwardedUrl("/home"));
    }

    @Test
    @Transactional //넣기 싫었는데 롤백하려고 넣음
    @WithCustomMember(TEST_PROFESSOR_NUM)
    void createLecture_professorSession_test() throws Exception {
        //given
        String url = BASE_URL + "/new";
        MockHttpServletRequestBuilder sameLecture = post(url);
        MockHttpServletRequestBuilder differentLecture = post(url);
        //when
        sameLecture
                .param("lectureName", "lecture1").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");
        differentLecture
                .param("lectureName", "LCT_lecture").param("year", "2022")
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
    @WithCustomMember(TEST_PROFESSOR_NUM)
    void getEditLecture_test() throws Exception {
        //given
        Member professor = memberRepository.findByMemberName("professor").get();
        Lecture lecture = lectureRepository.findLecturesByProfessor(professor).get(0);
        String url = BASE_URL + "/" + lecture.getId() + "/edit";
        MockHttpServletRequestBuilder notFoundLecture = get(BASE_URL + "/123456789/edit");
        MockHttpServletRequestBuilder foundLecture = get(url);

        //when
        //then
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
    @Transactional //넣기 싫었는데 롤백하려고 넣음
    @WithCustomMember(TEST_PROFESSOR_NUM)
    void editLecture_test() throws Exception {
        //given
        Member professor = memberRepository.findByMemberName("professor").get();
        Lecture lecture = lectureRepository.findLecturesByProfessor(professor).get(0);
        String url = BASE_URL + "/" + lecture.getId() + "/edit";
        MockHttpServletRequestBuilder sameLecture = post(url);
        MockHttpServletRequestBuilder notFoundLecture = post("/lectures/123456789/edit");
        MockHttpServletRequestBuilder foundLecture = post(url);

        //when
        sameLecture
                .param("lectureName", "lecture1").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");
        notFoundLecture
                .param("lectureName", "lecture3").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");
        foundLecture
                .param("lectureName", "lecture3").param("year", "2022")
                .param("semester", "2").param("lectureTimeList[0].startTime", "12:00")
                .param("lectureTimeList[0].endTime", "13:50").param("limitNum", "12");

        //then
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
}
