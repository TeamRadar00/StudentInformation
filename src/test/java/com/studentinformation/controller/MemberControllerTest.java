package com.studentinformation.controller;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
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
public class MemberControllerTest {

    @Autowired WebApplicationContext wac;
    @Autowired MemberRepository memberRepository;
    private MockMvc mock;
    private static Member emptyMember;
    private static Member adminMember;

    @BeforeAll
    static void createMember() {
        emptyMember = new Member("MCT_member", "MCT_member", "MCT_member", MemberState.inSchool, null);
        adminMember = new Member("MCT_admin", "MCT_admin", "MCT_admin", MemberState.admin, "MCT_admin");
    }

    @BeforeEach
    void getMockObject() {
        mock = MockMvcBuilders.webAppContextSetup(wac).build();
        memberRepository.save(emptyMember); //id값이 생성되지 않기에 generatedValue로 만들어지도록 db에 저장함
        memberRepository.save(adminMember);
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
                .param("prePassword","MCT_member")
                .param("newPassword","qwe")
                .param("confirmPassword","qwe");

        //then
        ifNoSessionThenRedirect(noSession,url);

        mock.perform(passwordError)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("changePassword"))
                .andExpect(view().name("members/password"));

        mock.perform(passwordCorrect)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("changePassword"))
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void goFindPassword_test() throws Exception {
        //given
        String url = "/members/find-member";
        MockHttpServletRequestBuilder builder = get(url);
        //when
//        builder.session(createLoginSession());
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goFindPassword"))
                .andExpect(model().attributeExists("findIdForm"))
                .andExpect(model().attributeExists("findPasswordForm"));
    }
    
    @Test
    void findId_test() throws Exception {
        //given
        String url = "/members/find-id";
        MockHttpServletRequestBuilder blackField = post(url).param("memberName"," ");
        MockHttpServletRequestBuilder notExistName = post(url).param("memberName","notExistName");
        MockHttpServletRequestBuilder existName = post(url).param("memberName","test");

        //when
        //then
        mock.perform(blackField)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findId"))
                .andExpect(model().attributeHasFieldErrors("findIdForm", "memberName"))
                .andExpect(model().attributeExists("findPasswordForm"));

        mock.perform(notExistName)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findId"))
                .andExpect(model().attributeHasErrors("findIdForm"))
                .andExpect(model().attributeExists("findPasswordForm"));

        mock.perform(existName)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findId"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    void findPassword_test() throws Exception {
        //given
        String url = "/members/find-password";
        MockHttpServletRequestBuilder blackField = post(url)
                .param("memberName", " ").param("studentNum", " ");
        MockHttpServletRequestBuilder incorrectField = post(url)
                .param("memberName", "incorrectField").param("studentNum", "incorrectField");
        MockHttpServletRequestBuilder existName = post(url)
                .param("memberName","test").param("studentNum","test");
        //when
        //then
        mock.perform(blackField)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(model().attributeExists("findIdForm"))
                .andExpect(model().attributeHasFieldErrors("findPasswordForm", "memberName"))
                .andExpect(model().attributeHasFieldErrors("findPasswordForm", "studentNum"));

        mock.perform(incorrectField)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(model().attributeHasErrors("findPasswordForm"))
                .andExpect(model().attributeExists("findIdForm"));

        mock.perform(existName)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("findPassword"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    void goRegister_test() throws Exception {
        //given
        String url = "/admin";
        MockHttpServletRequestBuilder noSession = get(url);
        MockHttpServletRequestBuilder studentSession = get(url);
        MockHttpServletRequestBuilder adminSession = get(url);

        //when
        studentSession.session(createLoginSession());
        adminSession.session(createAdminSession());

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goRegister"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(adminSession)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goRegister"))
                .andExpect(view().name("members/admin"));
    }

    @Test
    void register_test() throws Exception {
        //given
        String url = "/admin";
        MockHttpServletRequestBuilder noSession = post(url);
        MockHttpServletRequestBuilder studentSession = post(url);
        MockHttpServletRequestBuilder blackField = post(url).param("studentNum"," ")
                .param("memberName"," ").param("state","inSchool").param("collegeName"," ");
        MockHttpServletRequestBuilder duplicateMember = post(url).param("studentNum","MCT_member")
                .param("memberName","MCT_member").param("state","inSchool")
                .param("collegeName","MCT_member");
        MockHttpServletRequestBuilder registerMember = post(url).param("studentNum","new")
                .param("memberName","new").param("state","inSchool")
                .param("collegeName","new");

        //when
        studentSession.session(createLoginSession());
        blackField.session(createAdminSession());
        duplicateMember.session(createAdminSession());
        registerMember.session(createAdminSession());

        //then
        ifNoSessionThenRedirect(noSession, url);
        mock.perform(studentSession)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
        mock.perform(blackField)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(model().errorCount(3))
                .andExpect(view().name("members/admin"));
        mock.perform(duplicateMember)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(model().hasErrors())
                .andExpect(view().name("members/admin"));
        mock.perform(registerMember)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("register"))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/members/login"));
    }

    @Test
    void logout_test() throws Exception {
        //given
        String url = "/members/logout";
        MockHttpServletRequestBuilder builder = get(url);
        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("logout"))
                .andExpect(request().sessionAttributeDoesNotExist(SessionConst.LOGIN_MEMBER))
                .andExpect(flash().attributeExists("msg"))
                .andExpect(redirectedUrl("/home"));
    }


    private MockHttpSession createLoginSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, emptyMember);
        return session;
    }

    private MockHttpSession createAdminSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, adminMember);
        return session;
    }

    private void ifNoSessionThenRedirect(MockHttpServletRequestBuilder noSession, String url) throws Exception {
        mock.perform(noSession)
                .andExpect(redirectedUrl("/members/login?redirectURL="+url));
    }

}
