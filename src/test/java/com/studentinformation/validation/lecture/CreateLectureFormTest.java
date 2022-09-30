package com.studentinformation.validation.lecture;

import com.studentinformation.web.form.lecture.CRUDLectureForm;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class CreateLectureFormTest {
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
    void 모든칸_빈칸() throws Exception {
        //given
        CRUDLectureForm form = new CRUDLectureForm("", "", "1", null, null);
        //when

        //then

    }
}
