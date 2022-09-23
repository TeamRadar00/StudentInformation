package com.studentinformation.web.form.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class LoginMemberForm {

    @NotBlank
    private String studentNum;

    @NotBlank
    private String password;

}
