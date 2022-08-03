package com.studentinformation.domain.dto;

import com.studentinformation.domain.MemberState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginMemberForm {

    private String studentNum;
    private String memberName;
    private MemberState state;
    private String collegeName;

}
