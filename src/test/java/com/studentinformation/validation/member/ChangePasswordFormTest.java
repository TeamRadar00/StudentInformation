package com.studentinformation.validation.member;

import com.studentinformation.web.form.member.ChangePasswordForm;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ChangePasswordFormTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    public static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void close() {
        factory.close();
    }

    @Test
    void 모든칸_Black() throws Exception {
        //given
        ChangePasswordForm empty = new ChangePasswordForm("","", "");
        ChangePasswordForm whitespace = new ChangePasswordForm(" ", " ", " ");
        //when
        Set<ConstraintViolation<ChangePasswordForm>> validate1 = validator.validate(empty);
        Set<ConstraintViolation<ChangePasswordForm>> validate2 = validator.validate(whitespace);
        //then
        validate1.forEach(error -> assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다"));
        assertThat(validate1.size()).isEqualTo(3);

        validate2.forEach(error -> assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다"));
        assertThat(validate2.size()).isEqualTo(3);
    }

    @Test
    void 두_비밀번호_일치여부() throws Exception {
        //given
        ChangePasswordForm different = new ChangePasswordForm("123","qwe", "wer");
        ChangePasswordForm same = new ChangePasswordForm("123","qwe", "qwe");
        //when
        //then
        assertThat(different.isPasswordEqual()).isFalse();
        assertThat(same.isPasswordEqual()).isTrue();
    }
}
