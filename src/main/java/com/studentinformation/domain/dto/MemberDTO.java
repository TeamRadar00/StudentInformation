package com.studentinformation.domain.dto;


import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class MemberDTO {

    private String studentNum;
    private String password;
    private String memberName;
    private MemberState state;
    private String collegeName;

    public MemberDTO(String studentNum, String password, String memberName, MemberState state, String collegeName) {
        this.studentNum = studentNum;
        this.password = password;
        this.memberName = memberName;
        this.state = state;
        this.collegeName = collegeName;
    }

    public Member toEntity(){
        return new Member(studentNum,password,memberName,state,collegeName);
    }
}
