package com.studentinformation.domain.form;


import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@AllArgsConstructor
public class MemberForm {

    private String studentNum;
    private String memberName;
    private MemberState state;
    private String collegeName;

    // 이부분 널값으로 넣어도 되는지는 지켜봐야될 듯
    public Member toEntity(){ return new Member(studentNum,null,memberName,state,collegeName); }

    public static MemberForm createMemberForm(Member member){
        return new MemberForm(member.getStudentNum(), member.getMemberName(), member.getState(), member.getCollegeName());
    }

    // admin이 회원가입했을 때 초기 비밀번호는 학번과 동일하다
    public Member registerMember() { return new Member(studentNum,studentNum,memberName,state,collegeName); }
}
