package com.studentinformation.web.form.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class FindPasswordForm {

    @NotBlank
    private String memberName;

    @NotBlank
    private String studentNum;

    private String message;

    public void updateMessage(String message) {
        this.message = message;
    }
}
