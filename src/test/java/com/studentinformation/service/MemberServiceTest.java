package com.studentinformation.service;


import com.studentinformation.domain.*;
import com.studentinformation.web.form.member.ChangePasswordForm;
import com.sun.jdi.request.DuplicateRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired private MemberService memberService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 멤버추가테스트() throws Exception {
        Member test = new Member("test","test","test",
                        MemberState.inSchool,"test");

        Member saveMember = memberService.addMember(test);

        assertThat(test).isEqualTo(saveMember);
    }

    @Test
    public void 멤버정보업데이트() throws Exception {
        //given
        Member test = makeTestMember();

        String newPassword = "change";
        String newCollegeName = "newCollegeName";
        Member changeMember = new Member(test.getStudentNum(),newPassword,test.getMemberName(),
                                test.getState(), newCollegeName);
        //when
        memberService.update(test.getId(), changeMember);

        //then
        assertThat(passwordEncoder.matches(newPassword, test.getPassword())).isTrue();
        assertThat(test.getCollegeName()).isEqualTo(newCollegeName);
    }

    @Test
    public void 비밀번호변경() throws Exception {
        //given
        Member test = makeTestMember();

        //when
        String newPassword = "change";
        ChangePasswordForm form = new ChangePasswordForm("test", "change", "change");
        memberService.updatePassword(memberService.findById(test.getId()), form);

        //then
        assertThat(passwordEncoder.matches(newPassword, test.getPassword())).isTrue();
    }

    @Test
    @DisplayName("비밀번호가 중복됐을 경우 예외처리")
    public void 비밀번호변경_예외처리() throws Exception {
        //given
        Member test = makeTestMember();
        ChangePasswordForm form = new ChangePasswordForm("test", "test", "change");
        //when
        //then
        assertThatThrownBy(
                () -> memberService.updatePassword(test, form))
                .isInstanceOf(DuplicateRequestException.class)
                .hasMessage("newPassword is duplicated oldPassword");
    }

    @Test
    public void 아이디찾기() throws Exception {
        //given
        Member test = makeTestMember();
        //when
        String studentNum = memberService.findStudentNum(test.getMemberName());
        //then
        assertThat(studentNum).isEqualTo(test.getStudentNum());
    }

    @Test
    @DisplayName("학번에 해당하는 맴버가 없을 경우 예외처리")
    public void 아이디찾기_예외처리() throws Exception {
        assertThatThrownBy(
                ()->memberService.findStudentNum("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not found memberName data");
    }

    @Test
    public void 비밀번호찾기() throws Exception {
        //given
        Member test = makeTestMember();
        //when
        String findPassword = memberService.findPassword(test.getMemberName(),test.getStudentNum());
        //then
        assertThat(test.getPassword()).isEqualTo(findPassword);
    }

    @Test
    @DisplayName("1. 학번에 해당하는 맴버는 있으나, 학생 이름이 DB와 입력값이 다를 경우" +
            "2. 학번에 해당하는 맴버 자체가 없을 경우")
    public void 비밀번호찾기_예외처리() throws Exception {
        //given
        Member test = makeTestMember();
        //then
        assertThatThrownBy(
                ()->memberService.findPassword("not_match_id",test.getStudentNum()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not match memberName with studentNum data");

        assertThatThrownBy(
                        ()->memberService.findPassword("not_exist_name", "not_exist_num"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not found studentNum data");
    }


    private Member makeTestMember() {
        Member test = new Member("test","test","test",
                MemberState.inSchool,"test");

        memberService.addMember(test);
        return test;
    }

}
