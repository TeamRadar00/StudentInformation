package com.studentinformation.domain.form;

import com.studentinformation.domain.MemberState;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberForm {

    private String studentNum;
    private String memberName;
    private MemberState state;
    private String collegeName;

}
