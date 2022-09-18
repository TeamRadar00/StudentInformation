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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.HandlerResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    WebApplicationContext wac;

    MockMvc mock;

    @BeforeEach
    void getMockObject() {
        mock = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void goLogin_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/members/login");
        //when
//        builder.session(createLoginSession());
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goLogin"))
                .andExpect(model().attributeExists("loginMemberForm"));
    }

    @Test
    void login_test() throws Exception {
        //given
        MockHttpServletRequestBuilder ifUnmatched = post("/members/login");
        MockHttpServletRequestBuilder ifCorrect = post("/members/login");

        //when
        ifUnmatched.param("studentNum", "asd").param("password", "qwe");
        ifCorrect.param("studentNum", "test").param("password", "test")
                .queryParam("redirectURL","/home");

        //then
        mock.perform(ifUnmatched)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(view().name("members/login"));
        mock.perform(ifCorrect)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("login"))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void goPassword_test() throws Exception {
        //given
        String url = "/members/password";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder existSession = get(url);
        //when
        existSession.session(createLoginSession());
        //then
        ifNoSessionThenRedirect(noSession, url);

        mock.perform(existSession)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goPassword"))
                .andExpect(model().attributeExists("changePasswordForm"));
    }

    @Test
    void changePassword_test() throws Exception {
        //given
        String url = "/members/password";
        MockHttpServletRequestBuilder noSession = post(url);
        MockHttpServletRequestBuilder passwordError = post(url);
        MockHttpServletRequestBuilder passwordCorrect = post(url);

        //when
        passwordError.session(createLoginSession())
                .param("prePassword","asd")
                .param("newPassword","qwe")
                .param("confirmPassword","qwe");
        passwordCorrect.session(createLoginSession())
                .param("prePassword","test")
                .param("newPassword","qwe")
                .param("confirmPassword","qwe");

        //then
        ifNoSessionThenRedirect(noSession,url);

        mock.perform(passwordError)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("changePassword"))
                .andExpect(view().name("members/password"));

//        이거 테스트 돌리면 진짜 비번이 업뎃되는데 어카지
//        mock.perform(passwordCorrect)
//                .andExpect(handler().handlerType(MemberController.class))
//                .andExpect(handler().methodName("changePassword"))
//                .andExpect(redirectedUrl("/home"));
    }

    private MockHttpSession createLoginSession() {
        Member emptyMember = new Member("test", "test", "test", null, null);
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, emptyMember);
        return session;
    }

    private void ifNoSessionThenRedirect(MockHttpServletRequestBuilder noSession, String url) throws Exception {
        mock.perform(noSession)
                .andExpect(redirectedUrl("/members/login?redirectURL="+url));
    }
}
