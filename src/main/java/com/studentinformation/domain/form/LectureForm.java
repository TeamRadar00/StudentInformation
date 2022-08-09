package com.studentinformation.domain.form;

import com.studentinformation.domain.Member;
import com.studentinformation.domain.Week;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetTime;

@Getter
@ToString
@AllArgsConstructor
public class LectureForm {

    private String lectureName;
    private MemberForm professor;
    private String semester;
    private Week week;
    private OffsetTime time;
    private int limitNum;

}
