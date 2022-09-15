package com.studentinformation.domain.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import static org.springframework.util.StringUtils.*;

@Getter
@ToString
@AllArgsConstructor
public class SearchLectureForm {

    private String year;
    private String semester;
    private String selectOne;
    private String content;

    public boolean isAllExist() {
        return hasText(year) && hasText(semester) &&hasText(selectOne) &&hasText(content);
    }
}
