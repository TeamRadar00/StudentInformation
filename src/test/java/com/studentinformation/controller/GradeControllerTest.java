package com.studentinformation.controller;


import com.studentinformation.domain.*;
import com.studentinformation.repository.GradeRepository;
import com.studentinformation.repository.MemberRepository;
import com.studentinformation.web.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class GradeControllerTest {

    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    WebApplicationContext was;

    private MockMvc mock;
    private Member member;
    private List<Grade> gradeList;
    private Long lectureId;
    protected MockHttpSession session;

    static final String BASE_URL = "/grade";

    @BeforeEach
    public void init(){
        mock = MockMvcBuilders.webAppContextSetup(was).build();
        member = memberRepository.findByMemberName("test").get();
        gradeList = gradeRepository.findByStudentId(member.getId());
        lectureId = gradeList.get(0).getLecture().getId();
        session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER,member);
    }

    @Test
    public void getGoMyGradeTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/myGrade");
        //when
        //then
        mock.perform(builder.session(session))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("goMyGrade"))
                .andExpect(model().attributeExists("myGrade"))
                .andDo(print());

    }

    @Test
    public void getGoObjectionTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/objection");
        //when
        //then
        mock.perform(builder.session(session))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("goObjection"))
                .andExpect(model().attributeExists("gradeList"))
                .andDo(print());
    }

    @Test
    public void postGoObjectionTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = post(BASE_URL + "/objection");
        //when
        //then
        mock.perform(builder.session(session)
                        .param("gradeId",Long.toString(gradeList.get(0).getId()))
                        .param("objection","test"))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("submitObjection"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grade/objection"))
                .andDo(print());

    }


    @Test
    public void getReadObjectionTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/readObjection/{gradeId}", Long.toString(gradeList.get(0).getId()));
        //when

        //then
        mock.perform(builder.session(session))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("readObjection"))
                .andExpect(model().attributeExists("grade"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void postEditScoreThroughObjectionListTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = post(BASE_URL + "/readObjection/{gradeId}", Long.toString(gradeList.get(0).getId()));
        //when
        //then
        mock.perform(builder.session(session)
                        .param("gradeScore",Score.A_PLUS.toString()))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("editScoreThroughObjectionList"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grade/objectionList"))
                .andDo(print());

    }

    @Test
    public void getGoObjectionListTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builderWithLectureId = get(BASE_URL + "/objectionList");
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/objectionList");
        builderWithLectureId.param("lectureId",Long.toString(lectureId));


        //when
        //then
        mock.perform(builderWithLectureId.session(session))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("goObjectionList"))
//                .andExpect(model().attributeExists("getObjectionListForm")) //redirect 후 .addAttribute 정보 사라짐
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grade/objectionList/"+lectureId))
                .andDo(print());

        mock.perform(builder.session(session))
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("goObjectionList"))
                .andExpect(model().attributeExists("getObjectionListForm"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getGetObjectionList() throws Exception {
        //given
        MockHttpServletRequestBuilder builderWithGradeId = get(BASE_URL + "/objectionList/{lectureId}", lectureId)
                .session(session);
        MockHttpServletRequestBuilder builderWithNewLectureId = get(BASE_URL + "/objectionList/{lectureId}", lectureId)
                .session(session);
        MockHttpServletRequestBuilder builderWithPage = get(BASE_URL + "/objectionList/{lectureId}", lectureId)
                .session(session);


        builderWithGradeId.param("gradeId",Long.toString(gradeList.get(0).getId()));
        builderWithNewLectureId.param("lectureId",Long.toString(gradeList.get(1).getLecture().getId()));
        builderWithPage.param("page","1");
        //when
        //then
        mock.perform(builderWithGradeId)
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("getObjectionList"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grade/readObjection/"+gradeList.get(0).getId()))
                .andDo(print());

        mock.perform(builderWithNewLectureId)
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("getObjectionList"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grade/objectionList/"+gradeList.get(1).getLecture().getId()))
                .andDo(print());

        mock.perform(builderWithPage)
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("getObjectionList"))
                .andExpect(model().attributeExists("getObjectionListForm"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getGoGiveGradeTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/giveGrade");
        builder.session(session)
                .param("lectureId",Long.toString(lectureId));
        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("goGiveGrade"))
                .andExpect(model().attributeExists("GradeGoGiveGradeForm"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void postSubmitGiveGradeTest() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = post(BASE_URL + "/giveGrade");
        builder.session(session)
                .param("gradeScore","IN")
                .param("lectureId",Long.toString(lectureId));
        //when

        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(GradeController.class))
                .andExpect(handler().methodName("submitGiveGrade"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/grade/giveGrade"))
                .andDo(print());

    }

}
