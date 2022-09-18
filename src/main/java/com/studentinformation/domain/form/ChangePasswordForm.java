package com.studentinformation.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@AllArgsConstructor
public class ChangePasswordForm {

    @NotBlank
    private String prePassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;

    public boolean isPasswordEqual() {
        return newPassword.equals(confirmPassword);
    }
}
