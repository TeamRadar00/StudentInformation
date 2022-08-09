package com.studentinformation.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class SearchLectureForm {

    private String year;
    private String semester;
    private String selectOne;
    private String content;
}
