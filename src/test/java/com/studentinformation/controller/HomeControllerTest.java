package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.web.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class HomeControllerTest {

    @Autowired
    WebApplicationContext wac;

    MockMvc mock;

    @BeforeEach
    void getMockObject() {
        mock = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void goHome_test() throws Exception {
        //given
        Member emptyMember = new Member(null, null, null, null, null);
        MockHttpServletRequestBuilder builder = get("/")
                .session(createLoginSession(emptyMember));
        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("goHome"))
                .andExpect(redirectedUrl("/home"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void home_test() throws Exception {
        //given
        Member emptyMember = new Member(null, null, null, null, null);
        MockHttpServletRequestBuilder builder = get("/").session(createLoginSession(emptyMember));
        //when
        //then
//        mock.perform(builder)
//                .andExpect(handler().handlerType(HomeController.class))
//                .andExpect(handler().methodName("home"))
//                .
    }

    private MockHttpSession createLoginSession(Member emptyMember) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, emptyMember);
        return session;
    }
}