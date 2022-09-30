package com.studentinformation.web.form.lecture;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

import static org.springframework.util.StringUtils.*;

@Getter
@ToString
@AllArgsConstructor
public class SearchLectureForm {

    @NotBlank
    private String year;

    // 1 or 2
    private String semester;

    //professor or lectureName
    private String selectOne;

    @NotBlank
    private String content;

}
