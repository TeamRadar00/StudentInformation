package com.studentinformation.domain.form;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.MemberState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class LoginMemberForm {

    @NotBlank
    private String studentNum;

    @NotBlank
    private String password;

}
