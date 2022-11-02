package com.studentinformation.controller;

import com.studentinformation.util.WithCustomMember;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HomeControllerTest extends ControllerTestSetup{

    /**
     * andExpect 마지막에 .andDo(print())를 호출하면 상태를 볼 수 있다.
     */

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
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
    @WithCustomMember(TEST_STUDENT_NUM)
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