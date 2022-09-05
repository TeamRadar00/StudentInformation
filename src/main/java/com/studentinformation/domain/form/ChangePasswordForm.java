package com.studentinformation.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ChangePasswordForm {

    private String prePassword;
    private String newPassword;
    private String confirmPassword;

    public boolean isPasswordEqual() {
        return newPassword.equals(confirmPassword);
    }
}
