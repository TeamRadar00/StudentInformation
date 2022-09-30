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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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

    /**
     * andExpect 마지막에 .andDo(print())를 호출하면 상태를 볼 수 있다.
     */

    @Test
    void goHome_test() throws Exception {
        //given
        MockHttpServletRequestBuilder noSession = get("/");
        MockHttpServletRequestBuilder builder = get("/");
        //when
//        builder.session(createLoginSession());
        //then
        mock.perform(noSession)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("goHome"))
                .andExpect(redirectedUrl("/members/login?redirectURL=/"));

        mock.perform(builder)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("goHome"))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void home_test() throws Exception {
        //given
        MockHttpServletRequestBuilder noSession = get("/home");
        MockHttpServletRequestBuilder builder = get("/home");
        //when
        builder.session(createLoginSession());
        //then
        mock.perform(noSession)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("home"))
                .andExpect(redirectedUrl("/members/login?redirectURL=/home"));

        mock.perform(builder)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("home"))
                .andExpect(model().attributeExists("form"));
    }

    private MockHttpSession createLoginSession() {
        Member emptyMember = new Member(null, null, null, null, null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, emptyMember);
        return session;
    }
}