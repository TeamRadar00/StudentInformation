package com.studentinformation.service;


import com.studentinformation.domain.*;
import com.sun.jdi.request.DuplicateRequestException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author sim
 * https://tecoble.techcourse.co.kr/post/2020-09-15-test-isolation/
 * https://hudi.blog/isolated-test/
 * Service에서는 격리수준을 transactional로 해도 될듯
 * 아니면 실제 controller에서 사용한다고 가정하며는 transactional걷어내고 다른 방법 쓰면 될듯
 */

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired private MemberService memberService;

    @Test
    public void 멤버추가테스트() throws Exception {
        Member test = new Member("test","test","test",
                        MemberState.inSchool,"test");

        Member saveMember = memberService.addMember(test);

        Assertions.assertThat(test).isEqualTo(saveMember);
    }

    @Test
    public void 멤버정보업데이트() throws Exception {
        //given
        Member test = makeTestMember();

        Member changeMember = new Member(test.getStudentNum(),"change",test.getMemberName(),
                                test.getState(),test.getCollegeName());
        //when
        memberService.update(test.getId(), changeMember);

        Member changedMember = memberService.findById(test.getId());
        //then
        Assertions.assertThat("change").isEqualTo(changedMember.getPassword());
    }

    @Test
    public void 비밀번호변경() throws Exception {
        //given
        Member test = makeTestMember();
        //when
        memberService.updatePassword(test.getId(), "change");
        //then
        Assertions.assertThat("change").isEqualTo(test.getPassword());
    }

    @Test
    @DisplayName("비밀번호가 중복됐을 경우 예외처리")
    public void 비밀번호변경_예외처리() throws Exception {
        //given
        Member test = makeTestMember();
        //when
        //then
        Assertions.assertThatThrownBy(
                ()->memberService.updatePassword(test.getId(),"test"))
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
        Assertions.assertThat(studentNum).isEqualTo(test.getStudentNum());
    }
    @Test
    @DisplayName("학번에 해당하는 맴버가 없을 경우 예외처리")
    public void 아이디찾기_예외처리() throws Exception {
        Assertions.assertThatThrownBy(
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
        Assertions.assertThat(test.getPassword()).isEqualTo(findPassword);
    }

    @Test
    @DisplayName("1. 학번에 해당하는 맴버는 있으나, 학생 이름이 DB와 입력값이 다를 경우" +
            "2. 학번에 해당하는 맴버 자체가 없을 경우")
    public void 비밀번호찾기_예외처리() throws Exception {
        //given
        Member test = makeTestMember();
        //then
        Assertions.assertThatThrownBy(
                ()->memberService.findPassword("not_match_id",test.getStudentNum()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("not match memberName with studentNum data");

        Assertions.assertThatThrownBy(
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
