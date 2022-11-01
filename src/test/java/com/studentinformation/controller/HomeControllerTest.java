package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.web.session.SessionConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class HomeControllerTest {

    static final String TEST_STUDENT_NUM = "student";

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
    @WithUserDetails(value = TEST_STUDENT_NUM)
    void goHome_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/");
        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("goHome"))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithUserDetails(value = TEST_STUDENT_NUM)
    void home_success_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/home");
        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("home"))
                .andExpect(model().attributeExists("form"));
    }
}