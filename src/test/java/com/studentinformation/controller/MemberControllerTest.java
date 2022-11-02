package com.studentinformation.controller;

import com.studentinformation.util.WithCustomMember;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberControllerTest extends ControllerTestSetup{

    static final String BASE_URL = "/members";

    @Test
    void goLogin_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/login");
        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goLogin"))
                .andExpect(model().attributeExists("loginMemberForm"));
    }

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
    void goPassword_test() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/password");

        //when
        //then
        mock.perform(builder)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goPassword"))
                .andExpect(model().attributeExists("changePasswordForm"));
    }

    @Test
    @WithCustomMember(TEST_STUDENT_NUM)
    void changePassword_test() throws Exception {
        //given
        String url = BASE_URL + "/password";
        MockHttpServletRequestBuilder passwordError = post(url);
        MockHttpServletRequestBuilder passwordCorrect = post(url);

        //when
        passwordError
                .param("prePassword","asd")
                .param("newPassword","qwe")
                .param("confirmPassword","qwe");
        passwordCorrect
                .param("prePassword","student")
                .param("newPassword","qwe")
                .param("confirmPassword","qwe");

        //then
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
        MockHttpServletRequestBuilder builder = get(BASE_URL + "/find-member");

        //when
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
        String url = BASE_URL + "/find-id";
        MockHttpServletRequestBuilder blackField = post(url).param("memberName"," ");
        MockHttpServletRequestBuilder notExistName = post(url).param("memberName","notExistName");
        MockHttpServletRequestBuilder existName = post(url).param("memberName","student");

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
        String url = BASE_URL + "/find-password";
        MockHttpServletRequestBuilder blackField = post(url)
                .param("memberName", " ").param("studentNum", " ");
        MockHttpServletRequestBuilder incorrectField = post(url)
                .param("memberName", "incorrectField").param("studentNum", "incorrectField");
        MockHttpServletRequestBuilder existName = post(url)
                .param("memberName","student").param("studentNum","student");
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
    @WithCustomMember(TEST_ADMIN_NUM)
    void goRegister_test() throws Exception {
        //given
        MockHttpServletRequestBuilder adminSession = get("/admin");

        //when
        //then
        mock.perform(adminSession)
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("goRegister"))
                .andExpect(view().name("members/admin"));
    }

    @Test
    @WithCustomMember(TEST_ADMIN_NUM)
    void register_test() throws Exception {
        //given
        String url = "/admin";
        MockHttpServletRequestBuilder blackField = post(url).param("studentNum"," ")
                .param("memberName"," ").param("state","inSchool").param("collegeName"," ");
        MockHttpServletRequestBuilder duplicateMember = post(url).param("studentNum","student")
                .param("memberName","student").param("state","inSchool")
                .param("collegeName","student");
        MockHttpServletRequestBuilder registerMember = post(url).param("studentNum","new")
                .param("memberName","new").param("state","inSchool")
                .param("collegeName","new");

        //when
        //then
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

}
