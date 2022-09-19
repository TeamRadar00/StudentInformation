package com.studentinformation.validation;

import com.studentinformation.domain.form.LoginMemberForm;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

/**
 * 스프링부트테스트가 필요한 validation test는 controller test단으로 넘겼고 이외의 validation은 여기로 넣음
 * 원래 controller 테스트에 다 넣으려고 했는데 너무 많아져서 쪼갬. 이 방법이 좋은진 모르겠다.
 */
public class LoginMemberFormTest {

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
    void 학번과비밀번호_Black() throws Exception {
        //given
        LoginMemberForm empty = new LoginMemberForm("", "");
        LoginMemberForm whitespace = new LoginMemberForm(" ", " ");
        //when
        Set<ConstraintViolation<LoginMemberForm>> validate1 = validator.validate(empty);
        Set<ConstraintViolation<LoginMemberForm>> validate2 = validator.validate(whitespace);
        //then
        validate1.forEach(error -> assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다"));
        assertThat(validate1.size()).isEqualTo(2);

        validate2.forEach(error -> assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다"));
        assertThat(validate2.size()).isEqualTo(2);
    }
    @Test
    void 학번_black() throws Exception {
        //given
        LoginMemberForm emptyStudentNum = new LoginMemberForm("", "123");
        //when
        Set<ConstraintViolation<LoginMemberForm>> validate = validator.validate(emptyStudentNum);
        //then
        validate.forEach(error -> assertThat(error.getPropertyPath().toString()).isEqualTo("studentNum"));
        assertThat(validate.size()).isEqualTo(1);
    }
    @Test
    void 비밀번호_black() throws Exception {
        //given
        LoginMemberForm emptyPassword = new LoginMemberForm("123", "");
        //when
        Set<ConstraintViolation<LoginMemberForm>> validate3 = validator.validate(emptyPassword);
        //then
        validate3.forEach(error -> assertThat(error.getPropertyPath().toString()).isEqualTo("password"));
        assertThat(validate3.size()).isEqualTo(1);
    }
}
